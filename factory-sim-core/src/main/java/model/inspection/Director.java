package model.inspection;

import lombok.extern.java.Log;
import model.devices.Workforce;
import model.devices.human.Worker;
import model.devices.machine.Machine;
import model.devices.robot.Robot;
import model.factory.Factory;
import model.production.ProductionLine;

import java.util.List;

@Log

public class Director implements Visitor {
    @Override
    public void visit(Machine m){
        String report = m.getState().getDirectorReport(m);
        log.info("DIRECTOR: %s %s".formatted(m.getName(), report));
    }

    @Override
    public void visit(Robot r){
        String report = r.getState().getDirectorReport(r);
        log.info("DIRECTOR: %s %s".formatted(r.getName(), report));
    }

    public void visit(Worker w){
        String report = w.getState().getDirectorReport(w);
        log.info("DIRECTOR: %s %s".formatted(w.getName(), report));
    }

    @Override
    public void visit(Factory factory) {
        log.info("DIRECTOR: visiting factory %s".formatted(factory.getName()));
    }

    /**
     * Calculates and logs the percentage of active (working) stations on this production line.
     */
    @Override
    public void visit(ProductionLine prodLine) {
        double activeCount = 0;

        List<Workforce> workforces = prodLine.getWorkforces();
        for(Workforce workforce : workforces){
            if (workforce.getState().isWorking(workforce)){
                activeCount++;
            }
        }
        double activePercentage = (activeCount/(double)workforces.size() * 100);

        log.info("DIRECTOR: visiting line %s with %f%% work stations currently working."
                .formatted(prodLine.getName(), activePercentage));
    }
}
