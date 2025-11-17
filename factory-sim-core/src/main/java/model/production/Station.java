package model.production;

import model.material.Material;
import simulation.SimulationContext;
import simulation.Simulatable;

public abstract class Station implements Simulatable{

    private String id;

    public Station(String id) {
        this.id = id;
    }

    @Override
    public void onTick(SimulationContext context) {
    }

    public abstract Material process(Material input);

    public String getId() {
        return id;
    }
}