package app.service;

import model.factory.Factory;
import model.material.Material;
import model.production.ProductionLine;
import model.production.Station;
import model.devices.human.Worker;
import simulation.SimulationEngine;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SimulationService {

    private final SimulationEngine engine;
    private final ProductionLine line;

    public SimulationService() {
        //1 line of 3 workers
        List<Station> stations = List.of(
                new Worker("W1"),
                new Worker("W2"),
                new Worker("W3")
        );

        this.line = new ProductionLine("Line A", stations);
        Factory factory = new Factory("Factory one", List.of(line));
        this.engine = new SimulationEngine(factory);

        //add input some
        line.addInput(new Material("Iron", 1));
    }

    public String tick() {
        engine.runTick();
        return "Tick " + engine.getCurrentTick() + " done. Outputs: " + line.getOutputs().size();
    }
}
