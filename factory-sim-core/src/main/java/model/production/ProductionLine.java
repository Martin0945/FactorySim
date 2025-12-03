package model.production;

import lombok.Getter;
import model.devices.Station;
import model.devices.Workforce;
import model.material.Material;
import simulation.Simulatable;
import simulation.SimulationContext;

import java.util.ArrayList;
import java.util.List;

public class ProductionLine implements Simulatable {
    private String name;
    private List<Workforce> workforces;
    private final List<Material> buffers;
    @Getter
    private final List<Material> outputs = new ArrayList<>();

    public ProductionLine(String name, List<Workforce> workforces) {
        this.name = name;
        this.workforces = workforces;
        this.buffers = new ArrayList<>();
        workforces.forEach(w -> buffers.add(null));
    }

    //put material into first station buffer
    public void addInput(Material material) {
        buffers.set(0, material);
    }

    @Override
    public void onTick(SimulationContext context) {
        for (int i = workforces.size() - 1; i >= 0; i--) {
            Material current = buffers.get(i);
            if (current == null) continue;

            //process current material
            Material processed = workforces.get(i).process(current);
            //if processing failed for whatever reason
            if (processed == null || processed == current) {
                buffers.set(i, current);
                continue;
            }
            //if this is last station, store output
            if (i == workforces.size() - 1) {
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
