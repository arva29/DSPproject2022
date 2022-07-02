package AdministratorServer.Beans;

import AdministratorServer.Responses.TaxiAddingResponse;
import AdministratorServer.Responses.TaxiListResponse;
import SETA.Data.Position;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

/**
 * Represents the list of the taxi in the network. Instantiated by the server as a singleton
 */
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class TaxiList {
    @XmlElement
    private final HashMap<Integer, TaxiNetworkInfo> listOfTaxis;
    private static TaxiList instance;

    private TaxiList() {
        listOfTaxis = new HashMap<>();
    }

    public synchronized static TaxiList getInstance(){
        if(instance==null) {
            instance = new TaxiList();
        }
        return instance;
    }

    /**
     * Check if the taxi as a unique id and add it to the list, otherwise it returns false and does nothing
     * @param taxi taxi to add
     * @return true if there isn't already a taxi with its id, false otherwise
     */
    public synchronized boolean addTaxi(TaxiNetworkInfo taxi){
        if(listOfTaxis.containsKey(taxi.getId())){
            return false;
        } else {
            listOfTaxis.put(taxi.getId(), taxi);
            return true;
        }
    }

    /**
     * Remove the taxi from the list
     * @param id id of the taxi to remove
     */
    public synchronized void removeTaxi(Integer id){
        listOfTaxis.remove(id);
    }

    /**
     * Return the list of taxi from the map
     * @return list of taxi
     */
    private List<TaxiNetworkInfo> getListFromMap(){
        List<TaxiNetworkInfo> listToReturn = new ArrayList<>();
        for(int key : listOfTaxis.keySet()){
            listToReturn.add(listOfTaxis.get(key));
        }

        return listToReturn;
    }

    /**
     * Return the response of the server to the request of having the list of taxi in the network
     * @return Server response with the list of taxi in the network
     */
    public synchronized TaxiListResponse getListOfTaxis() {
        return new TaxiListResponse(getListFromMap());
    }

    /**
     * Return the response of the server with the initial position of the taxi and the list of the other taxis in the network
     * @param taxiId id of the taxi asking for response
     * @return Server response to the addition of the taxi
     */
    public TaxiAddingResponse responseToAddition(int taxiId){
        List<TaxiNetworkInfo> taxiList = new ArrayList<>();

        for(Integer key: listOfTaxis.keySet()){
            if(key != taxiId){
                taxiList.add(listOfTaxis.get(key));
            }
        }

        return new TaxiAddingResponse(getRandomStationPosition(), taxiList);
    }

    /**
     * Get randomly the position of 1 of the 4 recharging stations
     * @return position of a recharge station
     */
    private Position getRandomStationPosition(){
        int index = new Random().nextInt(3);

        /*switch (index){
            case 0: //NW
                return new Position(0, 0);
            case 1: //NE
                return new Position(0, 9);
            case 2: //SW
                return new Position(9, 0);
            default:
                return new Position(9, 9);
        }*/

        /**
         * todo AAA
         */
        return new Position(0, 0);
    }
}
