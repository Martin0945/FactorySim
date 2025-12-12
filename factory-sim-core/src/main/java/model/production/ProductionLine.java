package model.production;

import lombok.Getter;
import lombok.extern.java.Log;
import model.devices.Station;
import model.devices.Workforce;
import model.devices.machine.Machine;
import model.inspection.Director;
import model.inspection.Inspector;
import model.inspection.Visitor;
import model.material.Material;
import model.material.MaterialType;
import simulation.Simulatable;
import simulation.SimulationContext;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Log
@Getter

public class ProductionLine implements Simulatable {
    private String name;
    private List<Workforce> workforces;

    private final List<Material> lineStorage = new ArrayList<>();
    private final MaterialType targetOutput;

    private final List<Material> outputs = new ArrayList<>();

    public ProductionLine(String name, List<Workforce> workforces, MaterialType targetOutput) {
        this.name = name;
        this.workforces = workforces;
        this.targetOutput = targetOutput;
    }

    public void addInput(Material material) {
        lineStorage.add(material);
//        log.info(name + ": Added input " + material.getType());
    }

    @Override
    public void onTick(SimulationContext context) {
        workforces.forEach(Workforce::tick);

        for (Workforce w : workforces) {
            Material product = w.takeOutput();

            if (product != null) {
                if (product.getType() == targetOutput) {
                    outputs.add(product);
                    log.info("%s FINISHED: %s".formatted(this.name, product.getType()));
                    //todo event
                } else {
                    log.info("%s: %s finished product: %s".formatted(name, w.getName(), product.getType()));
                    lineStorage.add(product);
                }
            }
        }

        for (Workforce w : workforces) {
            boolean startedWorking = w.acceptInput(lineStorage);

            if (startedWorking) {
                log.info("%s: %s took ingredients and started working.".formatted(name, w.getName()));
            }
        }
    }

    public int getLineStorageCount() {
        return lineStorage.size();
    }

    public void acceptVisitor(Visitor visitor) {
        visitor.visit(this);
        workforces.forEach(w -> w.acceptVisitor(visitor));
    }

}