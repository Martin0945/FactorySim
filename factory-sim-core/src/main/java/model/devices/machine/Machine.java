package model.devices.machine;

import event.*;
import lombok.Getter;
import lombok.Setter;
import model.devices.Station;
import model.devices.state.IdleState;
import model.inspection.Visitor;
import model.material.Material;
import model.material.MaterialType;

import java.util.List;

@Getter
@Setter
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

        System.out.printf("Finished work at machine %s, output is: %s%n", this.getName(), this.getOutputBuffer().getType());

        // event
        this.setState(this.getState().checkWear(this));
    }

    @Override
    protected void increaseWear() {
        this.setWear(this.getWear() + this.getWearPerTick());
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