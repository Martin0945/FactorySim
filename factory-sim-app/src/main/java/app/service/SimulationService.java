package app.service;

import event.EventBus;
import model.devices.machine.Machine;
import model.factory.Factory;
import model.material.Material;
import model.production.*;
import simulation.*;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SimulationService {

    private final SimulationEngine engine;
    private final ProductionLine line;

    public SimulationService() {
        EventBus eventBus = new EventBus();
        List<Station> stations = List.of(
                new Machine("M1", 3, eventBus),
                new Machine("M2", 2, eventBus),
                new Machine("M3", 1, eventBus)
        );

        this.line = new ProductionLine("Line A", stations);
        Factory factory = new Factory("Factory one", List.of(line));


        RepairTechPool pool = new RepairTechPool(
                List.of(
                        new RepairTech("Bob"),
                        new RepairTech("Job")
                )
        );

        RepairManager repairManager = new RepairManager(pool, eventBus);

        this.engine = new SimulationEngine(factory, repairManager, eventBus);

        //add input some
        line.addInput(new Material("Iron", 1));
    }

    public String tick() {
        engine.runTick();
        return "Tick " + engine.getCurrentTick() + " done. Outputs: " + line.getOutputs().size();
    }
}
