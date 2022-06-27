package SETA;

import SETA.Simulators.Buffer;
import SETA.Simulators.Measurement;

import java.util.ArrayList;
import java.util.List;

/**
 * Buffer to manage the data coming from the sensor with a sliding window system
 */
public class MeasurementsBuffer implements Buffer {
    private final int WINDOW_DIMENSION = 8;
    private final StatisticsModule statisticsModule;
    List<Measurement> measurementsWindow;

    public MeasurementsBuffer(StatisticsModule statisticsModule) {
        this.measurementsWindow = new ArrayList<>();
        this.statisticsModule = statisticsModule;
    }

    /**
     * Add to a list the measurement coming from the sensor and when the dimension of the list is equal to the dimension
     * of the sliding window it calculated the average of that measurements and save it
     * @param m measurement to add
     */
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
            statisticsModule.addAverageMeasurement(averageMeasurement(readAllAndClean()));
        }
    }

    /**
     * Return the window with the measurements before removing first 4 elements of it
     * @return window before cleaning
     */
    @Override
    public List<Measurement> readAllAndClean() {
        List<Measurement> window = new ArrayList<>(measurementsWindow);
        measurementsWindow.subList(0,5).clear(); //Delete first 4 elements
        return window;
    }

    /**
     * Calculates the average of the list of measurements
     * @param list list of measurements
     * @return average of measurements as param
     */
    public double averageMeasurement(List<Measurement> list){
        double sum = 0;

        for(Measurement m: list){
            sum += m.getValue();
        }

        return sum/WINDOW_DIMENSION;
    }
}
