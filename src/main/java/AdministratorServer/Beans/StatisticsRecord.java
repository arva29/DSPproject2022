package AdministratorServer.Beans;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import java.sql.Timestamp;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class StatisticsRecord {
    private final int id;
    private final Timestamp timestamp;
    private final int batteryLvl;
    private final Statistics statistics;

    public StatisticsRecord(int id, Timestamp timestamp, int batteryLvl, Statistics statistics) {
        this.id = id;
        this.timestamp = timestamp;
        this.batteryLvl = batteryLvl;
        this.statistics = statistics;
    }

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
}
