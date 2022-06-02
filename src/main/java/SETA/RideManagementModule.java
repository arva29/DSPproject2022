package SETA;

import org.eclipse.paho.client.mqttv3.*;

import java.sql.Timestamp;
import java.util.Scanner;

public class RideManagementModule extends Thread{
    private final Taxi taxi;

    private final String BROKER = "tcp://localhost:1883";
    private final String CLIENT_ID = MqttClient.generateClientId();
    private final int QOS = 2;

    private String topic = "seta/smartcity/rides/";
    private MqttClient client;
    private String district;

    public RideManagementModule(String district, Taxi taxi) throws MqttException {
        this.taxi = taxi;
        this.client = new MqttClient(BROKER, CLIENT_ID);
        this.district = district;
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

                public void messageArrived(String topic, MqttMessage message) {
                    // Called when a message arrives from the server that matches any subscription made by the client
                    String time = new Timestamp(System.currentTimeMillis()).toString();
                    String receivedMessage = new String(message.getPayload());

                    /**
                     * TODO - ELEZIONE gRPC
                     */

                    /*System.out.println(CLIENT_ID +" Received a Message! - Callback - Thread PID: " + Thread.currentThread().getId() +
                            "\n\tTime:    " + time +
                            "\n\tTopic:   " + topic +
                            "\n\tMessage: " + receivedMessage +
                            "\n\tQoS:     " + message.getQos() + "\n");

                    System.out.println("\n ***  Press a random key to exit *** \n");*/

                }

                public void connectionLost(Throwable cause) {
                    System.out.println(CLIENT_ID + " Connectionlost! cause:" + cause.getMessage()+ "-  Thread PID: " + Thread.currentThread().getId());
                }

                public void deliveryComplete(IMqttDeliveryToken token) {
                    // Not used here
                }

            });
            System.out.println(CLIENT_ID + " Subscribing ... - Thread PID: " + Thread.currentThread().getId());
            client.subscribe(topic + district,QOS);
            System.out.println(CLIENT_ID + " Subscribed to topics : " + topic);


            System.out.println("\n ***  Press a random key to exit *** \n");
            Scanner command = new Scanner(System.in);
            command.nextLine();
            client.disconnect();

        } catch (MqttException me ) {
            System.out.println("reason " + me.getReasonCode());
            System.out.println("msg " + me.getMessage());
            System.out.println("loc " + me.getLocalizedMessage());
            System.out.println("cause " + me.getCause());
            System.out.println("excep " + me);
            me.printStackTrace();
        }

    }

    public void setDistrict(String district) {
        this.district = district;
    }
}
