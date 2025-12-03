package model.devices.machine;

import event.*;
import lombok.Getter;
import lombok.Setter;
import model.devices.Station;
import model.devices.state.IdleState;
import model.devices.state.State;
import model.devices.state.WorkingState;
import model.inspection.Visitor;
import model.material.Material;

@Getter
public class Machine extends Station {
    @Setter
    private double wear = 0;
    private final double wearPerTick = 20.0;
    private final double breakThreshold = 80.0;
    private final int priority;
    private final EventBus eventBus;
    private final int oil;
    private final Material input;
    private final Material output;

    @Setter
    private State state;
    private String name;

    public Machine(String name, int electricity, int oil, int priority, EventBus eventBus, Material input, Material output) {
        super(electricity);
        this.name = name;
        this.oil = oil;
        this.priority = priority;
        this.eventBus = eventBus;
        this.input = input;
        this.output = output;
        this.state = new WorkingState(); //default
    }


    //delegate functions to state
    @Override
    public Material process(Material current) {
        return state.process(this, current);
    }

    protected void increaseWear() {
        state.increaseWear(this);
    }

    public void repair() {
        wear = 0;
        setState(new IdleState()); //be idle after being repaired
        System.out.println("Repaired " + this.name);
    }

    @Override
    public void acceptVisitor(Visitor v) {

    }

}
