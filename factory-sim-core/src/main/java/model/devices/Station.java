package model.devices;

import event.EventBus;
import lombok.Getter;
import lombok.Setter;
import model.devices.state.State;
import model.economy.ConsumptionProfile;
import model.material.Material;

import java.util.List;

@Getter
@Setter
public abstract class Station extends Workforce {
    private double wear;
    private double wearPerTick;
    private double breakThreshold;
    private int priority;

    public Station(ConsumptionProfile consumptionProfile, EventBus eventBus) {
        super(consumptionProfile);
        this.wear = 0;
        this.setEventBus(eventBus);
    }

    @Override
    public void tick(){
        this.state = state.handleTick(this);

        if (state.isWorking(this)) {
            consume();
        }
    }
    @Override public double getQualityMultiplier() {
        return 1.2 - wear*(0.4/breakThreshold); //map 0 - break threshold, to 1.2 - 0.8
    }


    @Override
    public void consume(){
        consumption.consumeElectricity(profile.electricityPerTick()*(1+wear/100));
        consumption.consumeOil(profile.oilPerTick()*(1+wear/100));
    }

    @Override
    public boolean acceptInput(List<Material> materials) {
        State oldState = state;

        if(getInputBuffer() == null && getOutputBuffer() == null){
            this.state = state.acceptInput(this, materials);
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
    public abstract void repair();
}