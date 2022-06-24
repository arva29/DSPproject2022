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

    public synchronized boolean addTaxi(TaxiNetworkInfo taxi){
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        if(listOfTaxis.containsKey(taxi.getId())){
            return false;
        } else {
            listOfTaxis.put(taxi.getId(), taxi);
            return true;
        }
    }

    public synchronized void removeTaxi(Integer id){
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        listOfTaxis.remove(id);
    }

    private List<TaxiNetworkInfo> getListFromMap(){
        List<TaxiNetworkInfo> listToReturn = new ArrayList<>();
        for(int key : listOfTaxis.keySet()){
            listToReturn.add(listOfTaxis.get(key));
        }

        return listToReturn;
    }

    public synchronized TaxiListResponse getListOfTaxis() {
        return new TaxiListResponse(getListFromMap());
    }

    public TaxiAddingResponse responseToAddition(int taxiId){
        List<TaxiNetworkInfo> taxiList = new ArrayList<>();

        for(Integer key: listOfTaxis.keySet()){
            if(key != taxiId){
                taxiList.add(listOfTaxis.get(key));
            }
        }

        return new TaxiAddingResponse(getRandomStationPosition(), taxiList);
    }

    private Position getRandomStationPosition(){
        int index = new Random().nextInt(3);

        switch (index){
            case 0: //NW
                return new Position(0, 0);
            case 1: //NE
                return new Position(0, 9);
            case 2: //SW
                return new Position(9, 0);
            default:
                return new Position(9, 9);
        }

        /**
         * todo REMOVE
         */
        //return new Position(0, 0);
    }
}
