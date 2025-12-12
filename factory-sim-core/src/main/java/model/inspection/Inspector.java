package model.inspection;

import lombok.extern.java.Log;
import model.devices.Station;
import model.devices.Workforce;
import model.devices.human.Worker;
import model.devices.human.WorkerFactory;
import model.devices.machine.Machine;
import model.devices.robot.Robot;
import model.factory.Factory;
import model.production.ProductionLine;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Log

public class Inspector implements Visitor {
    private final List<Station> stations = new ArrayList<>();

    private List<Station> sortStations() {
        return stations.stream()
                .sorted(Comparator.comparingDouble(Station::getWear).reversed())
                .toList();

    }

    public void reportInspection(){
        List<Station> sortedStations = sortStations();

        for(Station station : sortedStations){
            String msg;

            if(station.getWear() <= 20){
                msg = "is looking OK (%f%%).".formatted(station.getWear());
            }else if(station.getWear() <= 60){
                msg = "is starting to look worn down (%f%%).".formatted(station.getWear());
            }else if(station.getWear() < 80){
                msg = "is reaching critical level (%f%%).".formatted(station.getWear());
            }else{
                msg = "reached CRITICAL level (%f%%), should be fixed immediately.".formatted(station.getWear());
            }

            log.info("INSPECTOR: %s %s".formatted(station.getName(), msg));
        }

    }

    @Override
    public void visit(Machine m){
        stations.add(m);
    }

    @Override
    public void visit(Robot r){
        stations.add(r);
    }

    @Override
    public void visit(Worker w){
    }

    @Override
    public void visit(ProductionLine prodLine) {
        log.info("INSPECTOR: at production line %s".formatted(prodLine.getName()));
    }

    @Override
    public void visit(Factory factory) {
        log.info("INSPECTOR: visiting factory %s".formatted(factory.getName()));
    }
}
