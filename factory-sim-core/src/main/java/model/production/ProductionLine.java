package model.production;

import lombok.Getter;
import model.material.Material;
import simulation.Simulatable;
import simulation.SimulationContext;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class ProductionLine implements Simulatable {
    private String name;
    private List<Station> stations;
    private final List<Material> buffers;
    @Getter
    private final List<Material> outputs = new ArrayList<>();

    public ProductionLine(String name, List<Station> stations) {
        this.name = name;
        this.stations = stations;
        this.buffers = new ArrayList<>();
        stations.forEach(s -> buffers.add(null));
    }

    //put material into first station buffer
    public void addInput(Material material) {
        buffers.set(0, material);
    }

    @Override
    public void onTick(SimulationContext context) {
        for (int i = stations.size() - 1; i >= 0; i--) {
            Material current = buffers.get(i);
            if (current == null) continue;
            //process current material
            Material processed = stations.get(i).process(current);

            //if this is last station, store output
            if (i == stations.size() - 1) {
                outputs.add(processed);
            }
            else {
                //move to next station if empty
                if (buffers.get(i + 1) == null) {
                    buffers.set(i + 1, processed);
                    buffers.set(i, null);
                }
                //else wait for next tick
                else {
                    buffers.set(i, processed);
                }
            }
        }
    }

}
