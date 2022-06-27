package SETA;

import AdministratorServer.Beans.Statistics;
import AdministratorServer.Beans.StatisticsRecord;
import SETA.Simulators.PM10Simulator;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

/**
 * Module to manage the sensor and the buffer with the sliding windows
 */
public class StatisticsModule extends Thread{
    private final MeasurementsBuffer buffer = new MeasurementsBuffer(this);
    private final RESTServerModule restServerModule;

    private final List<Double> averageMeasurements = new ArrayList<>();
    private int numberOfRides = 0;
    private int kmTravelled = 0;


    public StatisticsModule(RESTServerModule restServerModule){
        this.restServerModule = restServerModule;
    }

    @Override
    public void run() {
        PM10Simulator simulator = new PM10Simulator(buffer);
        simulator.start();

        while(true){
            try {
                Thread.sleep(15000);
                sendStatistics();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Sends the statistics to the server by taking the data and adding to them its taxi id, battery level and the current timestamp
     */
    private void sendStatistics() {
        Statistics statisticsToSend = new Statistics(kmTravelled, numberOfRides, averageMeasurements);
        StatisticsRecord statisticsRecord = new StatisticsRecord(Taxi.getTaxiNetworkInfo().getId(), new Timestamp(System.currentTimeMillis()), Taxi.getBatteryLvl(), statisticsToSend);

        restServerModule.sendStatistics(statisticsRecord);

        System.out.println("--- STATISTICS SENDS ---");

        cleanData();
    }

    /**
     * Clear the data collected
     */
    private synchronized void cleanData(){
        numberOfRides = 0;
        kmTravelled = 0;
        averageMeasurements.clear();
    }

    public synchronized void addAverageMeasurement(double m){
        averageMeasurements.add(m);
    }

    public synchronized List<Double> getAverageMeasurements() {
        return averageMeasurements;
    }

    public synchronized void addRide(){
        numberOfRides++;
    }

    public synchronized void addKmTravelled(int km){
        kmTravelled += km;
    }
}
