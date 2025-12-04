package model.devices;

import event.EventBus;
import lombok.Getter;
import lombok.Setter;
import model.devices.state.State;
import model.material.Material;

import java.util.List;

@Getter
@Setter
public abstract class Station extends Workforce {
    private int electricity;
    private double wear;
    private double wearPerTick;
    private double breakThreshold;
    private EventBus eventBus;
    private int priority;
    private int processDuration;

    public Station(int electricity) {
        super();
        this.electricity = electricity;
        this.wear = 0;
    }

    @Override
    public void tick(){
        this.state = state.handleTick(this);
    }

    @Override
    public boolean acceptInput(List<Material> materials) {
        State oldState = state;

        if(getInputBuffer() == null && getOutputBuffer() == null){
            this.state = state.acceptInput(this, materials);
            return true;
        }

        return !this.state.getClass().equals(oldState.getClass());
    }

    @Override
    public Material takeOutput() {
        if(getOutputBuffer() == null){
            return null;
        }
        Material out = getOutputBuffer();
        setOutputBuffer(null);
        return out;
    }

    public abstract void finishWork();
    protected abstract void increaseWear();
    public abstract void repair();
}