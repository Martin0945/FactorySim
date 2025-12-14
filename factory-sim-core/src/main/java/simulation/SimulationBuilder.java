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

    public SimulationBuilder addProductionLine(String name, List<Workforce> workforces, MaterialType targetOutput) {
        ProductionLine newProdLine = new ProductionLine(name, workforces, targetOutput);
        this.lines.add(newProdLine);
        return this;
    }

    public SimulationBuilder addRepairTech(String name){
        RepairTech newRepairTech = new RepairTech(name);
        repairTechs.add(newRepairTech);
        return this;
    }

    public Workforce createMachine(String type){
        return machineFactory.createMachine(type);
    }
    public Workforce createRobot(String type){
        return robotFactory.createRobot(type);
    }
    public Workforce createWorker(String type){
        return workerFactory.createWorker(type);
    }

    public SimulationRecord build(){
        Factory factory = new Factory("Factory one", lines);

        new VisitorManager(factory, eventBus);

        RepairTechPool pool = new RepairTechPool(repairTechs);
        RepairManager repairManager = new RepairManager(pool, eventBus);

        SupplyManager supplyManager = new SupplyManager(eventBus, lines);
        supplyManager.initializeLines();

        SimulationEngine engine = new SimulationEngine(factory, repairManager, eventBus);

        return new SimulationRecord(engine, supplyManager, repairManager,eventBus, lines);
    }

}
