package SETA;

import AdministratorServer.Beans.StatisticsRecord;
import AdministratorServer.Beans.TaxiNetworkInfo;
import AdministratorServer.Responses.TaxiAddingResponse;
import com.google.gson.Gson;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientHandlerException;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;

/**
 * Module to handle communication with the REST server
 */
public class RESTServerModule {
    private final String SERVER_ADDRESS = "http://localhost:1337";
    private final Client client = Client.create();

    /**
     * Send information of this taxi to the server in order to add it to the city. The method also read the server response and
     * set the initial position of the taxi and the list of other taxis already present in the city
     */
    public void addTaxiToNetwork(){
        ClientResponse clientResponse;

        String postPath = "/taxi/add";
        clientResponse = postRequest(client,SERVER_ADDRESS + postPath, new Gson().toJson(Taxi.getTaxiNetworkInfo()));
        TaxiAddingResponse response = clientResponse.getEntity(TaxiAddingResponse.class);

        Taxi.setPosition(response.getPosition());
        if(response.getTaxiList() != null) {
            Taxi.fillTaxiNetwork(response.getTaxiList());
        }
    }

    /**
     * Notify the server to remove the taxi from the server
     */
    public void removeTaxiFromNetwork(){
        ClientResponse clientResponse;

        String removePath = "/taxi/remove";
        clientResponse = deleteRequest(client,SERVER_ADDRESS + removePath, Taxi.getTaxiNetworkInfo());
        clientResponse.close();
    }

    /**
     * Send statistics to the administrator server
     * @param statisticsToSend statistics that have to be sent
     */
    public void sendStatistics(StatisticsRecord statisticsToSend){
        ClientResponse clientResponse;
        String postPath = "/taxi/statistics";

        clientResponse = postRequest(client, SERVER_ADDRESS + postPath, new Gson().toJson(statisticsToSend));

        //System.out.println("Response " + clientResponse.getStatus() + " - " + clientResponse.getStatusInfo());
        clientResponse.close();
    }

    private ClientResponse postRequest(Client client, String url, String input){
        WebResource webResource = client.resource(url);
        //System.out.println("JSON - " + input);

        try {
            return webResource.type("application/json").post(ClientResponse.class, input);
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
