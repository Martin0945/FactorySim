package model.devices.state;

import event.Event;
import event.EventType;
import model.devices.machine.Machine;
import model.material.Material;

public interface State {
    Material process(Machine machine, Material material);
    void increaseWear(Machine machine);
    String getState();

    static void CheckWear(Machine machine) {
        if (machine.getWear() >= machine.getBreakThreshold()) {
            machine.setState(new BrokenState());
            if (machine.getEventBus() != null) {
                machine.getEventBus().publish(new Event(EventType.BREAKDOWN, machine, machine.getPriority()));
            }
            System.out.println("Machine " + machine.getName() + " broke, wear " + machine.getWear() + "%");
        }
    }
}
