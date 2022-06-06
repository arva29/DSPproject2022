package AdministratorServer;

import AdministratorServer.Beans.TaxiInfo;
import AdministratorServer.Responses.TaxiListResponse;
import com.google.gson.Gson;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientHandlerException;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;

public class ClientProva {

    public static void main(String[] args){
        Client client = Client.create();
        String serverAddress = "http://localhost:1337";
        ClientResponse clientResponse = null;

        // POST EXAMPLE
        String postPath = "/taxi/add";
        TaxiInfo taxi = new TaxiInfo(1001,"127.0.0.1",888);
        clientResponse = postRequest(client,serverAddress+postPath,taxi);
        System.out.println(clientResponse.toString());

        //GET REQUEST #1
        String getPath = "/statistics/listoftaxis";
        clientResponse = getRequest(client,serverAddress+getPath);
        System.out.println(clientResponse.toString());
        TaxiListResponse taxiList = clientResponse.getEntity(TaxiListResponse.class);
        System.out.println("---- Taxis:");
        for (TaxiInfo i : taxiList.getTaxiList()){
            System.out.println("ID: " + i.getId());
        }

    }

    public static ClientResponse postRequest(Client client, String url, TaxiInfo t){
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

    public static ClientResponse getRequest(Client client, String url){
        WebResource webResource = client.resource(url);
        try {
            return webResource.type("application/json").get(ClientResponse.class);
        } catch (ClientHandlerException e) {
            System.out.println("Server non disponibile");
            return null;
        }
    }
}