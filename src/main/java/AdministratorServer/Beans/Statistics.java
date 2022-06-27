package AdministratorServer.Beans;

import java.util.List;

/**
 * Class to represent a single statistic. It contains the km travelled, the number of rides and the average of the pollution
 * captured by the sensors
 */
public class Statistics {
    private double kmTravelled;
    private int numberOfRides;
    private List<Double> averagePollution;

    public Statistics() {}

    public Statistics(double kmTravelled, int numberOfRides, List<Double> averagePollution) {
        this.kmTravelled = kmTravelled;
        this.numberOfRides = numberOfRides;
        this.averagePollution = averagePollution;
    }

    public double getKmTravelled() {
        return kmTravelled;
    }

    public double getNumberOfRides() {
        return numberOfRides;
    }

    public List<Double> getAveragePollution() {
        return averagePollution;
    }
}
