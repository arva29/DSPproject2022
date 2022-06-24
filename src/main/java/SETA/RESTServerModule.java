package SETA;

import AdministratorServer.Beans.StatisticsRecord;
import AdministratorServer.Beans.TaxiNetworkInfo;
import AdministratorServer.Responses.TaxiAddingResponse;
import com.google.gson.Gson;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientHandlerException;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;

public class RESTServerModule {
    private final String SERVER_ADDRESS = "http://localhost:1337";
    private final Client client = Client.create();

    public void addTaxiToNetwork(){
        ClientResponse clientResponse;

        String postPath = "/taxi/add";
        clientResponse = postRequest(client,SERVER_ADDRESS + postPath, Taxi.getTaxiNetworkInfo());
        TaxiAddingResponse response = clientResponse.getEntity(TaxiAddingResponse.class);

        Taxi.setPosition(response.getPosition());
        if(response.getTaxiList() != null) {
            Taxi.fillTaxiNetwork(response.getTaxiList());
        }
    }

    public void removeTaxiFromNetwork(){
        ClientResponse clientResponse;

        String removePath = "/taxi/remove";
        clientResponse = deleteRequest(client,SERVER_ADDRESS + removePath, Taxi.getTaxiNetworkInfo());
        clientResponse.close();
    }

    public void sendStatistics(StatisticsRecord statisticsToSend){
        ClientResponse clientResponse;

        String postPath = "/taxi/sendStatistics";
        clientResponse = postRequest(client,SERVER_ADDRESS + postPath, Taxi.getTaxiNetworkInfo());
        clientResponse.close();
    }

    private ClientResponse postRequest(Client client, String url, TaxiNetworkInfo t){
        WebResource webResource = client.resource(url);
        String input = new Gson().toJson(t);
        //System.out.println("-----> " + input);
        try {
            return webResource.type("application/json").post(ClientResponse.class, input);
        } catch (ClientHandlerException e) {
            System.out.println("Server non disponibile");
            return null;
        }
    }

    private ClientResponse getRequest(Client client, String url){
        WebResource webResource = client.resource(url);
        try {
            return webResource.type("application/json").get(ClientResponse.class);
        } catch (ClientHandlerException e) {
            System.out.println("Server non disponibile");
            return null;
        }
    }

    private ClientResponse deleteRequest(Client client, String url, TaxiNetworkInfo t){
        WebResource webResource = client.resource(url);
        String input = new Gson().toJson(t);

        try {
            return webResource.type("application/json").delete(ClientResponse.class, input);
        } catch (ClientHandlerException e) {
            System.out.println("Server non disponibile");
            return null;
        }
    }
}
