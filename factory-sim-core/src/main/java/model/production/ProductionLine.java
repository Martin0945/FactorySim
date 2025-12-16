package model.production;

import lombok.Getter;
import lombok.extern.java.Log;
import model.devices.Workforce;
import model.economy.ConsumptionData;
import model.inspection.Visitor;
import model.material.Material;
import model.material.MaterialType;
import simulation.Simulatable;
import simulation.SimulationContext;
import simulation.prices.PriceList;
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
    private final PriceList priceList = new PriceList();
    private double materialCosts;
    private double revenue;

    public ProductionLine(String name, List<Workforce> workforces, MaterialType targetOutput) {
        this.name = name;
        this.workforces = workforces;
        this.targetOutput = targetOutput;
    }

    public void addInput(Material material) {
        lineStorage.add(material);

        double price = priceList.getMaterialPrice(material.getType());
        materialCosts += price;
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
                    double price = priceList.getMaterialPrice(product.getType());
                    revenue += price;
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