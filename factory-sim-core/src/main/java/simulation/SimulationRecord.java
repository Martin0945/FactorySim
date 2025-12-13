package simulation;

import event.EventBus;
import model.production.ProductionLine;
import model.production.SupplyManager;

import java.util.List;

public record SimulationRecord(
        SimulationEngine engine,
       SupplyManager supplyManager,
       EventBus eventBus,
       List<ProductionLine> lines) {

}
