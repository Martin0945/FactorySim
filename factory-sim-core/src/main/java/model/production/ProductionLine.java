package model.production;

import lombok.Getter;
import lombok.extern.java.Log;
import model.devices.Workforce;
import model.inspection.Visitor;
import model.material.Material;
import model.material.MaterialType;
import simulation.Simulatable;
import simulation.SimulationContext;
import java.util.ArrayList;
import java.util.List;

@Log
@Getter

public class ProductionLine implements Simulatable {
    private final String name;
    private final List<Workforce> workforces;
    private final List<Material> lineStorage = new ArrayList<>();
    private final MaterialType targetOutput;
    private final List<Material> outputs = new ArrayList<>();

    private double materialCosts;
    private double revenue;

    public ProductionLine(String name, List<Workforce> workforces, MaterialType targetOutput) {
        this.name = name;
        this.workforces = workforces;
        this.targetOutput = targetOutput;
    }

    /**
     * Adds a raw material to the line's shared internal storage and updates total material costs.
     * These materials are later picked up by workforces.
     */
    public void addInput(Material material) {
        lineStorage.add(material);

        materialCosts += material.getValue();
    }

    /**
     * Executes one simulation step for the entire line.
     * 1. Ticks all workforces.
     * 2. Collects finished items (storing intermediate ones back to line storage, final ones to outputs).
     * 3. Attempts to distribute waiting materials from storage to idle workforces.
     */
    @Override
    public void onTick(SimulationContext context) {
        workforces.forEach(Workforce::tick);

        for (Workforce w : workforces) {
            Material product = w.takeOutput();

            //product finished
            if (product != null) {
                //final product
                if (product.getType() == targetOutput) {
                    outputs.add(product);
                    revenue += product.getValue();
                    log.info("%s FINISHED: %s".formatted(this.name, product.getType()));

                //semi finished product (material)
                } else {
                    log.info("%s: %s finished product: %s".formatted(name, w.getName(), product.getType()));
                    lineStorage.add(product);
                }
            }
        }
        for (Workforce w : workforces) {
            //check for every workforce if is able to accept input and start working
            boolean startedWorking = w.acceptInput(lineStorage);

            if (startedWorking) {
                log.info("%s: %s took ingredients and started working.".formatted(name, w.getName()));
            }
        }
    }


    public int getLineStorageCount() {
        return lineStorage.size();
    }

    /**
     * Allows inspection of the line and recursively visits all contained workforces.
     */
    public void acceptVisitor(Visitor visitor) {
        visitor.visit(this);
        workforces.forEach(w -> w.acceptVisitor(visitor));
    }

}