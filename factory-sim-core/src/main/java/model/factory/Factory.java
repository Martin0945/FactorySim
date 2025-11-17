package model.factory;

import model.production.ProductionLine;
import simulation.Simulatable;
import simulation.SimulationContext;

import java.util.List;

public class Factory implements Simulatable {
    private String name;
    private List<ProductionLine> productionLines;

    public Factory(String name, List<ProductionLine> productionLines) {
        this.name = name;
        this.productionLines = productionLines;
    }

    @Override
    public void onTick(SimulationContext context) {
        productionLines.forEach(pl -> pl.onTick(context));
    }
}
