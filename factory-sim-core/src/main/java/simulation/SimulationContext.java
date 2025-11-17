package simulation;

public class SimulationContext {
    private int currentTick = 0;

    public int getCurrentTick() {
        return currentTick;
    }

    public void nextTick() {
        currentTick++;
    }
}
