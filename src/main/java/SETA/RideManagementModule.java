package SETA;

import AdministratorServer.Beans.TaxiNetworkInfo;
import SETA.Data.RideRequest;
import com.google.gson.Gson;
import org.eclipse.paho.client.mqttv3.*;

import java.util.Arrays;

/**
 * Class to manage the mqtt communication with broker. It runs a thread that always listen to the mqtt topic "seta/smartcity/rides/district{i}" (i = number of district) and
 * elaborates every message by starting an election or not
 */
public class RideManagementModule extends Thread{
    private final String BROKER = "tcp://localhost:1883";
    private final String CLIENT_ID = MqttClient.generateClientId();
    private final String topic = "seta/smartcity/rides/district";
    private final int QOS = 2;

    private MqttClient client;
    private int district;
    private final NetworkCommunicationModule networkCommunicationModule;
    private final StatisticsModule statisticsModule;

    public RideManagementModule(int district, NetworkCommunicationModule networkCommunicationModule, StatisticsModule statisticsModule) throws MqttException {
        this.client = new MqttClient(BROKER, CLIENT_ID);
        this.district = district;
        this.networkCommunicationModule = networkCommunicationModule;
        this.statisticsModule = statisticsModule;
        networkCommunicationModule.setRideManagementModule(this);
    }

    @Override
    public void run() {
        try {
            RideManagementModule thisModule = this;
            client = new MqttClient(BROKER, CLIENT_ID);
            MqttConnectOptions connOpts = new MqttConnectOptions();
            connOpts.setCleanSession(true);

            // Connect the client
            System.out.println(CLIENT_ID + " Connecting Broker " + BROKER);
            client.connect(connOpts);
            System.out.println(CLIENT_ID + " Connected - Thread PID: " + Thread.currentThread().getId());

            // Callback
            client.setCallback(new MqttCallback() {

                public void messageArrived(String topic, MqttMessage message) throws InterruptedException, MqttException {

                    RideRequest rideRequest = new Gson().fromJson(new String(message.getPayload()), RideRequest.class);

                    //System.out.println("Message arrived (" + rideRequest.getId() + ")! - FREE: " + Taxi.isFree() + " - ALREADY ANSWERED - " + !Taxi.rideAlreadyAccomplished(rideRequest.getId()));

                    if (rideRequest.getDistrict() == district) {
                        if(Taxi.isFree() && !Taxi.isAskingForRecharging() && !Taxi.rideAlreadyAccomplished(rideRequest.getId())) { //If not free Taxi doesn't take into consideration the request

                            networkCommunicationModule.startElection(rideRequest, thisModule);

                        }
                    }
                }

                public void connectionLost(Throwable cause) {
                    System.err.println(CLIENT_ID + " Connection lost! cause:" + cause.getMessage() + " - Stack Trace: " + Arrays.toString(cause.getStackTrace()) + " - Thread PID: " + Thread.currentThread().getId());
                }

                public void deliveryComplete(IMqttDeliveryToken token) {
                    // Not used here
                }

            });

            System.out.println(CLIENT_ID + " Subscribing ... - Thread PID: " + Thread.currentThread().getId());
            client.subscribe(topic + district, QOS);

            notifyDistrictChange(Taxi.getPosition().getDistrict());

        } catch (MqttException me) {
            System.out.println("reason " + me.getReasonCode());
            System.out.println("msg " + me.getMessage());
            System.out.println("loc " + me.getLocalizedMessage());
            System.out.println("cause " + me.getCause());
            System.out.println("excep " + me);
            me.printStackTrace();
        }

    }

    /**
     * Disconnect the client from mqtt broker
     */
    public void disconnect() throws MqttException {
        if(client.isConnected()) {
            client.disconnect();
        }
        System.out.println(" -- MQTT CLIENT DISCONNETTED -- ");
    }

    /**
     * Method to accomplish the taxi ride. It updates the taxi position and the battery level according to the ending position of the ride and the distance travelled.
     * @param rideRequest request to accomplish
     */
    public void accomplishRide(RideRequest rideRequest) throws InterruptedException, MqttException {
        int kmTravelled = (int)Math.ceil(Taxi.getPosition().getDistance(rideRequest.getStartPosition()) + rideRequest.getStartPosition().getDistance(rideRequest.getEndPosition()));
        System.out.println("\n ... on the road!\n");
        Thread.sleep(5000);
        System.out.println("ACCOMPLISHED RIDE - " + rideRequest.getId());
        Taxi.setBatteryLvl(Taxi.getBatteryLvl() - kmTravelled);
        System.out.println("BATTERY: " + Taxi.getBatteryLvl());
        Taxi.setPosition(rideRequest.getEndPosition());
        System.out.println("POSITION: " + Taxi.getPosition().getX() + "," + Taxi.getPosition().getY());

        if (Taxi.getBatteryLvl() < 30){
            recharge(false);
        } else {
            Taxi.setFree(true);
            Taxi.setEligible(true);
            System.out.println();
        }

        Taxi.addToAccomplishedRide(rideRequest.getId());
        notifyAccomplishedRide(rideRequest);

        if(Taxi.getPosition().getDistrict() != district){
            changeDistrict();
        }

        statisticsModule.addRide();
        statisticsModule.addKmTravelled(kmTravelled);
    }

    /**
     * Unsubscribe from previous district topis and subscribe to the new one
     */
    private void changeDistrict() throws MqttException {
        client.unsubscribe(topic + district);
        district = Taxi.getPosition().getDistrict();
        client.subscribe(topic + district, QOS);
        notifyDistrictChange(district);
        System.out.println("Subscribe to new topic --> " + topic + district);
    }

    /**
     * Method to starting mutual exclusion algorithm to decide what taxi have the access to the recharging station or if a taxi
     * can access it directly. It also calls the method that execute the actual recharging.
     * @param directAccess true if the taxi is notified directly from the queue of a taxi, in this way it acquires the charging station directly.
     *                     false if the taxi has to communicate with others to decide if the charging station is free
     */
    public void recharge(boolean directAccess) throws InterruptedException, MqttException {
        if(Taxi.isFree()) Taxi.setFree(false);
        if(!directAccess){
            if(networkCommunicationModule.askingForRecharge()){
                completeRecharging();
            }
        } else {
            completeRecharging();
        }
    }

    /**
     * Complete the charging process with a 10 seconds timeout and set the position of the taxi to the one of the charging station of its district.
     */
    public void completeRecharging() throws InterruptedException {
        Taxi.setAskingForRecharging(false);
        Taxi.setRecharging(true);
        if(Taxi.isFree()){Taxi.setFree(false);}
        Taxi.setBatteryLvl(Taxi.getBatteryLvl() - (int)Math.ceil(Taxi.getPosition().getDistance(Taxi.getPosition().getRechargeStation())));
        Taxi.setPosition(Taxi.getPosition().getRechargeStation());
        System.out.println(" ...Starting recharging ");
        Thread.sleep(10000);
        Taxi.setBatteryLvl(100);
        Taxi.setFree(true);
        Taxi.setEligible(true);
        Taxi.setRecharging(false);
        System.out.println(" - RICARICA EFFETTUATA - ");
        notifyFreeChargingStation();
    }

    /**
     * Method to notify the first taxi on the recharge queue, if the queue exists, that now the charging station is free. To the taxi is also passed
     * the recharge queue in order to do the same with other taxis in the queue
     */
    private void notifyFreeChargingStation() throws InterruptedException {

        System.err.println("Recharge Queue BEFORE");
        for(TaxiNetworkInfo i: Taxi.getRechargeQueue()){
            System.err.println("ID - " + i.getId());
        }

        if(Taxi.getRechargeQueue().size() > 0){
            TaxiNetworkInfo nextTaxiToRecharge = Taxi.getRechargeQueue().get(0);
            Taxi.getRechargeQueue().remove(0);

            System.err.println("\nRecharge Queue AFTER");
            for(TaxiNetworkInfo i: Taxi.getRechargeQueue()){
                System.err.println("ID - " + i.getId());
            }

            networkCommunicationModule.notifyPendingTaxi(nextTaxiToRecharge);
            Taxi.clearRechargingQueue();
        }
    }

    /**
     * Method to notify SETA process the ride request that the taxi has accomplished
     * @param rideRequest ride request just accomplished
     */
    private void notifyAccomplishedRide(RideRequest rideRequest) throws MqttException {
        final String notifyTopic = "seta/smartcity/rides/accomplished";

        Gson gson = new Gson();
        String jsonString = gson.toJson(rideRequest); //RideRequest to json string

        MqttMessage message = new MqttMessage(jsonString.getBytes());

        // Set the QoS on the Message
        message.setQos(QOS);
        //System.out.println(CLIENT_ID + " Publishing message: " + jsonString + " ...");
        client.publish(notifyTopic, message);
        //System.out.println(CLIENT_ID + " Message published");
    }


    /**
     * Method to publish to the topic "seta/smartcity/rides/districtChanges" the district entered by the taxi in order to
     * eventually receive ride request not accomplished in that district
     * @param district district that has just been entered by the taxi
     */
    private void notifyDistrictChange(int district) throws MqttException {
        final String notifyTopic = "seta/smartcity/rides/districtChanges";

        Gson gson = new Gson();
        String jsonString = gson.toJson(district); //RideRequest to json string

        MqttMessage message = new MqttMessage(jsonString.getBytes());

        // Set the QoS on the Message
        message.setQos(QOS);
        //System.out.println(CLIENT_ID + " Publishing message: " + jsonString + " ...");
        client.publish(notifyTopic, message);
        //System.out.println(CLIENT_ID + " Message published");
    }

}
