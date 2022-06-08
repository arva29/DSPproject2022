package SETA;

import SETA.Data.RideRequest;
import com.google.gson.Gson;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;

public class SetaProcess {

    //ID VA BENE COSI'?
    private static int requestId = 1000; //First ID
    private static final String BROKER = "tcp://localhost:1883";
    private static final String BASE_TOPIC = "seta/smartcity/rides/district";
    private static final String CLIENT_ID = MqttClient.generateClientId();
    private static final int QOS = 2;

    public static void main(String[] args) throws InterruptedException {

        MqttClient client;

        try {
            client = new MqttClient(SetaProcess.BROKER, SetaProcess.CLIENT_ID);
            MqttConnectOptions connOpts = new MqttConnectOptions();
            connOpts.setCleanSession(true);
            //connOpts.setUserName(username); // optional
            //connOpts.setPassword(password.toCharArray()); // optional
            //connOpts.setWill("this/is/a/topic","will message".getBytes(),1,false);  // optional
            //connOpts.setKeepAliveInterval(60);  // optional

            // Connect the client
            System.out.println(SetaProcess.CLIENT_ID + " Connecting Broker " + SetaProcess.BROKER);
            client.connect(connOpts);
            System.out.println(SetaProcess.CLIENT_ID + " Connected");

            /**
             * TODO - REMOVE COMMENT
             */
            //while (true){
                //Thread.sleep(5000);

                SetaProcess.publishRideRequest(client);
                //SetaProcess.publishRideRequest(client);

                Thread.sleep(10000);

                /*if (client.isConnected())
                    client.disconnect();
                System.out.println("Publisher " + SetaProcess.CLIENT_ID + " disconnected");*/
            //}

        } catch (MqttException me) {
            System.out.println("reason " + me.getReasonCode());
            System.out.println("msg " + me.getMessage());
            System.out.println("loc " + me.getLocalizedMessage());
            System.out.println("cause " + me.getCause());
            System.out.println("excep " + me);
            me.printStackTrace();
        }
    }

    private static void publishRideRequest(MqttClient client) throws MqttException {
        RideRequest rideRequest;
        int district;

        rideRequest = new RideRequest(requestId);
        district = rideRequest.getDistrict();
        requestId++;

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
}
