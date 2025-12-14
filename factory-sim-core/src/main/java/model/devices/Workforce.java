package model.devices;

import event.EventBus;
import lombok.Getter;
import lombok.Setter;
import model.devices.state.IdleState;
import model.devices.state.State;
import model.economy.ConsumptionData;
import model.economy.ConsumptionProfile;
import model.inspection.Visitor;
import model.material.Material;
import model.material.MaterialType;

import java.lang.reflect.AnnotatedType;
import java.util.List;

@Getter
@Setter
public abstract class Workforce {
    protected State state;
    protected String name;
    protected List<MaterialType> recipe;
    protected List<Material> inputBuffer;
    protected Material outputBuffer;
    protected int currentProgress = 0;
    private int processDuration;
    private EventBus eventBus;
    private MaterialType outputType;
    @Getter
    protected final ConsumptionData consumption = new ConsumptionData();

    protected final ConsumptionProfile profile;



    public Workforce(ConsumptionProfile profile) {
        this.state = new IdleState();
        this.profile = profile;
    }

    public void consume() {
        consumption.consumeElectricity(profile.electricityPerTick());
        consumption.consumeOil(profile.oilPerTick());
        consumption.payWages(profile.wagePerTick());
    }

    public ConsumptionData getConsumptionData() {
        return consumption;
    }
    // inside Workforce class
    public List<MaterialType> getInput() {
        return recipe;
    }

    public MaterialType getOutput() {
        return outputType;
    }

    public abstract void acceptVisitor(Visitor visitor);

    public abstract void tick();

    public abstract Material takeOutput();

    public abstract boolean acceptInput(List<Material> material);

    public abstract void finishWork();
}