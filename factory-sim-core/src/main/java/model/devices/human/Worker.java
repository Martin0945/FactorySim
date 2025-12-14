package model.devices.human;

import event.EventBus;
import lombok.Getter;
import lombok.extern.java.Log;
import model.devices.Workforce;
import model.devices.state.IdleState;
import model.devices.state.State;
import model.economy.ConsumptionProfile;
import model.inspection.Director;
import model.inspection.Visitor;
import model.material.Material;
import model.material.MaterialType;

import java.util.List;

@Getter
@Log

public class Worker extends Workforce {
    private final int priority;
    private final EventBus eventBus;

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

    @Override
    public void acceptVisitor(Visitor visitor) {
        visitor.visit(this);
    }

    @Override
    public boolean acceptInput(List<Material> materials) {
        State oldState = state;

        if(inputBuffer == null && outputBuffer == null){

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
        this.outputBuffer = new Material(this.getOutputType());
        inputBuffer = null;

        //log.info("Worker %s FINISHED: %s".formatted(this.name, this.outputType));

    }

    public Material takeOutput() {
        if(outputBuffer == null){
            return null;
        }
        Material out = outputBuffer;
        outputBuffer = null;
        return out;
    }

}