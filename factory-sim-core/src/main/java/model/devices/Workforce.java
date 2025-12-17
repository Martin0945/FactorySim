package model.devices;

import event.EventBus;
import event.EventSource;
import lombok.Getter;
import lombok.Setter;
import model.devices.state.IdleState;
import model.devices.state.State;
import model.economy.ConsumptionData;
import model.economy.ConsumptionProfile;
import model.inspection.Visitor;
import model.material.Material;
import model.material.MaterialType;

import java.util.List;

@Getter
@Setter
public abstract class Workforce implements EventSource {
    protected State state;
    private String name;

    private List<MaterialType> recipe;
    private List<Material> inputBuffer;
    private Material outputBuffer;
    private MaterialType outputType;

    private int currentProgress = 0;
    private int processDuration;
    private EventBus eventBus;

    private final ConsumptionData consumption = new ConsumptionData();
    private final ConsumptionProfile profile;

    public Workforce(ConsumptionProfile profile) {
        this.state = new IdleState();
        this.profile = profile;
    }

    /**
     * @return  the quality multiplier affecting the final product value.
     * Default is 1.0 (no change).
     */
    protected double getQualityMultiplier() {
        return 1.0;
    }

    /**
     * Consumes resources (money, electricity, oil) based on the profile.
     */
    protected abstract void consume();

    /**
     * Accepts a visitor for inspection purposes.
     *
     * @param visitor {@link Visitor}
     */
    public abstract void acceptVisitor(Visitor visitor);

    /**
     * Performs a single simulation step. Updates state and triggers logic.
     */
    public abstract void tick();

    /**
     * Retrieves the finished product and clears the output buffer.
     * @return The finished Material or null if the buffer is empty.
     */
    public abstract Material takeOutput();

    /**
     * Attempts to load materials into the input buffer.
     * @return true if the input was accepted and state changed, false otherwise.
     */
    public abstract boolean acceptInput(List<Material> material);

    /**
     * Finishes the work by putting the material into output buffer and calculates final value.
     */
    public abstract void finishWork();
}