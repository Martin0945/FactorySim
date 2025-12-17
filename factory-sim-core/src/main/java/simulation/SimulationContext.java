package simulation;

import lombok.Getter;

@Getter
public class SimulationContext {
    private int currentTick = 0;

    /**
     * Advances the simulation time.
     */
    void nextTick() {
        currentTick++;
    }
}
