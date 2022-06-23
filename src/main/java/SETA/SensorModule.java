package SETA;

import SETA.Simulators.PM10Simulator;

import java.util.ArrayList;
import java.util.List;

public class SensorModule extends Thread{
    private final MeasurementsBuffer buffer = new MeasurementsBuffer(this);
    private final List<Double> averageMeasurements = new ArrayList<>();

    @Override
    public void run() {
        PM10Simulator simulator = new PM10Simulator(buffer);
        simulator.start();
    }

    public void addAverageMeasurement(double m){
        averageMeasurements.add(m);
    }
}
