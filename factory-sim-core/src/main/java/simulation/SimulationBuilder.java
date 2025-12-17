package simulation;

import event.EventBus;
import model.devices.Workforce;
import model.devices.human.WorkerFactory;
import model.devices.machine.MachineFactory;
import model.devices.robot.RobotFactory;
import model.factory.Factory;
import model.inspection.VisitorManager;
import model.material.MaterialType;
import model.production.ProductionLine;
import model.production.SupplyManager;
import simulation.repair.RepairManager;
import simulation.repair.RepairTech;
import simulation.repair.RepairTechPool;

import java.util.ArrayList;
import java.util.List;

public class SimulationBuilder {
    private final EventBus eventBus;
    private final MachineFactory machineFactory;
    private final RobotFactory robotFactory;
    private final WorkerFactory workerFactory;

    private final List<ProductionLine> lines = new ArrayList<>();
    private final List<RepairTech> repairTechs = new ArrayList<>();

    public SimulationBuilder(){
        this.eventBus = new EventBus();
        this.machineFactory = new MachineFactory(eventBus);
        this.robotFactory = new RobotFactory(eventBus);
        this.workerFactory = new WorkerFactory(eventBus);
    }

    /**
     * Adds a configured production line to the simulation.
     */
    public SimulationBuilder addProductionLine(String name, List<Workforce> workforces, MaterialType targetOutput) {
        ProductionLine newProdLine = new ProductionLine(name, workforces, targetOutput);
        this.lines.add(newProdLine);
        return this;
    }

    /**
     * Registers a named technician to the repair pool.
     */
    public SimulationBuilder addRepairTech(String name){
        RepairTech newRepairTech = new RepairTech(name);
        repairTechs.add(newRepairTech);
        return this;
    }

    /**
     * Delegate method for creating machines via the internal MachineFactory.
     */
    public Workforce createMachine(String type){
        return machineFactory.createMachine(type);
    }

    /**
     * Delegate method for creating robots via the internal RobotFactory.
     */
    public Workforce createRobot(String type){
        return robotFactory.createRobot(type);
    }

    /**
     * Delegate method for creating workers via the internal WorkerFactory.
     */
    public Workforce createWorker(String type){
        return workerFactory.createWorker(type);
    }
    
    /**
     * Finalizes configuration and initializes the engine with all dependencies.
     * Sets up the VisitorManager, RepairManager, and SupplyManager.
     * @param name The name of the Factory entity.
     * @return An immutable record containing the constructed simulation components.
     */
    public SimulationRecord build(String name){
        Factory factory = new Factory(name, lines);

        new VisitorManager(factory, eventBus);

        RepairTechPool pool = new RepairTechPool(repairTechs);
        RepairManager repairManager = new RepairManager(pool, eventBus);

        SupplyManager supplyManager = new SupplyManager(eventBus, lines);
        supplyManager.initializeLines();

        SimulationEngine engine = new SimulationEngine(factory, repairManager, eventBus);

        return new SimulationRecord(engine, supplyManager, repairManager,eventBus, lines);
    }

}
