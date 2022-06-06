package SETA;

import AdministratorServer.Beans.TaxiInfo;
import AdministratorServer.Responses.TaxiAddingResponse;
import SETA.Data.Taxi;
import com.google.gson.Gson;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientHandlerException;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;

public class RESTServerModule {
    private final String SERVER_ADDRESS = "http://localhost:1337";

    public Taxi addTaxiToNetwork(TaxiInfo taxiInfo){
        Client client = Client.create();
        ClientResponse clientResponse;

        String postPath = "/taxi/add";
        clientResponse = postRequest(client,SERVER_ADDRESS + postPath, taxiInfo);
        TaxiAddingResponse response = clientResponse.getEntity(TaxiAddingResponse.class);

        return new Taxi(taxiInfo,
                response.getPosition(),
                response.getTaxiList()
        );
    }

    private ClientResponse postRequest(Client client, String url, TaxiInfo t){
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

    private ClientResponse getRequest(Client client, String url){
        WebResource webResource = client.resource(url);
        try {
            return webResource.type("application/json").get(ClientResponse.class);
        } catch (ClientHandlerException e) {
            System.out.println("Server non disponibile");
            return null;
        }
    }
}
