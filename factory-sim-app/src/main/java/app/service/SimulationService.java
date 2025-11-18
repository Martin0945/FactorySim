package app.service;

import event.EventBus;
import model.devices.Workforce;
import model.devices.human.Worker;
import model.devices.human.WorkerType;
import model.devices.machine.Machine;
import model.devices.machine.MachineType;
import model.devices.robot.Robot;
import model.devices.robot.RobotType;
import model.factory.Factory;
import model.material.Material;
import model.material.MaterialType;
import model.production.*;
import simulation.*;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SimulationService {

    private final SimulationEngine engine;
    private final ProductionLine lineA;
    private final ProductionLine lineB;

    public SimulationService() {
        EventBus eventBus = new EventBus();
        List<Workforce> workforcesA = List.of(
                new Machine(MachineType.LASER_CUTTER, 20, 1 ,  3, eventBus),
                new Machine(MachineType.PLASTIC_MOLDER, 15, 2, 2, eventBus),
                new Robot(RobotType.ASSEMBLY_ROBOT, 12),
                new Robot(RobotType.PAINT_STATION, 10),
                new Worker(WorkerType.WHEEL_ASSEMBLER, 300),
                new Worker (WorkerType.EUI, 350)
        );

        List<Workforce> workforcesB = List.of(
                new Machine(MachineType.GLASS_CUTTER, 10, 1 , 3, eventBus),
                new Machine(MachineType.CASE_MAKER, 18, 2, 2, eventBus),
                new Robot(RobotType.ASSEMBLY_ROBOT, 12),
                new Robot(RobotType.PAINT_STATION, 10),
                new Worker (WorkerType.EUI, 350)
        );

        this.lineA = new ProductionLine("Line A", workforcesA);
        this.lineB = new ProductionLine("Line B", workforcesB);

        Factory factory = new Factory("Factory one", List.of(lineA, lineB));


        RepairTechPool pool = new RepairTechPool(
                List.of(
                        new RepairTech("Bob"),
                        new RepairTech("Job")
                )
        );

        RepairManager repairManager = new RepairManager(pool, eventBus);

        this.engine = new SimulationEngine(factory, repairManager, eventBus);

        //add input some
        lineA.addInput(new Material(MaterialType.STEEL, 1));
        lineA.addInput(new Material(MaterialType.PLASTIC, 2));
        lineA.addInput(new Material(MaterialType.RUBBER, 1));
        lineA.addInput(new Material(MaterialType.ELECTRONICS, 1));
        lineA.addInput(new Material(MaterialType.PAINT, 1));


    }

    public String tick() {
        engine.runTick();
        return "Tick " + engine.getCurrentTick() + " done. Outputs: " + lineA.getOutputs().size();
    }
}
