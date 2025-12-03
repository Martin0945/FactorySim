package model.devices.state;

import model.devices.machine.Machine;
import model.material.Material;

public class BrokenState implements State {

    @Override
    public Material process(Machine machine, Material material) {
        //cannot change
        return material;
    }

    @Override
    public void increaseWear(Machine machine) {
        //do nothing
    }

    @Override
    public String getState() {
        return "Broken";
    }
}
