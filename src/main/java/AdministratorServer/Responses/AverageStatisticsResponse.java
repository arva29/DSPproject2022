package AdministratorServer.Responses;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class AverageStatisticsResponse {
    private double batteryLvl;
    private double kmTravelled;
    private double numberOfRides;
    private double averagePollution;

    public AverageStatisticsResponse(double batteryLvl, double kmTravelled, double numberOfRides, double averagePollution) {
        this.batteryLvl = batteryLvl;
        this.kmTravelled = kmTravelled;
        this.numberOfRides = numberOfRides;
        this.averagePollution = averagePollution;
    }

    public AverageStatisticsResponse() {}

    public double getBatteryLvl() {
        return batteryLvl;
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
