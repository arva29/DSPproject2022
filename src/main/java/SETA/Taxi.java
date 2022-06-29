package SETA;

import AdministratorServer.Beans.TaxiNetworkInfo;
import SETA.Data.Position;
import SETA.Data.RechargingTrigger;
import org.eclipse.paho.client.mqttv3.MqttException;

import java.sql.Timestamp;
import java.util.*;

/**
 * Main entity of a taxi. Initialize all the module for managing communication with administrator server and with other taxis in the network. It also has
 * a thread running to read the system input for quitting and recharging commands.
 */
public class Taxi {
    private static Boolean free = true; //True if it is free to accept a ride
    private static Boolean inElection = false; //True if Taxi is involved in an election
    private static Boolean eligible = true; //True if Taxi is involved in an election
    private static Boolean recharging = false; //True if it is recharging
    private static int currentElection = -1; //Request ID for the current election
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
        StatisticsModule statisticsModule;

        restServerModule = new RESTServerModule();

        // ADDING TAXI TO THE NETWORK
        restServerModule.addTaxiToNetwork();

        networkCommunicationModule = new NetworkCommunicationModule();

        statisticsModule = new StatisticsModule(restServerModule);

        rideManagement = new RideManagementModule(position.getDistrict(), networkCommunicationModule, statisticsModule);

        networkCommunicationModule.start();
        networkCommunicationModule.notifyPresenceToNetwork();
        rideManagement.start();
        statisticsModule.start();

        System.out.println("--- TAXI ---\n - ID: " + ID + "\n - POS: " + position.getX() + "," + position.getY() + "\n");


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
                        /**
                         * todo rivedere
                         */
                        if(!isFree()){
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

    /**
     *
     * @param taxiList List of the
     */
    public static void fillTaxiNetwork(List<TaxiNetworkInfo> taxiList){
        taxiNetwork.addAll(taxiList);
    }

    public static synchronized Boolean isFree() {
        return free;
    }

    /**
     * Changes the state of the taxi to free or not and if it becomes free it notify it to all the waiting threads
     * @param free
     */
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

    /**
     * Set to the given boolean value the askingForRecharging field and set the timestamp of the object to the current time
     * in order to save the timestamp of the request
     * @param askingForRecharging true if the taxi is asking for recharging, false if not
     */
    public static synchronized void setAskingForRecharging(Boolean askingForRecharging) {
        Taxi.askingForRecharging.setValue(askingForRecharging);
        Taxi.askingForRecharging.setTimestamp(new Timestamp(System.currentTimeMillis()));
    }

    /**
     * Adds the taxi to the recharging queue
     * @param taxi taxi to add to the recharging queue
     */
    public static void addToRechargeQueue(TaxiNetworkInfo taxi){
        rechargeQueue.add(taxi);
    }

    public static List<TaxiNetworkInfo> getRechargeQueue(){
        return rechargeQueue;
    }

    /**
     * Clear the queue of taxis asking for recharging
     */
    public static void clearRechargingQueue(){
        rechargeQueue.clear();
    }

    /**
     * Add to the ride request to the already accomplished rides
     * @param rideRequestId ride request to add
     */
    public static synchronized void addToAccomplishedRide(Integer rideRequestId) {
        rideAccomplished.add(rideRequestId);
        //System.out.println(" - ADDED " + rideRequestId);
    }


    /**
     * Return if the ride request is already been accomplished by another taxi
     * @param rideRequestId ride request to verify
     * @return true if the list of accomplished ride contains the ride request as parameter, false if it is not in the list.
     */
    public static synchronized boolean rideAlreadyAccomplished(Integer rideRequestId) {
        return rideAccomplished.contains(rideRequestId);
    }

    /**
     * If the taxi is not free the object will wait until it will be available again
     */
    public static void waitForEligibility() throws InterruptedException {
        synchronized (electionLock){
            System.out.println("... waiting the taxi to be free!");
            electionLock.wait();
        }
    }

    /**
     * Notify to wake up objects that are waiting for taxi to be free
     */
    public static void notifyEligibility() {
        synchronized (electionLock){
            //System.out.println("Taxi is now free!");
            electionLock.notifyAll();
        }
    }

    /**
     * Method to leave the network by disconnecting the taxi from all the communications module
     * @param restServerModule module to communice with administrator server
     * @param rideManagement module to manage the ride requests
     * @param networkCommunicationModule module to communicate with other taxis in the network
     */
    private static void leaveNetwork(RESTServerModule restServerModule, RideManagementModule rideManagement, NetworkCommunicationModule networkCommunicationModule) throws MqttException, InterruptedException {
        restServerModule.removeTaxiFromNetwork();
        networkCommunicationModule.notifyLeavingNetwork();
        rideManagement.disconnect();
        networkCommunicationModule.disconnect();
    }
}
