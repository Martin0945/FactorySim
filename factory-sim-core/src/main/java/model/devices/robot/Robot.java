package model.devices.robot;

import event.Event;
import event.EventBus;
import event.EventType;
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
public class Robot extends Station {

    private final MaterialType outputType;

    public Robot(String name, int electricity, double wearPerTick, int priority,
                 int processDuration, MaterialType outputType, List<MaterialType> recipe,
                 EventBus eventBus) {
        super(electricity);
        this.setName(name);
        this.setWearPerTick(wearPerTick);
        this.setPriority(priority);
        this.setProcessDuration(processDuration);
        this.setEventBus(eventBus);
        this.setRecipe(recipe);

        this.outputType = outputType;

        this.setState(new IdleState());
    }

    @Override
    public void finishWork() {
        this.setOutputBuffer(new Material(this.outputType));

        this.setInputBuffer(null);

        log.info("Robot %s FINISHED: %s".formatted(this.name, this.outputType));
        //event

    }

    @Override
    protected void increaseWear() {
    }

    @Override
    public void repair() {
        this.setWear(0);
        this.setState(new IdleState());
        System.out.println("Robot " + this.getName() + " was repaired/serviced.");
    }

    @Override
    public void acceptVisitor(Visitor v) {
        v.visit(this);
    }

}