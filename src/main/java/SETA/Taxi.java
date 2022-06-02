package SETA;

import AdministratorServer.Beans.TaxiBean;
import AdministratorServer.Responses.TaxiAddingResponse;
import AdministratorServer.Responses.TaxiListResponse;
import SETA.Data.Position;
import com.google.gson.Gson;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientHandlerException;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import org.eclipse.paho.client.mqttv3.MqttException;

import java.util.List;
import java.util.Random;

public class Taxi extends Thread{

    /**
     * TODO: Da rivedere inizializz. id e porta
     */

    private final int ID = new Random().nextInt(1000);
    private final String IP_ADDRESS = "127.0.0.1";
    private final int PORT_NUMBER = ID;

    private List<TaxiBean> taxiList;
    private Position position;

    public void run(){

        Client client = Client.create();
        String serverAddress = "http://localhost:1337";
        ClientResponse clientResponse;
        RideManagementModule rideManagement;

        // POST EXAMPLE
        String postPath = "/taxi/add";
        TaxiBean taxi = new TaxiBean(ID,IP_ADDRESS,PORT_NUMBER);
        clientResponse = postRequest(client,serverAddress+postPath,taxi);
        //System.out.println(clientResponse.toString());
        taxiList = clientResponse.getEntity(TaxiListResponse.class).getTaxiList();


        if (clientResponse != null) {
            position = clientResponse.getEntity(TaxiAddingResponse.class).getPosition();
            taxiList = clientResponse.getEntity(TaxiAddingResponse.class).getTaxiList();
        } else {
            System.err.println("NO RESPONSE FROM SERVER");
        }

        try {
            rideManagement = new RideManagementModule(String.valueOf(position.getDistrict()), this);
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

    public ClientResponse postRequest(Client client, String url, TaxiBean t){
        WebResource webResource = client.resource(url);
        String input = new Gson().toJson(t);
        System.out.println("-----> " + input);
        try {
            return webResource.type("application/json").post(ClientResponse.class, input);
        } catch (ClientHandlerException e) {
            System.out.println("Server non disponibile");
            return null;
        }
    }

    public ClientResponse getRequest(Client client, String url){
        WebResource webResource = client.resource(url);
        try {
            return webResource.type("application/json").get(ClientResponse.class);
        } catch (ClientHandlerException e) {
            System.out.println("Server non disponibile");
            return null;
        }
    }

    private void notifyAllOtherTaxis(){

    }
}
