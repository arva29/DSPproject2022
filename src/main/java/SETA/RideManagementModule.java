package SETA;

import SETA.Data.RideRequest;
import com.google.gson.Gson;
import com.sun.jersey.api.client.Client;
import org.eclipse.paho.client.mqttv3.*;

import java.util.Scanner;

public class RideManagementModule extends Thread{
    private final String BROKER = "tcp://localhost:1883";
    private final String CLIENT_ID = MqttClient.generateClientId();
    private final int QOS = 2;

    private String topic = "seta/smartcity/rides/district";
    private MqttClient client;
    private int district;
    private NetworkCommunicationModule networkCommunicationModule;

    public RideManagementModule(int district, NetworkCommunicationModule networkCommunicationModule) throws MqttException {
        this.client = new MqttClient(BROKER, CLIENT_ID);
        this.district = district;
        this.networkCommunicationModule = networkCommunicationModule;
    }

    @Override
    public void run() {
        try {
            client = new MqttClient(BROKER, CLIENT_ID);
            MqttConnectOptions connOpts = new MqttConnectOptions();
            connOpts.setCleanSession(true);



            // Connect the client
            System.out.println(CLIENT_ID + " Connecting Broker " + BROKER);
            client.connect(connOpts);
            System.out.println(CLIENT_ID + " Connected - Thread PID: " + Thread.currentThread().getId());

            // Callback
            client.setCallback(new MqttCallback() {

                public void messageArrived(String topic, MqttMessage message) throws InterruptedException {

                    RideRequest rideRequest = new Gson().fromJson(new String(message.getPayload()), RideRequest.class);

                    if (rideRequest.getDistrict() == district) {
                        networkCommunicationModule.startElection(rideRequest);
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

    public void setDistrict(int district) {
        this.district = district;
    }

    public void disconnect() throws MqttException {
        client.disconnect();
        System.out.println(" -- MQTT CLIENT DISCONNETTED -- ");
    }

}
