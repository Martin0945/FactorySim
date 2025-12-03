package app.service;

import event.EventBus;
import model.devices.Workforce;
import model.devices.human.EUIWorker;
import model.devices.human.WheelAssembler;
import model.devices.human.Worker;
import model.devices.human.WorkerType;
import model.devices.machine.*;
import model.devices.robot.AssemblyRobot;
import model.devices.robot.PaintRobot;
import model.devices.robot.Robot;
import model.devices.robot.RobotType;
import model.factory.Factory;
import model.material.Material;
import model.material.MaterialType;
import model.production.*;
import org.springframework.http.MediaType;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;
import simulation.*;
import org.springframework.stereotype.Service;


import java.util.List;

@Service
public class SimulationService {

    private final SimulationEngine engine;
    private final ProductionLine lineA;
    private final ProductionLine lineB;
    private SseEmitter emitter;

    public SimulationService() {
        EventBus eventBus = new EventBus();
        MachineFactory machineFactory = new MachineFactory(eventBus);
        List<Workforce> workforcesA = List.of(
                machineFactory.createMachine("LaserCutter"),
                machineFactory.createMachine("PlasticMolder"),
                new AssemblyRobot(),
                new PaintRobot(),
                new EUIWorker(),
                new WheelAssembler()
        );

        List<Workforce> workforcesB = List.of(
                machineFactory.createMachine("GlassCutter"),
                machineFactory.createMachine("CaseMaker"),
                new AssemblyRobot(),
                new PaintRobot(),
                new EUIWorker()
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


        eventBus.setUiListener(event -> {
            try {
                emitter.send(event, MediaType.APPLICATION_JSON);
            } catch (Exception e) {
                emitter = null; //disconnected client
            }
        });
    }

    public String tick() {
        engine.runTick();
        return "Tick " + engine.getCurrentTick() + " done. Outputs: " + lineA.getOutputs().size();
    }

    public Object getFactoryState() {
        return null; //placeholder
    }

    public SseEmitter createEventStream() {
        emitter = new SseEmitter(0L);
        emitter.onCompletion(() -> emitter = null);
        emitter.onTimeout(() -> emitter = null);
        return emitter;
    }
}
