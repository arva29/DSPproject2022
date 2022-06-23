package SETA;

import AdministratorServer.Beans.TaxiNetworkInfo;
import SETA.Data.Position;
import SETA.Data.RechargingTrigger;
import com.example.grpc.TaxiNetworkServiceOuterClass;
import org.eclipse.paho.client.mqttv3.MqttException;

import java.sql.Timestamp;
import java.util.*;

public class Taxi {
    private static Boolean free = true; //True if it is free to accept a ride
    private static Boolean inElection = false; //True if Taxi is involved in an election
    private static Boolean eligible = true; //True if Taxi is involved in an election
    private static Boolean recharging = false; //True if it is recharging
    private static int currentElection = 0; //Request ID for the current election
    private static RechargingTrigger askingForRecharging = new RechargingTrigger(false); //True if it is asking for recharging
    public static final Object electionLock = new Object();

    private static final int ID = new Random().nextInt(1000) + 1400;
    //private static final int ID = 1440;
    private static final String IP_ADDRESS = "127.0.0.1";
    private static final int PORT = ID;

    private static int batteryLvl = 90;
    private static Position position;
    private static final TaxiNetworkInfo taxiNetworkInfo = new TaxiNetworkInfo(ID, IP_ADDRESS, PORT);
    private static final List<TaxiNetworkInfo> taxiNetwork = new ArrayList<>();
    private static final List<TaxiNetworkInfo> rechargeQueue = new ArrayList<>();
    private static final List<Integer> rideAccomplished = new ArrayList<>();

    public static void main(String[] args) throws InterruptedException, MqttException {

        RESTServerModule restServerModule;
        RideManagementModule rideManagement;
        NetworkCommunicationModule networkCommunicationModule;

        restServerModule = new RESTServerModule();

        // ADDING TAXI TO THE NETWORK
        restServerModule.addTaxiToNetwork();

        networkCommunicationModule = new NetworkCommunicationModule();
        networkCommunicationModule.start();
        networkCommunicationModule.notifyPresenceToNetwork();

        System.out.println("--- TAXI ---\n - ID: " + ID + "\n - POS: " + position.getX() + "," + position.getY() + "\n");

        rideManagement = new RideManagementModule(position.getDistrict(), networkCommunicationModule);
        rideManagement.start();

        Thread stdinThread = new Thread(() -> {
            Scanner scanner = new Scanner(System.in);
            String line;
            while (true){
                line = scanner.nextLine();
                if(line.equals("quit")){
                    if(!isFree() || isInElection() || isAskingForRecharging() || isRecharging()){ //Taxi can quit the network only if it is free
                        System.out.println("..waiting");
                        try {
                            waitForEligibility();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                    break;
                } else if(line.equals("recharge")){
                    if(batteryLvl < 100) {
                        if(!isFree()){ // ?????
                            System.out.println("..waiting");
                            try {
                                waitForEligibility();
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }
                        try {
                            rideManagement.recharge(false);
                        } catch (InterruptedException | MqttException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
            try {
                while (true) {
                    if(free) {
                        leaveNetwork(restServerModule, rideManagement, networkCommunicationModule);
                        break;
                    }
                }
            } catch (MqttException | InterruptedException e) {
                e.printStackTrace();
            }
        });
        stdinThread.start();

    }

    public static synchronized void addTaxiToNetwork(TaxiNetworkInfo newTaxi){
        taxiNetwork.add(newTaxi);
    }

    public static synchronized void removeTaxiFromNetwork(int id){
        taxiNetwork.removeIf(i -> i.getId() == id);
    }

    public static synchronized List<TaxiNetworkInfo> getTaxiNetwork(){
        return taxiNetwork;
    }

    public static int getBatteryLvl() {
        return batteryLvl;
    }

    public static void setBatteryLvl(int batteryLvl) {
        Taxi.batteryLvl = batteryLvl;
    }

    public static Position getPosition() {
        return position;
    }

    public static void setPosition(Position position) {
        Taxi.position = position;
        //System.out.println("New position -> (" + position.getX() + "," + position.getY() + ")");
    }

    public static TaxiNetworkInfo getTaxiNetworkInfo() {
        return taxiNetworkInfo;
    }

    public static void fillTaxiNetwork(List<TaxiNetworkInfo> taxiList){
        taxiNetwork.addAll(taxiList);
    }

    public static synchronized Boolean isFree() {
        return free;
    }

    public static synchronized void setFree(boolean free) throws InterruptedException {

        Taxi.free = free;

        if(free){
            Taxi.notifyEligibility();
        }
        //System.out.println("--- Free -> " + free);
    }

    public static synchronized Boolean isInElection() {
        return inElection;
    }

    public static synchronized void setInElection(Boolean inElection) {

        Taxi.inElection = inElection;
        //System.out.println("--- InElection -> " + inElection);
    }

    public static synchronized Boolean isEligible() {
        return eligible;
    }

    public static synchronized void setEligible(Boolean eligible) {

        Taxi.eligible = eligible;
        //System.out.println("--- Eligible -> " + eligible);
    }

    public static synchronized Boolean isRecharging() {
        return recharging;
    }

    public static synchronized void setRecharging(boolean recharging) {
        Taxi.recharging = recharging;
    }

    public static synchronized Boolean isAskingForRecharging() {
        return askingForRecharging.getValue();
    }

    public static synchronized RechargingTrigger getAskingForRecharging() {
        return askingForRecharging;
    }

    public static synchronized int getCurrentElection() {
        return currentElection;
    }

    public static synchronized void setCurrentElection(int currentElection) {
        Taxi.currentElection = currentElection;
    }

    public static synchronized void setAskingForRecharging(Boolean askingForRecharging) {
        Taxi.askingForRecharging.setValue(askingForRecharging);
        Taxi.askingForRecharging.setTimestamp(new Timestamp(System.currentTimeMillis()));
    }

    public static void addToRechargeQueue(TaxiNetworkInfo taxi){
        rechargeQueue.add(taxi);
    }

    public static List<TaxiNetworkInfo> getRechargeQueue(){
        return rechargeQueue;
    }

    public static void clearRechargingQueue(){
        rechargeQueue.clear();
    }

    public static synchronized void addToAccomplishedRide(Integer rideRequestId) {
        rideAccomplished.add(rideRequestId);
        //System.out.println(" - ADDED " + rideRequestId);
    }

    public static synchronized boolean rideAlreadyAccomplished(Integer rideRequestId) {
        return rideAccomplished.contains(rideRequestId);
    }

    public static void waitForEligibility() throws InterruptedException {
        synchronized (electionLock){
            System.out.println("... waiting the taxi to be free!");
            electionLock.wait();
        }
    }

    public static void notifyEligibility() throws InterruptedException {
        synchronized (electionLock){
            System.out.println("Taxi is finally free!");
            electionLock.notifyAll();
        }
    }

    private static void leaveNetwork(RESTServerModule restServerModule, RideManagementModule rideManagement, NetworkCommunicationModule networkCommunicationModule) throws MqttException, InterruptedException {
        restServerModule.removeTaxiFromNetwork();
        networkCommunicationModule.notifyLeavingNetwork();
        rideManagement.disconnect();
        networkCommunicationModule.disconnect();
    }
}
