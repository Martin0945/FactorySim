package model.factory;

import lombok.AllArgsConstructor;
import model.devices.human.Worker;
import model.devices.machine.Machine;
import model.devices.robot.Robot;
import model.production.ProductionLine;
import simulation.Simulatable;
import simulation.SimulationContext;

import java.util.List;

@AllArgsConstructor

public class Factory implements Simulatable {
    private String name;
    private List<ProductionLine> productionLines;
    private List<Worker> workers;
    private List<Machine> machines;
    private List<Robot> robots;
    private Inventory inventory;

    public Factory(String factoryName, List<ProductionLine> lines) {
        this.name = factoryName;
        this.productionLines = lines;
    }


    @Override
    public void onTick(SimulationContext context) {
        productionLines.forEach(pl -> pl.onTick(context));
    }
}
