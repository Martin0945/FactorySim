package simulation;

import event.EventBus;
import lombok.Getter;
import model.factory.Factory;

public class SimulationEngine {

    private final Factory factory;
    private final RepairManager repairManager;
    private final SimulationContext context = new SimulationContext();
    @Getter
    private final EventBus eventBus;

    public SimulationEngine(Factory factory, RepairManager repairManager, EventBus eventBus) {
        this.factory = factory;
        this.repairManager = repairManager;
        this.eventBus = eventBus;
    }

    public void runTick() {
        factory.onTick(context);
        repairManager.onTick(context);
        context.nextTick();
        eventBus.dispatchAll();
    }

    public int getCurrentTick() {
        return context.getCurrentTick();
    }

}
