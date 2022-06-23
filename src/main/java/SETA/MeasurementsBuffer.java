package SETA;

import SETA.Simulators.Buffer;
import SETA.Simulators.Measurement;

import java.util.ArrayList;
import java.util.List;

public class MeasurementsBuffer implements Buffer {
    private final int WINDOW_DIMENSION = 8;
    private final SensorModule sensorModule;
    List<Measurement> measurementsWindow;

    public MeasurementsBuffer(SensorModule sensorModule) {
        this.measurementsWindow = new ArrayList<>();
        this.sensorModule = sensorModule;
    }

    @Override
    public void addMeasurement(Measurement m) {
        /**
         * TODO: Sync timestamp ??
         */
        int comparison = m.compareTo(measurementsWindow.get(measurementsWindow.size()-1));
        if(comparison <= 0){
            m.setTimestamp(measurementsWindow.get(measurementsWindow.size()-1).getTimestamp() + 1);
        }

        measurementsWindow.add(m);

        if(measurementsWindow.size() == 8){
            sensorModule.addAverageMeasurement(averageMeasurement(readAllAndClean()));
        }
    }

    @Override
    public List<Measurement> readAllAndClean() {
        List<Measurement> window = new ArrayList<>(measurementsWindow);
        measurementsWindow.subList(0,5).clear(); //Delete first 4 elements
        return window;
    }

    public double averageMeasurement(List<Measurement> list){
        double sum = 0;

        for(Measurement m: list){
            sum += m.getValue();
        }

        return sum/WINDOW_DIMENSION;
    }
}
