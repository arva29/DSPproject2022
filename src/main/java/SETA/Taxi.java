package SETA;

import AdministratorServer.Beans.TaxiNetworkInfo;
import SETA.Data.Position;
import com.example.grpc.TaxiNetworkServiceOuterClass;
import org.eclipse.paho.client.mqttv3.MqttException;

import java.util.*;

public class Taxi {
    private static Boolean free = true; //True if it is free to accept a ride
    private static final Object freeLock = new Object();
    private static Boolean recharging = false; //True if it is recharging
    private static final Map<Integer, Boolean> eligibilityMap = new HashMap<>();

    private static final int ID = new Random().nextInt(1000) + 1400;
    //private static final int ID = 1440;
    private static final String IP_ADDRESS = "127.0.0.1";
    private static final int PORT = ID;

    private static int batteryLvl = 90;
    private static Position position;
    private static final TaxiNetworkInfo taxiNetworkInfo = new TaxiNetworkInfo(ID, IP_ADDRESS, PORT);
    private static final List<TaxiNetworkInfo> taxiNetwork = new ArrayList<>();
    private static final List<TaxiNetworkInfo> rechargeQueue = new ArrayList<>();

    public static void main(String[] args) throws InterruptedException, MqttException {

        RESTServerModule restServerModule;
        RideManagementModule rideManagement;
        NetworkCommunicationModule networkCommunicationModule;

        restServerModule = new RESTServerModule();

        // ADDING TAXI TO THE NETWORK
        restServerModule.addTaxiToNetwork();

        networkCommunicationModule = new NetworkCommunicationModule();
        networkCommunicationModule.start();
        networkCommunicationModule.notifyPresenceToNetwork(taxiNetwork, taxiNetworkInfo);

        System.out.println("--- TAXI ---\n - ID: " + ID + "\n - POS: " + position.getX() + "," + position.getY() + "\n");

        rideManagement = new RideManagementModule(position.getDistrict(), networkCommunicationModule);
        rideManagement.start();

        Thread stdinThread = new Thread(() -> {
            Scanner scanner = new Scanner(System.in);
            String line;
            while (true){
                line = scanner.nextLine();
                if(line.equals("quit")){
                    break;
                } else if(line.equals("recharge")){
                    if(batteryLvl < 100) {
                        //if(!isFree()){
                        //    System.out.println("..waiting");
                        //    getFreeLock().wait();
                        //}
                        try {
                            rideManagement.recharge(false);
                        } catch (InterruptedException | MqttException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
            try {
                /**
                 * todo SOLVE WITH SYNC ????
                 */
                while (true) {
                    if(free) {
                        leaveNetwork(restServerModule, rideManagement, networkCommunicationModule);
                        break;
                    }
                }
            } catch (MqttException e) {
                e.printStackTrace();
            }
        });
        stdinThread.start();

    }

    public static synchronized void addTaxiToNetwork(TaxiNetworkInfo newTaxi){
        taxiNetwork.add(newTaxi);
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
        System.out.println("New position -> (" + position.getX() + "," + position.getY() + ")");
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

    public static synchronized void setFree(boolean free) {
        System.out.println("------ free -> " + free);
        Taxi.free = free;
    }

    public static synchronized Object getFreeLock(){
        return freeLock;
    }

    public static synchronized Boolean isRecharging() {
        return recharging;
    }

    public static synchronized void setRecharging(boolean recharging) {
        Taxi.recharging = recharging;
    }

    public static synchronized Map<Integer, Boolean> getEligibilityMap(){
        return eligibilityMap;
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

    public static void addAllToRechargeQueue(List<TaxiNetworkServiceOuterClass.TaxiInformation> taxiList){
        for(TaxiNetworkServiceOuterClass.TaxiInformation i: taxiList){
            addToRechargeQueue(new TaxiNetworkInfo(i));
        }
    }

    private static void leaveNetwork(RESTServerModule restServerModule, RideManagementModule rideManagement, NetworkCommunicationModule networkCommunicationModule) throws MqttException {
        restServerModule.removeTaxiFromNetwork();
        rideManagement.disconnect();
        networkCommunicationModule.disconnect();

        /**
         * TODO - Ending task - Notify other taxis ??
         */
    }
}
