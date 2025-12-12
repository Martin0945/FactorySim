package model.factory;

import lombok.AllArgsConstructor;
import lombok.Getter;
import model.devices.human.Worker;
import model.devices.machine.Machine;
import model.devices.robot.Robot;
import model.inspection.Director;
import model.inspection.Inspector;
import model.inspection.Visitor;
import model.production.ProductionLine;
import simulation.Simulatable;
import simulation.SimulationContext;

import java.util.List;

@AllArgsConstructor

public class Factory implements Simulatable {
    @Getter
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

    public void acceptVisitor(Director director){
        director.visit(this);
        productionLines.forEach(pl -> pl.acceptVisitor(director));
    }

    public void acceptVisitor(Inspector inspector){
        inspector.visit(this);
        productionLines.forEach(pl -> pl.acceptVisitor(inspector));

    }
}
