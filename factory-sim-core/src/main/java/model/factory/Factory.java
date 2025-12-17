package model.factory;

import lombok.Getter;
import model.inspection.Visitor;
import model.production.ProductionLine;
import simulation.Simulatable;
import simulation.SimulationContext;

import java.util.List;

public class Factory implements Simulatable {
    @Getter
    private final String name;
    private final List<ProductionLine> productionLines;

    public Factory(String factoryName, List<ProductionLine> lines) {
        this.name = factoryName;
        this.productionLines = lines;
    }

    /**
     * Sends the simulation tick further to all production lines in this factory.
     */
    @Override
    public void onTick(SimulationContext context) {
        productionLines.forEach(pl -> pl.onTick(context));
    }

    /**
     * Accepts a visitor and passes it down to all production lines recursively.
     */
    public void acceptVisitor(Visitor visitor){
        visitor.visit(this);
        productionLines.forEach(pl -> pl.acceptVisitor(visitor));
    }

}
