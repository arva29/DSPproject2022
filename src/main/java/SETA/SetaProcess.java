package SETA;

import SETA.Data.RideRequest;
import com.google.gson.Gson;
import org.eclipse.paho.client.mqttv3.*;

import java.util.*;

/**
 * The SETA process that has the job to publish the ride request to the mqtt broker
 */
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
            SubscriberTask subscriberThread = new SubscriberTask();
            subscriberThread.start();

            //Thread to republish request that are not taken in charge
            PendingRequestTask pendingRequestTask = new PendingRequestTask(client);
            pendingRequestTask.start();

            //debugCommandScanner(client);

            while (true) {

                SetaProcess.publishRideRequest(client);
                SetaProcess.publishRideRequest(client);

                Thread.sleep(5000);
            }

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

    /**
     * Publishes the ride request on the correct topic based on the district of the request
     * @param client mqtt client
     */
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

    /**
     * Republish a request that has not been taken care of
     * @param client mqtt client
     * @param request request to republish
     */
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

    /**
     * Adds the request to the map with all the pending requests
     * @param district district of the request
     * @param rideRequest request to add
     */
    public static synchronized void addRequestToMap(int district, RideRequest rideRequest){
        pendingRideRequest.get(district).add(rideRequest);
    }

    /**
     * Removes the request from the map with all the pending requests
     * @param district district of the request
     * @param rideRequest request to remove
     */
    public static synchronized void removeRequestToMap(int district, RideRequest rideRequest){

        for(int i=0; i<pendingRideRequest.get(district).size(); i++){
            if(pendingRideRequest.get(district).get(i).getId() == rideRequest.getId()){
                pendingRideRequest.get(district).remove(i);
            }
        }

    }

    /**
     * Thread that works as subscriber to the topic "seta/smartcity/rides/accomplished" where taxis notify their completed
     * rides
     */
    private static class SubscriberTask extends Thread{
        private static final String TOPIC = "seta/smartcity/rides/accomplished";

        @Override
        public void run() {
            try {
                MqttClient client = new MqttClient(SetaProcess.BROKER, MqttClient.generateClientId());
                MqttConnectOptions connOpts = new MqttConnectOptions();
                connOpts.setCleanSession(true);

                // Connect the client
                System.out.println(client + " Connecting Broker " + BROKER);
                client.connect(connOpts);
                System.out.println(client + " Connected - Thread PID: " + Thread.currentThread().getId());

                // Callback
                client.setCallback(new MqttCallback() {

                    public void messageArrived(String topic, MqttMessage message) throws InterruptedException, MqttException {

                        RideRequest rideRequest = new Gson().fromJson(new String(message.getPayload()), RideRequest.class);
                        removeRequestToMap(rideRequest.getDistrict(), rideRequest);

                    }

                    public void connectionLost(Throwable cause) {
                        System.err.println(client + " Connection lost! cause:" + cause.getMessage() + " - Stack Trace: " + Arrays.toString(cause.getStackTrace()) + " - Thread PID: " + Thread.currentThread().getId());
                    }

                    public void deliveryComplete(IMqttDeliveryToken token) {
                        // Not used here
                    }

                });

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

    /**
     * Thread that works as subscriber to the topic "seta/smartcity/rides/districtChanges" where taxis notify when they change district.
     * In this way the SETA process can republish all the ride that are pending in that district
     */
    private static class PendingRequestTask extends Thread{
        private static final String TOPIC = "seta/smartcity/rides/districtChanges";
        private static MqttClient publisherClient;

        public PendingRequestTask(MqttClient client) {
            publisherClient = client;
        }

        @Override
        public void run() {
            try {
                MqttClient client = new MqttClient(SetaProcess.BROKER, MqttClient.generateClientId());
                MqttConnectOptions connOpts = new MqttConnectOptions();
                connOpts.setCleanSession(true);

                // Connect the client
                System.out.println(client + " Connecting Broker " + BROKER);
                client.connect(connOpts);
                System.out.println(client + " Connected - Thread PID: " + Thread.currentThread().getId());

                // Callback
                client.setCallback(new MqttCallback() {

                    public void messageArrived(String topic, MqttMessage message) throws InterruptedException, MqttException {

                        Integer district = new Gson().fromJson(new String(message.getPayload()), Integer.class);

                        for(RideRequest r: pendingRideRequest.get(district)){
                            republishRideRequest(publisherClient, r);
                        }

                    }

                    public void connectionLost(Throwable cause) {
                        System.err.println(client + " Connection lost! cause:" + cause.getMessage() + " - Stack Trace: " + Arrays.toString(cause.getStackTrace()) + " - Thread PID: " + Thread.currentThread().getId());
                    }

                    public void deliveryComplete(IMqttDeliveryToken token) {
                        // Not used here
                    }

                });

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

    private static void printMap(){
        System.out.println(" - PENDING REQUESTS - ");
        for(Integer i: pendingRideRequest.keySet()){
            System.out.println(" DISTRICT " + i);
            for (RideRequest r: pendingRideRequest.get(i)){
                System.out.println(" - " + r.getId());
            }
        }
    }

    /**
     * Only for debugging purpose. With "r" you can publish a ride and with "l" you can see all pending rides divided by district
     */
    private static void debugCommandScanner(MqttClient client) throws MqttException {
        Scanner scanner = new Scanner(System.in);
        String a;
        //DEBUG PURPOSE
        while (true) {
            a = scanner.nextLine();
            if (a.equals("r")) {
                SetaProcess.publishRideRequest(client);
            } else if(a.equals("l")){
                printMap();
            }
        }
    }
}
