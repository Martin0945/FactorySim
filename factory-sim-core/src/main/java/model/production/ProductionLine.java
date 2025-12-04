package model.production;

import lombok.Getter;
import lombok.extern.java.Log;
import model.devices.Workforce;
import model.devices.machine.Machine;
import model.material.Material;
import model.material.MaterialType;
import simulation.Simulatable;
import simulation.SimulationContext;

import java.util.ArrayList;
import java.util.List;

@Log
public class ProductionLine implements Simulatable {
    private String name;
    private List<Workforce> workforces;

    private final List<Material> lineStorage = new ArrayList<>();
    private final MaterialType targetOutput;

    @Getter
    private final List<Material> outputs = new ArrayList<>();

    public ProductionLine(String name, List<Workforce> workforces, MaterialType targetOutput) {
        this.name = name;
        this.workforces = workforces;
        this.targetOutput = targetOutput;
    }

    public void addInput(Material material) {
        lineStorage.add(material);
        log.info(name + ": Added input " + material.getType());
    }

    @Override
    public void onTick(SimulationContext context) {
        workforces.forEach(Workforce::tick);

        for (Workforce w : workforces) {
            Material product = w.takeOutput();

            if (product != null) {
                if (product.getType() == targetOutput) {
                    outputs.add(product);
                    log.info("ProductionLine %s FINISHED: %s".formatted(this.name, product.getType()));
                    //event
                } else {
                    lineStorage.add(product);
                    log.info(name + ": Machine " + w.getName() + " produced " + product.getType() + " -> returned to storage.");
                }
            }
        }

        for (Workforce w : workforces) {
            boolean startedWorking = w.acceptInput(lineStorage);

            if (startedWorking) {
                log.info(name + ": Machine " + w.getName() + " took ingredients and started working.");
            }
        }
    }

    public int getLineStorageCount() {
        return lineStorage.size();
    }
}