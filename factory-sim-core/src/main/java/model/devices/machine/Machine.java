package model.devices.machine;

import event.*;
import lombok.Getter;
import model.devices.Station;
import model.inspection.Visitor;
import model.material.Material;
@Getter

public class Machine extends Station {
    private boolean working = true;
    private double wear = 0.0;
    private final double wearPerTick = 20.0;
    private final double breakThreshold = 80.0;
    private final int priority;
    private final EventBus eventBus;
    private final int oil;
    private MachineType machineType;

    public Machine(MachineType machineType, int electricity, int oil, int priority, EventBus eventBus) {
        super(electricity);
        this.machineType = machineType;
        this.oil = oil;
        this.priority = priority;
        this.eventBus = eventBus;
    }

    @Override
    protected void increaseWear() {
        wear += wearPerTick;
        if (wear >= breakThreshold && working) {
            working = false;
            System.out.println("Machine " + getMachineType() + " broke, wear " + wear + "%");

            if (eventBus != null) {
                eventBus.publish(new Event(EventType.BREAKDOWN,this,this.priority));
            }
        }
    }

    public void repair() {
        this.working = true;
        wear = 0.0;
        System.out.println("Repaired " + getMachineType());
    }

    public Material process(Material input) {
        if (!working) return input;
        increaseWear();
        return input;
    }

    public void acceptVisitor(Visitor v){
        v.visit(this);
    }
}