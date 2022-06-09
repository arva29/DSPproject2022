package SETA;

import AdministratorServer.Beans.TaxiNetworkInfo;
import SETA.Data.RideRequest;
import com.google.gson.Gson;
import org.eclipse.paho.client.mqttv3.*;

import java.util.ArrayList;
import java.util.List;

public class RideManagementModule extends Thread{
    private final String BROKER = "tcp://localhost:1883";
    private final String CLIENT_ID = MqttClient.generateClientId();
    private final String topic = "seta/smartcity/rides/district";
    private final int QOS = 2;

    private MqttClient client;
    private int district;
    private final NetworkCommunicationModule networkCommunicationModule;

    public RideManagementModule(int district, NetworkCommunicationModule networkCommunicationModule) throws MqttException {
        this.client = new MqttClient(BROKER, CLIENT_ID);
        this.district = district;
        this.networkCommunicationModule = networkCommunicationModule;
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

                    if (rideRequest.getDistrict() == district) {
                        networkCommunicationModule.startElection(rideRequest, thisModule);
                    }

                }

                public void connectionLost(Throwable cause) {
                    System.err.println(CLIENT_ID + " Connection lost! cause:" + cause.getMessage() + "-  Thread PID: " + Thread.currentThread().getId());
                }

                public void deliveryComplete(IMqttDeliveryToken token) {
                    // Not used here
                }

            });

            System.out.println(CLIENT_ID + " Subscribing ... - Thread PID: " + Thread.currentThread().getId());
            client.subscribe(topic + district, QOS);

        } catch (MqttException me) {
            System.out.println("reason " + me.getReasonCode());
            System.out.println("msg " + me.getMessage());
            System.out.println("loc " + me.getLocalizedMessage());
            System.out.println("cause " + me.getCause());
            System.out.println("excep " + me);
            me.printStackTrace();
        }

    }

    public void disconnect() throws MqttException {
        client.disconnect();
        System.out.println(" -- MQTT CLIENT DISCONNETTED -- ");
    }

    /**
     * Method to accomplish the taxi ride. It updates the taxi position and the battery level according to the ending position of the ride and the distance travelled.
     * @param rideRequest request to accomplish
     * @throws InterruptedException
     * @throws MqttException
     */
    public void accomplishRide(RideRequest rideRequest) throws InterruptedException, MqttException {
        System.out.println("ACCOMPLISH RIDE - " + rideRequest.getId());
        Thread.sleep(5000);

        Taxi.setBatteryLvl(Taxi.getBatteryLvl() - (int)Math.ceil(Taxi.getPosition().getDistance(rideRequest.getStartPosition()) + rideRequest.getStartPosition().getDistance(rideRequest.getEndPosition())));
        System.out.println("BATTERY: " + Taxi.getBatteryLvl());
        Taxi.setPosition(rideRequest.getEndPosition());
        System.out.println("POSITION: " + Taxi.getPosition().getX() + "," + Taxi.getPosition().getY());

        if (Taxi.getBatteryLvl() < 30){
            recharge(false);
        } else {
            Taxi.setAvailableForRide(true);
        }

        if(Taxi.getPosition().getDistrict() != district){
            changeDistrict();
        }
    }

    /**
     * Unsubscribe from previous district topis and subscribe to the new one
     * @throws MqttException
     */
    private void changeDistrict() throws MqttException {
        client.unsubscribe(topic + district);
        district = Taxi.getPosition().getDistrict();
        client.subscribe(topic + district, QOS);
        System.out.println("Subscribe to new topic --> " + topic + district);
    }

    /**
     * Method to starting mutual exclusion algorithm to decide what taxi have the access to the recharging station or if a taxi
     * can access it directly. It also calls the method that execute the actual recharging.
     * @param directAccess true if the taxi is notified directly from the queue of a taxi, in this way it acquires the charging station directly.
     *                     false if the taxi has to communicate with others to decide if the charging station is free
     * @throws InterruptedException
     * @throws MqttException
     */
    public void recharge(boolean directAccess) throws InterruptedException, MqttException {
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
     * @throws InterruptedException
     */
    public void completeRecharging() throws InterruptedException {
        Thread.sleep(10000);
        Taxi.setPosition(Taxi.getPosition().getRechargeStation());
        Taxi.setBatteryLvl(100);
        Taxi.setAvailableForRide(true);
        notifyFreeChargingStation();
    }

    /**
     * Method to notify the first taxi on the recharge queue, if the queue exists, that now the charging station is free. To the taxi is also passed
     * the recharge queue in order to do the same with other taxis in the queue
     * @throws InterruptedException
     */
    private void notifyFreeChargingStation() throws InterruptedException {
        if(Taxi.getRechargeQueue().size() > 0){
            TaxiNetworkInfo nextTaxiToRecharge = Taxi.getRechargeQueue().get(0);
            Taxi.getRechargeQueue().remove(0);
            networkCommunicationModule.notifyPendingTaxi(nextTaxiToRecharge, Taxi.getRechargeQueue());
            Taxi.clearRechargingQueue();
        }
    }
}
