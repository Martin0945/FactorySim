package model.devices.human;

import event.EventBus;
import lombok.Getter;
import lombok.extern.java.Log;
import model.devices.Workforce;
import model.devices.state.State;
import model.economy.ConsumptionProfile;
import model.inspection.Visitor;
import model.material.Material;
import model.material.MaterialType;

import java.util.List;

@Getter
@Log

public class Worker extends Workforce {
    private final int priority;
    private final EventBus eventBus;
    private final double WORKER_MULTIPLIER = 2;

    public Worker(int pay, int priority, EventBus eventBus, MaterialType outputType,
                  int processDuration, List<MaterialType> recipe, String name) {
        super(new ConsumptionProfile(0.0,0.0,pay));
        this.priority = priority;
        this.eventBus = eventBus;
        this.setOutputType(outputType);

        this.setProcessDuration(processDuration);
        this.setName(name);
        this.setRecipe(recipe);
        this.setEventBus(eventBus);

    }

    /**
     * Pays wages to the worker based on the consumption profile.
     */
    @Override
    protected void consume(){
        this.getConsumption().payWages(this.getProfile().wagePerTick());
    }

    @Override
    public boolean acceptInput(List<Material> materials) {
        State oldState = state;

        if(this.getInputBuffer() == null && this.getOutputBuffer() == null){
            state = state.acceptInput(this, materials);
        }

        return !this.state.getClass().equals(oldState.getClass());
    }

    public void tick() {
        this.state = state.handleTick(this);

        if (state.isWorking(this)) {
            consume();
        }
    }

    public void finishWork() {
        double inputValue = this.getInputBuffer().stream()
                .mapToDouble(Material::getValue)
                .sum();
        double finalValue = inputValue * getQualityMultiplier() * WORKER_MULTIPLIER;

        this.setOutputBuffer(new Material(this.getOutputType(), finalValue));
        this.setInputBuffer(null);
    }

    public Material takeOutput() {
        Material buffer = this.getOutputBuffer();
        if(buffer == null){
            return null;
        }
        this.setOutputBuffer(null);
        return buffer;
    }

    @Override
    public void acceptVisitor(Visitor visitor) {
        visitor.visit(this);
    }
}