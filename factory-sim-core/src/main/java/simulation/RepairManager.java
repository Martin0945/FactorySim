package simulation;

import event.*;
import event.EventListener;
import model.devices.machine.Machine;

import java.util.*;

public class RepairManager implements EventListener, Simulatable {
    private final PriorityQueue<Machine> repairQueue = new PriorityQueue<>(Comparator.comparingInt(Machine::getPriority).reversed());

    private final RepairTechPool pool;
    private final EventBus eventBus;
    private final Map<RepairTech, Machine> activeRepairs = new HashMap<>();
    private final int REPAIR_DURATION = 3;      //placeholder
    private final Map<Machine, Integer> remaining = new HashMap<>();

    public RepairManager(RepairTechPool pool, EventBus eventBus) {
        this.pool = pool;
        this.eventBus = eventBus;
        eventBus.register(EventType.BREAKDOWN, this);
    }

    @Override
    public void handleEvent(Event event) {
        Machine machine = (Machine) event.getSource();
        if (!repairQueue.contains(machine)) {
            repairQueue.add(machine);
            System.out.println("added to repair queue: " + machine.getName());
        }
    }

    @Override
    public void onTick(SimulationContext context) {
        //while techs free and machines need fixing, start new repairs
        while (pool.hasAvailable() && !repairQueue.isEmpty()) {
            Machine m = repairQueue.poll();
            RepairTech tech = pool.acquireTech();
            activeRepairs.put(tech, m);
            remaining.put(m, REPAIR_DURATION);
            publishEvent(EventType.REPAIR_STARTED, m);
        }
        //iterate through all active repairs
        Iterator<Map.Entry<RepairTech, Machine>> it = activeRepairs.entrySet().iterator();
        while (it.hasNext()) {
            var entry = it.next();
            RepairTech tech = entry.getKey();
            Machine m = entry.getValue();

            int ticks = remaining.get(m) - 1;
            if(ticks > 0){
                remaining.put(m, ticks);
            }
            else {
                m.repair();
                remaining.remove(m);
                publishEvent(EventType.REPAIR_FINISHED, m);
                it.remove();
                pool.releaseTech(tech);
            }
        }
    }

    private void publishEvent(EventType type, Machine machine) {
        if(eventBus != null)
            eventBus.publish(new Event(type, machine, machine.getPriority()));
    }
}