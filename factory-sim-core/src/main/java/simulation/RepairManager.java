package simulation;

import event.*;
import event.EventListener;
import lombok.Getter;
import lombok.extern.java.Log;
import model.devices.Station;
import simulation.stats.OutageStats;

import java.util.*;

@Log

public class RepairManager implements EventListener, Simulatable {
    private final PriorityQueue<Station> repairQueue = new PriorityQueue<>(Comparator.comparingInt(Station::getPriority).reversed());

    private final RepairTechPool pool;
    private final EventBus eventBus;
    private final Map<RepairTech, Station> activeRepairs = new HashMap<>();
    private final int REPAIR_DURATION = 3;      //placeholder
    private final Map<Station, Integer> remaining = new HashMap<>();
    @Getter
    private final OutageStats outageStats = new OutageStats();

    public RepairManager(RepairTechPool pool, EventBus eventBus) {
        this.pool = pool;
        this.eventBus = eventBus;
        eventBus.register(EventType.BREAKDOWN, this);
    }

    @Override
    public void handleEvent(Event event) {
        Station station = (Station) event.getSource();
        if (!repairQueue.contains(station)) {
            repairQueue.add(station);
            outageStats.recordBreakdown();
            log.info("%s added to repair queue.".formatted(station.getName()));
        }
    }

    @Override
    public void onTick(SimulationContext context) {
        //while techs free and machines need fixing, start new repairs
        while (pool.hasAvailable() && !repairQueue.isEmpty()) {
            Station m = repairQueue.poll();
            RepairTech tech = pool.acquireTech();
            activeRepairs.put(tech, m);
            remaining.put(m, REPAIR_DURATION);
            publishEvent(EventType.REPAIR_STARTED, m);
        }
        outageStats.recordWaitTime(repairQueue.size());
        //iterate through all active repairs
        Iterator<Map.Entry<RepairTech, Station>> it = activeRepairs.entrySet().iterator();
        while (it.hasNext()) {
            var entry = it.next();
            RepairTech tech = entry.getKey();
            Station s = entry.getValue();

            int ticks = remaining.get(s) - 1;
            if(ticks > 0){
                remaining.put(s, ticks);
            }
            else {
                s.repair();
                remaining.remove(s);
                outageStats.recordOutageDuration(REPAIR_DURATION);
                publishEvent(EventType.REPAIR_FINISHED, s);
                it.remove();
                pool.releaseTech(tech);
            }
        }
    }

    private void publishEvent(EventType type, Station machine) {
        if(eventBus != null)
            eventBus.publish(new Event(type, machine, machine.getPriority()));
    }
}