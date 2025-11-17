package model.devices.machine;

import model.production.Station;
import model.material.Material;

public class Machine extends Station {
    private boolean working = true;

    public Machine(String id) {
        super(id);
    }

    public boolean isWorking() {
        return working;
    }

    public void breakDown() {
        this.working = false;
    }

    public void repair() {
        this.working = true;
    }


    @Override
    public Material process(Material input) {
        return input;
    }
}