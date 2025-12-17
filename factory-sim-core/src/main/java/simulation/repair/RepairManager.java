package simulation.repair;

import event.*;
import event.EventListener;
import lombok.Getter;
import lombok.extern.java.Log;
import model.devices.Station;
import simulation.Simulatable;
import simulation.SimulationContext;
import simulation.stats.OutageStats;

import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

@Log

public class RepairManager implements EventListener, Simulatable {
    private final PriorityQueue<Station> repairQueue = new PriorityQueue<>
            (Comparator.comparingInt(Station::getPriority).reversed());

    private final RepairTechPool pool;
    private final EventBus eventBus;
    private final Map<RepairTech, Station> activeRepairs = new HashMap<>();

    private final int MIN_REPAIR_DURATION = 2;
    private final int MAX_REPAIR_DURATION = 5;

    private final Map<Station, Integer> totalRepairTimes = new HashMap<>();
    private final Map<Station, Integer> remaining = new HashMap<>();

    @Getter
    private final OutageStats outageStats = new OutageStats();

    /**
     * Initializes the manager and subscribes to BREAKDOWN events.
     */
    public RepairManager(RepairTechPool pool, EventBus eventBus) {
        this.pool = pool;
        this.eventBus = eventBus;
        eventBus.register(EventType.BREAKDOWN, this);
    }

    /**
     * Enqueues a broken station for repair when a BREAKDOWN event occurs.
     * Prevents duplicate entries in the repair queue.
     */
    @Override
    public void handleEvent(Event event) {
        Station station = (Station) event.getSource();
        if (!repairQueue.contains(station)) {
            repairQueue.add(station);
            outageStats.recordBreakdown();
            log.info("%s added to repair queue.".formatted(station.getName()));
        }
    }

    /**
     * Main repair loop executed every tick.
     * Controls the repair process by assigning techs and progressing active repairs.
     */
    @Override
    public void onTick(SimulationContext context) {
        assignTech();

        outageStats.recordWaitTime(repairQueue.size());

        //iterate through all active repairs
        progressRepairs();
    }

    /**
     * Iterates through all active repairs, decrements their remaining time,
     * and finalizes the repair if the time reaches zero. Also records stats
     * for reports.
     */
    private void progressRepairs() {
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

                int actualDuration = totalRepairTimes.remove(s);
                outageStats.recordOutageDuration(actualDuration);

                publishEvent(EventType.REPAIR_FINISHED, s);
                it.remove();
                pool.releaseTech(tech);
            }
        }
    }

    /**
     * Checks if there are available technicians and broken stations.
     * If so, assigns techs to stations and schedules the repair duration.
     */
    private void assignTech() {
        //while techs free and machines need fixing, start new repairs
        while (pool.hasAvailable() && !repairQueue.isEmpty()) {

            int duration = ThreadLocalRandom.current().nextInt(MIN_REPAIR_DURATION, MAX_REPAIR_DURATION + 1);

            Station station = repairQueue.poll();
            RepairTech tech = pool.acquireTech();
            activeRepairs.put(tech, station);
            remaining.put(station, duration);
            totalRepairTimes.put(station, duration);

            publishEvent(EventType.REPAIR_STARTED, station);
        }
    }

    private void publishEvent(EventType type, Station machine) {
        if(eventBus != null)
            eventBus.publish(new Event(type, machine, machine.getPriority()));
    }
}