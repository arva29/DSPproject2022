package AdministratorServer.services;

import AdministratorServer.Beans.Statistics;
import AdministratorServer.Beans.TaxiNetworkInfo;
import AdministratorServer.Responses.AverageStatisticsResponse;
import AdministratorServer.Responses.LastStatisticsByIdResponse;
import AdministratorServer.Responses.TaxiListResponse;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientHandlerException;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Scanner;

public class AdministratorClient {

    private static final String SERVER_ADDRESS = "http://localhost:1337";
    private static final Client client = Client.create();

    public static void main(String[] args) {

        Scanner scanner = new Scanner(System.in);
        String line;

        while (true){
            line = scanner.nextLine();

            checkCommand(line);
        }

    }

    private static void checkCommand(String line) {

        String[] commandArray = line.split(" ");

        switch (commandArray[0]){
            case "list":
                showListOfTaxi();
                break;
            case "taxiStat":
                try {
                    showTaxiStats(Integer.parseInt(commandArray[1]), Integer.parseInt(commandArray[2]));
                } catch (NumberFormatException e){
                    e.printStackTrace();
                    defaultMessage();
                }
                break;
            case "timestampStats":
                try {
                    Timestamp t1 = Timestamp.valueOf(commandArray[1]);
                    Timestamp t2 = Timestamp.valueOf(commandArray[2]);
                    showTimestampStats(t1, t2);
                } catch (NumberFormatException e){
                    e.printStackTrace();
                    defaultMessage();
                }
                break;
            default:
                defaultMessage();
                break;
        }
    }

    private static void showListOfTaxi() {
        String getPath = "/statistics/listOfTaxis";
        ClientResponse clientResponse = getRequest(client,SERVER_ADDRESS+getPath);
        System.out.println(clientResponse.toString());

        TaxiListResponse list = clientResponse.getEntity(TaxiListResponse.class);
        System.out.println("\nTAXI LIST");
        for (TaxiNetworkInfo i : list.getTaxiList()){
            System.out.println(" - ID: " + i.getId() + "  [IP:" + i.getIpAddress() + " - PORT: " + i.getPortNumber() + "]");
        }
    }

    private static void showTimestampStats(Timestamp t1, Timestamp t2) {
        String getPath = "/statistics/" + t1 + "-" + t2;
        ClientResponse clientResponse = getRequest(client,SERVER_ADDRESS+getPath);
        System.out.println(clientResponse.toString());

        AverageStatisticsResponse avgStats = clientResponse.getEntity(AverageStatisticsResponse.class);
        System.out.println("\nAVERAGE STATS BETWEEN " + t1 + " AND " + t2);
        avgStats.print();

    }

    private static void showTaxiStats(int id, int n) {
        String getPath = "/statistics/" + id + "/" + n;
        ClientResponse clientResponse = getRequest(client,SERVER_ADDRESS+getPath);
        System.out.println(clientResponse.toString());

        LastStatisticsByIdResponse lastStats = clientResponse.getEntity(LastStatisticsByIdResponse.class);
        System.out.println("\nAVERAGE OF LAST " + n + " STATISTICS OF TAXI " + id);
        lastStats.getStatistics().print();

    }

    private static void defaultMessage(){
        System.out.println("UNKNOWN COMMAND! This is the list of available commands:");
        System.out.println(" - list: Display the list of taxi in the network");
        System.out.println(" - taxiStat {id} {n}: Display last n local statistics of taxi with the id specified");
        System.out.println(" - timestampStats {t1} {t2}: Display the average of statistics from all the taxi between the two specified timestamp (format: yyyy-[m]m-[d]d hh:mm:ss)");
    }

    private static ClientResponse getRequest(Client client, String url){
        WebResource webResource = client.resource(url);
        try {
            return webResource.type("application/json").get(ClientResponse.class);
        } catch (ClientHandlerException e) {
            System.out.println("Server non disponibile");
            return null;
        }
    }
}
