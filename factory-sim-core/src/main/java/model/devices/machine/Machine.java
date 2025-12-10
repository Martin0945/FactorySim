package model.devices.machine;

import event.*;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.java.Log;
import model.devices.Station;
import model.devices.state.IdleState;
import model.inspection.Visitor;
import model.material.Material;
import model.material.MaterialType;

import java.util.List;

@Getter
@Setter
@Log
public class Machine extends Station {
    private final int oil;
    private final MaterialType outputType;

    public Machine(int electricity, double wearPerTick, int priority, EventBus eventBus,
                   int oil, MaterialType outputType, int processDuration, String name,
                   List<MaterialType> recipe) {
        super(electricity);

        this.oil = oil;
        this.outputType = outputType;

        this.setWearPerTick(wearPerTick);
        this.setPriority(priority);
        this.setEventBus(eventBus);
        this.setProcessDuration(processDuration);
        this.setBreakThreshold(80.0);
        this.setName(name);
        this.setRecipe(recipe);
    }

    @Override
    public void finishWork() {
        this.setOutputBuffer(new Material(this.outputType));
        this.setInputBuffer(null);

        log.info("Machine %s FINISHED: %s".formatted(this.name, this.outputType));

        //todo event
    }

    @Override
    public void repair() {
        this.setWear(0);
        this.setState(new IdleState());
        System.out.println("Repaired " + this.getName());
    }

    @Override
    public void acceptVisitor(Visitor v) {
        //v.visit(this);
    }
}