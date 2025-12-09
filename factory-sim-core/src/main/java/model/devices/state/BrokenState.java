package model.devices.state;

import event.Event;
import event.EventType;
import lombok.extern.java.Log;
import model.devices.Station;
import model.devices.Workforce;
import model.devices.human.Worker;
import model.devices.machine.Machine;
import model.material.Material;

import java.util.List;

@Log
public class BrokenState implements State {

    @Override
    public State checkWear(Station station) {
        log.info("Machine already broken");
        return this;
    }

    @Override
    public Material process(Machine machine, Material material) {
        //cannot change
        return material;
    }

    @Override
    public void increaseWear(Station station) {
        log.info("Machine already broken, cannot increase wear");
        //do nothing
    }

    @Override
    public String getState() {
        return "Broken";
    }

    public State acceptInput(Workforce workforce, List<Material> materials) {
        log.info("Machine is broken, cannot accept input");
        return this;
    }

    @Override
    public State handleTick(Station station) {
        //repair logic
        return this;
    }

    @Override
    public State handleTick(Worker worker) {
        return new IdleState();
    }


}
