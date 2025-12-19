package model.devices;

import event.EventBus;
import lombok.Getter;
import lombok.Setter;
import model.devices.state.State;
import model.economy.ConsumptionData;
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

    /**
     * Calculates quality based on current wear. Higher wear reduces quality.
     *
     * @return Double ranging from 0.8 - 1.2
     */
    @Override
    protected double getQualityMultiplier() {
        return 1.2 - wear*(0.4/breakThreshold); //map 0 - break threshold, to 1.2 - 0.8
    }

    @Override
    protected void consume(){
        ConsumptionData data = this.getConsumption();
        ConsumptionProfile prof = this.getProfile();

        data.consumeElectricity(prof.electricityPerTick()*(1+wear/100));
        data.consumeOil(prof.oilPerTick()*(1+wear/100));
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

    /**
     * Resets the wear level to zero and updates the state.
     */
    public abstract void repair();
}