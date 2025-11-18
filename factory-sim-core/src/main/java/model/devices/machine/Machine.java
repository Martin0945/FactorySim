package model.devices.machine;

import event.*;
import lombok.Getter;
import model.production.Station;
import model.material.Material;

public class Machine extends Station {
    private boolean working = true;
    private double wear = 0.0;
    private final double wearPerTick = 20.0;
    private final double breakThreshold = 80.0;
    @Getter
    private final int priority;
    @Getter
    private final EventBus eventBus;

    public Machine(String name, int priority, EventBus eventBus) {
        super(name);
        this.priority = priority;
        this.eventBus = eventBus;
    }

    private void increaseWear() {
        wear += wearPerTick;
        if (wear >= breakThreshold && working) {
            working = false;
            System.out.println("Machine " + getName() + " broke, wear " + wear + "%");

            if (eventBus != null) {
                eventBus.publish(new Event(EventType.BREAKDOWN,this,this.priority));
            }
        }
    }

    public void repair() {
        this.working = true;
        wear = 0.0;
        System.out.println("Repaired " + getName());
    }

    @Override
    public Material process(Material input) {
        if (!working) return input;
        increaseWear();
        return input;
    }

}