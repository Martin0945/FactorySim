package simulation;

import event.EventBus;
import lombok.Getter;
import model.factory.Factory;

public class SimulationEngine {

    private final Factory factory;
    private final SimulationContext context = new SimulationContext();
    @Getter
    private final EventBus eventBus = new EventBus();

    public SimulationEngine(Factory factory) {
        this.factory = factory;
    }

    public void runTick() {
        factory.onTick(context);
        context.nextTick();
        eventBus.dispatchAll();
    }

    public int getCurrentTick() {
        return context.getCurrentTick();
    }

}
