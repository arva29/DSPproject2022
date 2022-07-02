package AdministratorServer;

import AdministratorServer.Beans.StatisticsRecord;
import AdministratorServer.Responses.AverageStatisticsResponse;
import AdministratorServer.Responses.LastStatisticsByIdResponse;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Class that works as a storage of statistics for the Administrator server and provide all the methods to query these statistics
 * in order to satisfy client requests.
 */
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

    /**
     * Add a statistic to the list of statistic already stored
     * @param statisticToAdd statistic that has to be added
     */
    public void addStatistics(StatisticsRecord statisticToAdd){
        getAllStatistics().computeIfAbsent(statisticToAdd.getId(), k -> new ArrayList<>());
        getAllStatistics().get(statisticToAdd.getId()).add(statisticToAdd);

        System.out.println("Added statistics from " + statisticToAdd.getId());
    }

    /**
     * Return all the last n statistics of the taxi with the id specified as param
     * @param n number of statistics to return
     * @param taxiId id of the taxi for which it has to provides the statistics
     * @return all the last n statistics of the taxi with the id specified as param
     */
    public LastStatisticsByIdResponse getLastNStatistics(int n, int taxiId){
        if(getAllStatistics().get(taxiId) != null) {
            List<StatisticsRecord> allStatistics = getAllStatistics().get(taxiId);


            if (n > allStatistics.size()) {
                n = allStatistics.size();
            }

            double kmTravelled = 0;
            double batteryLvl = 0;
            double pollutionLvl = 0;
            double accomplishedRides = 0;
            int pollutionCounter = 0;

            for (int i = allStatistics.size() - 1; i >= allStatistics.size() - n; i--) {
                kmTravelled += allStatistics.get(i).getStatistics().getKmTravelled();
                batteryLvl += allStatistics.get(i).getBatteryLvl();
                for (double d : allStatistics.get(i).getStatistics().getAveragePollution()) {
                    pollutionLvl += d;
                    pollutionCounter++;
                }
                accomplishedRides += allStatistics.get(i).getStatistics().getNumberOfRides();
            }

            return new LastStatisticsByIdResponse(
                    taxiId,
                    new AverageStatisticsResponse(
                            batteryLvl / n,
                            kmTravelled / n,
                            accomplishedRides / n,
                            pollutionLvl / pollutionCounter
                    )
            );
        } else {
            return new LastStatisticsByIdResponse(
                    -1,
                    new AverageStatisticsResponse(
                            0,
                            0,
                            0,
                            0
                    )
            );
        }
    }

    /**
     * Return the average of the statistics of all taxis in the period t2 - t1
     * @param t1 timestamp 1
     * @param t2 timestamp 2
     * @return Average of the statistics of all taxis between the two timestamp specified
     */
    public AverageStatisticsResponse getAverageStatisticsFromT1ToT2(Timestamp t1, Timestamp t2){

        List<StatisticsRecord> allRecords;

        if (getAllStatistics().keySet().size() > 0) {
            double kmTravelled = 0;
            double batteryLvl = 0;
            double pollutionLvl = 0;
            double accomplishedRides = 0;
            int statsCounter = 0;
            int pollutionCounter = 0;

            for (Integer key : getAllStatistics().keySet()) {
                allRecords = getAllStatistics().get(key);

                for (StatisticsRecord record : allRecords) {
                    if (Timestamp.valueOf(record.getTimestamp()).after(t1) && Timestamp.valueOf(record.getTimestamp()).before(t2)) {
                        kmTravelled += record.getStatistics().getKmTravelled();
                        batteryLvl += record.getBatteryLvl();
                        for (double d : record.getStatistics().getAveragePollution()) {
                            pollutionLvl += d;
                            pollutionCounter++;
                        }
                        accomplishedRides += record.getStatistics().getNumberOfRides();
                        statsCounter++;
                    }
                }
            }

            return new AverageStatisticsResponse(
                    batteryLvl / statsCounter,
                    kmTravelled / statsCounter,
                    accomplishedRides / statsCounter,
                    pollutionLvl / pollutionCounter
            );
        } else {
            return new AverageStatisticsResponse(
                    -1,
                    0,
                    0,
                    0
            );
        }
    }
}
