package simulation;

import event.Event;
import event.EventBus;
import event.EventSource;
import event.EventType;
import lombok.Getter;
import lombok.extern.java.Log;
import model.factory.Factory;
import simulation.repair.RepairManager;

@Log

public class SimulationEngine implements EventSource {
    private final int DIRECTOR_TICK = 11;
    private final int INSPECTOR_TICK = 12;

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

    /**
     * Executes a single simulation step:
     * 1. Updates the factory and repair manager.
     * 2. Advances time.
     * 3. Triggers periodic visits (Director/Inspector).
     * 4. Dispatches all queued events.
     */
    public void runTick() {
        log.info("=================TICK: %d ================%n".formatted(context.getCurrentTick() + 1));

        factory.onTick(context);
        repairManager.onTick(context);
        context.nextTick();

        if(getCurrentTick() % DIRECTOR_TICK == 0){
            log.info("Generating director visit event!");
            eventBus.publish(new Event(EventType.DIRECTOR_VISIT, this, 5));
        }
        if(getCurrentTick() % INSPECTOR_TICK == 0){
            log.info("Generating inspector visit event!");
            eventBus.publish(new Event(EventType.INSPECTION, this, 6));
        }

        eventBus.dispatchAll();
    }

    public int getCurrentTick() {
        return context.getCurrentTick();
    }

    @Override
    public String getName(){
        return "SimulationEngine";
    }
}
