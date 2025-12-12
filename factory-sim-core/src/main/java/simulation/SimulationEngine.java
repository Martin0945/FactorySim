package simulation;

import event.Event;
import event.EventBus;
import event.EventType;
import lombok.Getter;
import lombok.extern.java.Log;
import model.factory.Factory;
@Log

public class SimulationEngine {
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

}
