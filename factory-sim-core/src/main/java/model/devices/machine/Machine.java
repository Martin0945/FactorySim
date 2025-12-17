package model.devices.machine;

import event.*;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.java.Log;
import model.devices.Station;
import model.economy.ConsumptionProfile;
import model.inspection.Visitor;
import model.material.Material;
import model.material.MaterialType;

import java.util.List;

@Getter
@Setter
@Log
public class Machine extends Station {
    public Machine(int electricity, double wearPerTick, int priority, EventBus eventBus,
                   int oil, MaterialType outputType, int processDuration, String name,
                   List<MaterialType> recipe) {
        super(new ConsumptionProfile(electricity,oil,0.0 ), eventBus);

        this.setOutputType(outputType);

        this.setWearPerTick(wearPerTick);
        this.setPriority(priority);
        this.setEventBus(eventBus);
        this.setProcessDuration(processDuration);
        this.setBreakThreshold(80.0);
        this.setName(name);
        this.setRecipe(recipe);
        this.setProcessDuration(processDuration);
    }

    @Override
    public void finishWork() {
        double inputValue = inputBuffer.stream()
                .mapToDouble(Material::getValue)
                .sum();
        double finalValue = inputValue * getQualityMultiplier() * 2.0;
        this.setOutputBuffer(new Material(this.getOutputType(),finalValue));
        this.setInputBuffer(null);

        //log.info("Machine %s FINISHED: %s".formatted(this.name, this.outputType));

        //todo event
    }

    @Override
    public void repair() {
        this.setWear(0);
        state = state.checkWear(this);
        log.info("Machine %s was repaired/serviced.".formatted(this.getName()));
    }

    @Override
    public void acceptVisitor(Visitor visitor) {
        visitor.visit(this);
    }
}