package AdministratorServer;

import AdministratorServer.Beans.Statistics;
import AdministratorServer.Beans.TaxiNetworkInfo;
import AdministratorServer.Responses.AverageStatisticsResponse;
import AdministratorServer.Responses.LastStatisticsByIdResponse;
import AdministratorServer.Responses.TaxiListResponse;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientHandlerException;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;

import javax.xml.bind.UnmarshalException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * Class to manage the standard input of the administrator client and to provide all the commands for retrieving information
 * from the server
 */
public class AdministratorClient {

    private static final String SERVER_ADDRESS = "http://localhost:1337";
    private static final Client client = Client.create();

    public static void main(String[] args) {

        defaultMessage();

        Scanner scanner = new Scanner(System.in);
        String line;

        while (true){
            line = scanner.nextLine();

            checkCommand(line);
        }

    }

    /**
     * Check what command was inserted
     * @param line standard input line
     */
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
                if(commandArray.length == 3){
                    String s1 = commandArray[1];
                    String s2 = commandArray[2];
                    // TEST -> timestampStats 2022-06-29_18:09:23 2022-06-30_18:25:00
                    showTimestampStats(s1, s2);
                } else {
                    defaultMessage();
                }
                break;
            default:
                defaultMessage();
                break;
        }
    }

    /**
     * Print the list of the taxis in the network
     */
    private static void showListOfTaxi() {
        String getPath = "/statistics/listOfTaxis";
        ClientResponse clientResponse = getRequest(client,SERVER_ADDRESS+getPath);

        if (clientResponse.getStatus() == 200) {
            System.out.println(clientResponse);
        } else {
            System.out.println("ERROR with code " + clientResponse.getStatus());
        }

        TaxiListResponse list = clientResponse.getEntity(TaxiListResponse.class);
        System.out.println("\nTAXI LIST");
        try {
            for (TaxiNetworkInfo i : list.getTaxiList()) {
                System.out.println(" - ID: " + i.getId() + "  [IP:" + i.getIpAddress() + " - PORT: " + i.getPortNumber() + "]");
            }
        } catch (NullPointerException e){
            System.out.println("No taxis in the network");
        }
    }

    /**
     * Print the avarage of the statistics of all the taxis in the period between the two timestamp
     * @param t1 first timestamp
     * @param t2 last timestamp
     */
    private static void showTimestampStats(String t1, String t2) {
        String getPath = "/statistics/" + t1 + "&" + t2;
        ClientResponse clientResponse = getRequest(client,SERVER_ADDRESS+getPath);

        if (clientResponse.getStatus() == 200) {
            System.out.println(clientResponse);

            AverageStatisticsResponse avgStats = clientResponse.getEntity(AverageStatisticsResponse.class);

            if(avgStats.getBatteryLvl() != -1){
                System.out.println("\nAVERAGE STATS BETWEEN " + t1 + " AND " + t2);
                avgStats.print();
            } else {
                System.out.println("\nTHERE ARE NO STATISTICS AVAILABLE");
            }

        } else if (clientResponse.getStatus() == 400){
            defaultMessage();
        } else {
            System.out.println("ERROR with code " + clientResponse.getStatus());
        }
    }

    /**
     * Print the last n statistics of the taxi specified
     * @param id id of the taxi
     * @param n number of statistics to retrieve
     */
    private static void showTaxiStats(int id, int n) {
        String getPath = "/statistics/" + id + "/" + n;
        ClientResponse clientResponse = getRequest(client,SERVER_ADDRESS+getPath);

        if (clientResponse.getStatus() == 200) {
            System.out.println(clientResponse);

            LastStatisticsByIdResponse lastStats = clientResponse.getEntity(LastStatisticsByIdResponse.class);

            if(lastStats.getId() != -1) {
                System.out.println("\nAVERAGE OF LAST " + n + " STATISTICS OF TAXI " + id);
                lastStats.getStatistics().print();
            } else {
                System.out.println("\nNO TAXI WITH THE SPECIFIED ID");
            }

        } else {
            System.out.println("ERROR with code " + clientResponse.getStatus());
        }
    }

    /**
     * Print a default message with all the commands available
     */
    private static void defaultMessage(){
        System.out.println("\nThis is the list of available commands:");
        System.out.println(" - list: Display the list of taxi in the network");
        System.out.println(" - taxiStat {id} {n}: Display last n local statistics of taxi with the id specified");
        System.out.println(" - timestampStats {t1} {t2}: Display the average of statistics from all the taxi between the two specified timestamp (format: yyyy-[m]m-[d]d_hh:mm:ss)");
    }

    /**
     * Execute the GET request to the server
     * @param client client that has to do the request
     * @param url url of the desired request
     * @return response to the request performed
     */
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
