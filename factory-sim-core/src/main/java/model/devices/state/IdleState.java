package model.devices.state;

import model.devices.machine.Machine;
import model.material.Material;

public class IdleState implements State {

    @Override
    public Material process(Machine machine, Material material) {
        increaseWear(machine);
        machine.setState(new WorkingState());   //turn on machine
        return material;
    }

    @Override
    public void increaseWear(Machine machine) {
        machine.setWear(machine.getWear() + machine.getWearPerTick() / 2);  //slower wear when idle
        State.CheckWear(machine);
    }


    @Override
    public String getState() {
        return "Idle";
    }
}
