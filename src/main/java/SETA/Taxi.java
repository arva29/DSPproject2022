package SETA;

import AdministratorServer.Beans.TaxiNetworkInfo;
import SETA.Data.Position;
import SETA.Data.RideRequest;
import org.eclipse.paho.client.mqttv3.MqttException;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;
import java.util.concurrent.atomic.AtomicBoolean;

public class Taxi {
    private static AtomicBoolean eligible = new AtomicBoolean(true);

    private static final int ID = new Random().nextInt(1000) + 1400;
    //private static final int ID = 1440;
    private static final String IP_ADDRESS = "127.0.0.1";
    private static final int PORT = ID;

    private static int batteryLvl = 100;
    private static Position position;
    private static final TaxiNetworkInfo taxiNetworkInfo = new TaxiNetworkInfo(ID, IP_ADDRESS, PORT);
    private static final List<TaxiNetworkInfo> taxiNetwork = new ArrayList<>();

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
            while (true){
                if(scanner.nextLine().equals("quit")){
                    break;
                } else if(scanner.nextLine().equals("recharge")){
                    /**
                     * TODO - RECHARGE PROTOCOL
                     */
                }
            }
            try {
                leaveNetwork(restServerModule, rideManagement, networkCommunicationModule);
            } catch (MqttException e) {
                e.printStackTrace();
            }
        });
        stdinThread.start();

    }

    public static void addTaxiToNetwork(TaxiNetworkInfo newTaxi){
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
    }

    public static TaxiNetworkInfo getTaxiNetworkInfo() {
        return taxiNetworkInfo;
    }

    public static void fillTaxiNetwork(List<TaxiNetworkInfo> taxiList){
        taxiNetwork.addAll(taxiList);
    }

    public static AtomicBoolean getEligible() {
        return eligible;
    }

    public static void setEligible(boolean value) {
        Taxi.eligible.set(value);
    }

    private static void leaveNetwork(RESTServerModule restServerModule, RideManagementModule rideManagement, NetworkCommunicationModule networkCommunicationModule) throws MqttException {
        restServerModule.removeTaxiFromNetwork();
        rideManagement.disconnect();
        networkCommunicationModule.disconnect();

        /**
         * TODO - Ending task - Notify other taxis ??
         */
    }

    public static void accomplishRide(RideRequest rideRequest) throws InterruptedException {
        Thread.sleep(5000);
        batteryLvl -= (int)Math.ceil(position.getDistance(rideRequest.getStartPosition()) + rideRequest.getStartPosition().getDistance(rideRequest.getEndPosition()));
        System.out.println("BATTERY: " + batteryLvl);
        position = rideRequest.getEndPosition();
        System.out.println("POSITION: " + position.getX() + "," + position.getY());
        eligible.set(true);
    }
}
