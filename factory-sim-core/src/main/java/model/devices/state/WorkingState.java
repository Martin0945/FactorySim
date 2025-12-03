package model.devices.state;

import model.devices.machine.Machine;
import model.material.Material;

public class WorkingState implements State {

    @Override
    public Material process(Machine machine, Material material) {
        increaseWear(machine);
        if(material.getType() == machine.getInput().getType()){
            //also probably check quantity here
            return machine.getOutput();
        }
        else{
            //probably some failure event here
            return material;
        }
    }

    @Override
    public void increaseWear(Machine machine) {
        machine.setWear(machine.getWear() + machine.getWearPerTick());
        State.CheckWear(machine);
    }

    @Override
    public String getState() {
        return "Working";
    }
}
