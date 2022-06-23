package AdministratorServer.Beans;

import AdministratorServer.Responses.AverageStatisticsResponse;
import AdministratorServer.Responses.LastStatisticsByIdResponse;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.List;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class StatisticsStorage {
    private final HashMap<Integer, List<StatisticsRecord>> statisticsStorage;
    private static StatisticsStorage instance;

    public StatisticsStorage() {
        this.statisticsStorage = new HashMap<>();
    }

    public static synchronized StatisticsStorage getInstance() {
        if(instance == null){
            instance = new StatisticsStorage();
        }
        return instance;
    }

    private synchronized HashMap<Integer, List<StatisticsRecord>> getAllStatistics() {
        return statisticsStorage;
    }

    public void addStatistics(StatisticsRecord statisticToAdd){
        getAllStatistics().get(statisticToAdd.getId()).add(statisticToAdd);
    }

    public LastStatisticsByIdResponse getLastNStatistics(int n, int taxiId){
        List<StatisticsRecord> allStatistics = getAllStatistics().get(taxiId);

        double kmTravelled = 0;
        double batteryLvl = 0;
        double pollutionLvl = 0;
        double accomplishedRides = 0;

        for(int i = allStatistics.size() - 1; i >= allStatistics.size() - n; i--){
            kmTravelled += allStatistics.get(i).getStatistics().getKmTravelled();
            batteryLvl += allStatistics.get(i).getBatteryLvl();
            /**
             * TODO: Sistemare
             */
            //pollutionLvl += allStatistics.get(i).getStatistics().getAveragePollution();
            accomplishedRides += allStatistics.get(i).getStatistics().getNumberOfRides();
        }

        return new LastStatisticsByIdResponse(
                taxiId,
                new AverageStatisticsResponse(
                batteryLvl/n,
                kmTravelled/n,
                accomplishedRides/n,
                pollutionLvl/n
                )
        );
    }

    public AverageStatisticsResponse getAverageStatisticsFromT1ToT2(Timestamp t1, Timestamp t2){

        List<StatisticsRecord> allRecords;
        double kmTravelled = 0;
        double batteryLvl = 0;
        double pollutionLvl = 0;
        double accomplishedRides = 0;
        int statsCounter = 0;

        for(Integer key: getAllStatistics().keySet()){
            allRecords = getAllStatistics().get(key);

            for(StatisticsRecord record: allRecords){
                if(record.getTimestamp().after(t1) && record.getTimestamp().before(t2)){
                    kmTravelled += record.getStatistics().getKmTravelled();
                    batteryLvl += record.getBatteryLvl();
                    /**
                     * TODO: Sistemare
                     */
                    //pollutionLvl += record.getStatistics().getAveragePollution();
                    accomplishedRides += record.getStatistics().getNumberOfRides();
                    statsCounter++;
                }
            }
        }

        return new AverageStatisticsResponse(
                batteryLvl/statsCounter,
                kmTravelled/statsCounter,
                accomplishedRides/statsCounter,
                pollutionLvl/statsCounter
        );

    }
}
