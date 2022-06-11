package SETA;

import SETA.Data.RideRequest;
import com.google.gson.Gson;
import org.eclipse.paho.client.mqttv3.*;

import java.util.*;

public class SetaProcess {

    private static int requestId = 1000; //First ID
    private static final String BROKER = "tcp://localhost:1883";
    private static final String BASE_TOPIC = "seta/smartcity/rides/district";
    private static final String CLIENT_ID = MqttClient.generateClientId();
    private static final int QOS = 2;

    private static final Map<Integer, List<RideRequest>> pendingRideRequest = new HashMap<>();

    public static void main(String[] args) throws InterruptedException {

        MqttClient client;
        pendingRideRequest.put(1, new ArrayList<>());
        pendingRideRequest.put(2, new ArrayList<>());
        pendingRideRequest.put(3, new ArrayList<>());
        pendingRideRequest.put(4, new ArrayList<>());

        try {
            client = new MqttClient(SetaProcess.BROKER, SetaProcess.CLIENT_ID);
            MqttConnectOptions connOpts = new MqttConnectOptions();
            connOpts.setCleanSession(true);

            // Connect the client
            System.out.println(SetaProcess.CLIENT_ID + " Connecting Broker " + SetaProcess.BROKER);
            client.connect(connOpts);
            System.out.println(SetaProcess.CLIENT_ID + " Connected");

            //Thread to read ride request accomplished
            SubscriberTask subscriberThread = new SubscriberTask(client);
            subscriberThread.start();


            /**
             * TODO - REMOVE COMMENT
             */
            //while (true){

            Scanner scanner = new Scanner(System.in);
            //DEBUG PURPOSE
            while (true) {
                if (scanner.nextLine().equals("r")) {
                    SetaProcess.publishRideRequest(client);
                }
            }
                /*SetaProcess.publishRideRequest(client);
                SetaProcess.publishRideRequest(client);

                Thread.sleep(5000);*/

                //publishPendingRequest(client);

                /*if (client.isConnected())
                    client.disconnect();
                System.out.println("Publisher " + SetaProcess.CLIENT_ID + " disconnected");*/


        } catch (MqttException me) {
            System.out.println("reason " + me.getReasonCode());
            System.out.println("msg " + me.getMessage());
            System.out.println("loc " + me.getLocalizedMessage());
            System.out.println("cause " + me.getCause());
            System.out.println("excep " + me);
            me.printStackTrace();
        }
    }

    private static void publishPendingRequest(MqttClient client) throws MqttException {
        for(int i=1; i<5; i++){
            for(RideRequest request: getRequestOfOneDistrict(i)){
                republishRideRequest(client, request);
            }
        }

    }

    private static void publishRideRequest(MqttClient client) throws MqttException {
        RideRequest rideRequest;
        int district;

        rideRequest = new RideRequest(requestId);
        district = rideRequest.getDistrict();
        requestId++;

        //Add request to list, in order to not lose it if a taxi doesn't take it
        addRequestToMap(district, rideRequest);

        Gson gson = new Gson();
        String jsonString = gson.toJson(rideRequest); //RideRequest to json string

        MqttMessage message = new MqttMessage(jsonString.getBytes());

        // Set the QoS on the Message
        message.setQos(SetaProcess.QOS);
        System.out.println(SetaProcess.CLIENT_ID + " Publishing message: " + jsonString + " ...");
        client.publish(SetaProcess.BASE_TOPIC + district, message);
        System.out.println("--> Topic: DISTRICT " + district);//Adding the number of district based on starting position of request
        System.out.println(SetaProcess.CLIENT_ID + " Message published");
    }

    private static void republishRideRequest(MqttClient client, RideRequest request) throws MqttException {

        Gson gson = new Gson();
        String jsonString = gson.toJson(request); //RideRequest to json string

        MqttMessage message = new MqttMessage(jsonString.getBytes());

        // Set the QoS on the Message
        message.setQos(SetaProcess.QOS);
        System.out.println(SetaProcess.CLIENT_ID + " Republishing message: " + jsonString + " ...");
        client.publish(SetaProcess.BASE_TOPIC + request.getDistrict(), message);
        //System.out.println("--> Topic: DISTRICT " + district);//Adding the number of district based on starting position of request
        System.out.println(SetaProcess.CLIENT_ID + " Message published");
    }

    public static synchronized void addRequestToMap(int district, RideRequest rideRequest){
        pendingRideRequest.get(district).add(rideRequest);
    }

    public static synchronized void removeRequestToMap(int district, RideRequest rideRequest){
        pendingRideRequest.get(district).remove(rideRequest);
    }

    public static synchronized List<RideRequest> getRequestOfOneDistrict(int district){
        return pendingRideRequest.get(district);
    }

    private static class SubscriberTask extends Thread{
        private static final String TOPIC = "seta/smartcity/rides/accomplished";
        private MqttClient client;

        public SubscriberTask(MqttClient client){
            this.client = client;
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

                    public void messageArrived(String topic, MqttMessage message) throws InterruptedException, MqttException {

                        RideRequest rideRequest = new Gson().fromJson(new String(message.getPayload()), RideRequest.class);

                        removeRequestToMap(rideRequest.getDistrict(), rideRequest);

                    }

                    public void connectionLost(Throwable cause) {
                        System.err.println(CLIENT_ID + " Connection lost! cause:" + cause.getMessage() + " - Stack Trace: " + Arrays.toString(cause.getStackTrace()) + " - Thread PID: " + Thread.currentThread().getId());
                    }

                    public void deliveryComplete(IMqttDeliveryToken token) {
                        // Not used here
                    }

                });

                System.out.println(CLIENT_ID + " Subscribing ... - Thread PID: " + Thread.currentThread().getId());
                client.subscribe(TOPIC, QOS);

            } catch (MqttException me) {
                System.out.println("reason " + me.getReasonCode());
                System.out.println("msg " + me.getMessage());
                System.out.println("loc " + me.getLocalizedMessage());
                System.out.println("cause " + me.getCause());
                System.out.println("except " + me);
                me.printStackTrace();
            }
        }
    }
}
