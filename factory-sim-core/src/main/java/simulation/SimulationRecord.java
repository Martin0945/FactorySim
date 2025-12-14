package simulation;

import event.EventBus;
import model.production.ProductionLine;
import model.production.SupplyManager;

import java.util.List;

public record SimulationRecord(
        SimulationEngine engine,
        SupplyManager supplyManager,
        RepairManager repairManager,
        EventBus eventBus,
        List<ProductionLine> lines) {

}
