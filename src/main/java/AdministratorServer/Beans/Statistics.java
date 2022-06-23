package AdministratorServer.Beans;

import java.util.List;

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
