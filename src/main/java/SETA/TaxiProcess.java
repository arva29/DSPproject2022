package SETA;

import AdministratorServer.Beans.TaxiInfo;
import AdministratorServer.Responses.TaxiAddingResponse;
import AdministratorServer.Responses.TaxiListResponse;
import SETA.Data.Position;
import SETA.Data.Taxi;
import com.google.gson.Gson;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientHandlerException;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import org.eclipse.paho.client.mqttv3.MqttException;

import java.util.List;
import java.util.Random;

public class TaxiProcess {
    private static final int ID = new Random().nextInt(1000);
    private static final String IP_ADDRESS = "127.0.0.1";
    private static final int PORT = ID;


    public static void main(String[] args) throws InterruptedException {

        /**
         * TODO: Da rivedere inizializz. id e porta
         */

        TaxiInfo taxiInfo = new TaxiInfo(ID, IP_ADDRESS, PORT);

        RESTServerModule restServerModule = new RESTServerModule();
        RideManagementModule rideManagement;

        // ADDING TAXI TO THE NETWORK
        Taxi taxi = restServerModule.addTaxiToNetwork(taxiInfo);

        NetworkCommunicationModule networkCommunicationModule = new NetworkCommunicationModule(PORT, taxi);
        networkCommunicationModule.notifyPresenceToNetwork(taxi.getTaxiNetwork(), taxiInfo);

        try {
            rideManagement = new RideManagementModule(String.valueOf(taxi.getPosition().getDistrict()), taxi);
            System.out.println("--- TAXI\n - pos: " + rideManagement.getTaxi().getPosition().getX() + "," + rideManagement.getTaxi().getPosition().getY() + "\n - id: " + rideManagement.getTaxi().getTaxiInfo().getId());
            rideManagement.start();
        } catch (MqttException e) {
            e.printStackTrace();
        }

        /*
        //GET REQUEST #1
        String getPath = "/statistics/listoftaxis";
        clientResponse = getRequest(client,serverAddress+getPath);
        System.out.println(clientResponse.toString());
        TaxiList taxiList = clientResponse.getEntity(TaxiList.class);
        System.out.println("---- Taxis:");
        for (Integer i : taxiList.getListOfTaxis().keySet()){
            System.out.println("ID: " + i);
        }
        */

    }



}
