package model.devices.robot;

import event.Event;
import event.EventBus;
import event.EventType;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.java.Log;
import model.devices.Station;
import model.devices.state.IdleState;
import model.inspection.Director;
import model.inspection.Visitor;
import model.material.Material;
import model.material.MaterialType;

import java.util.List;

@Getter
@Setter
@Log
public class Robot extends Station {


    public Robot(String name, int electricity, double wearPerTick, int priority,
                 int processDuration, MaterialType outputType, List<MaterialType> recipe,
                 EventBus eventBus) {
        super(electricity, eventBus);
        this.setName(name);
        this.setWearPerTick(wearPerTick);
        this.setPriority(priority);
        this.setProcessDuration(processDuration);
        this.setBreakThreshold(70.0);
        this.setEventBus(eventBus);
        this.setRecipe(recipe);
        this.setProcessDuration(processDuration);

        this.setOutputType(outputType);

        this.setState(new IdleState());
    }

    @Override
    public void finishWork() {
        this.setOutputBuffer(new Material(this.getOutputType()));

        this.setInputBuffer(null);

        //log.info("Robot %s FINISHED: %s".formatted(this.name, this.outputType));
        //event

    }

    @Override
    public void repair() {
        this.setWear(0);
        state = state.checkWear(this);
        log.info("Robot %s was repaired/serviced.".formatted(this.getName()));
    }

    @Override
    public void acceptVisitor(Visitor visitor) {
        visitor.visit(this);
    }

}