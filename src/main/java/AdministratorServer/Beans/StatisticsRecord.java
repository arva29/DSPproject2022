package AdministratorServer.Beans;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import java.sql.Timestamp;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class StatisticsRecord {
    private int id;
    private Timestamp timestamp;
    private int batteryLvl;
    private Statistics statistics;

    public StatisticsRecord(int id, Timestamp timestamp, int batteryLvl, Statistics statistics) {
        this.id = id;
        this.timestamp = timestamp;
        this.batteryLvl = batteryLvl;
        this.statistics = statistics;
    }

    public StatisticsRecord() {}

    public int getId() {
        return id;
    }

    public Timestamp getTimestamp() {
        return timestamp;
    }

    public double getBatteryLvl() {
        return batteryLvl;
    }

    public Statistics getStatistics() {
        return statistics;
    }

    public void print(){
        System.out.println(" - ID -> " + id);
        System.out.println(" - TIMESTAMP -> " + timestamp.toString());
        System.out.println(" - BATTERY -> " + batteryLvl);
        System.out.println(" - KM -> " + statistics.getKmTravelled());
        System.out.println(" - N° OF RIDES -> " + statistics.getNumberOfRides());
        System.out.println(" - POLLUTION -> " + statistics.getAveragePollution());
    }
}
