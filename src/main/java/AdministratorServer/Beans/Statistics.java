package AdministratorServer.Beans;

public class Statistics {
    private double kmTravelled;
    private int numberOfRides;
    private double averagePollution;

    public Statistics() {}

    public Statistics(double kmTravelled, int numberOfRides, double averagePollution) {
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

    public double getAveragePollution() {
        return averagePollution;
    }
}
