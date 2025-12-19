package model.devices.robot;

import event.EventBus;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.java.Log;
import model.devices.Station;
import model.devices.state.IdleState;
import model.economy.ConsumptionProfile;
import model.inspection.Visitor;
import model.material.Material;
import model.material.MaterialType;

import java.util.List;

@Getter
@Setter
@Log
public class Robot extends Station {
    private final double BREAK_THRESHOLD = 70.0;
    private final double ROBOT_MULTIPLIER = 2.2;

    public Robot(String name, int electricity, double wearPerTick, int priority,
                 int processDuration, MaterialType outputType, List<MaterialType> recipe,
                 EventBus eventBus) {
        super(new ConsumptionProfile(electricity,0.0,0.0 ), eventBus);
        this.setName(name);
        this.setWearPerTick(wearPerTick);
        this.setPriority(priority);
        this.setProcessDuration(processDuration);
        this.setBreakThreshold(BREAK_THRESHOLD);
        this.setEventBus(eventBus);
        this.setRecipe(recipe);
        this.setProcessDuration(processDuration);

        this.setOutputType(outputType);
        this.setState(new IdleState());
    }

    @Override
    public void finishWork() {
        double inputValue = this.getInputBuffer().stream()
                .mapToDouble(Material::getValue)
                .sum();
        double finalValue = inputValue * getQualityMultiplier() * ROBOT_MULTIPLIER;

        this.setOutputBuffer(new Material(this.getOutputType(), finalValue));
        this.setInputBuffer(null);
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