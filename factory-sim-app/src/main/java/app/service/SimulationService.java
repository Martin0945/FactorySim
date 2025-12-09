package app.service;

import event.EventBus;
import model.devices.Workforce;
import model.devices.human.Worker;
import model.devices.human.WorkerFactory;
import model.devices.machine.*;
import model.devices.robot.Robot;
import model.devices.robot.RobotFactory;
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
    //private final ProductionLine lineB;
    private SseEmitter emitter;

    public SimulationService() {
        EventBus eventBus = new EventBus();
        MachineFactory machineFactory = new MachineFactory(eventBus);
        RobotFactory robotFactory = new RobotFactory(eventBus);
        WorkerFactory workerFactory = new WorkerFactory(eventBus);

        List<Workforce> workforcesA = List.of(
                machineFactory.createMachine("LaserCutter"),
                machineFactory.createMachine("PlasticMolder"),
                robotFactory.createRobot("AssemblyRobot"),
                robotFactory.createRobot("PaintRobotSC"),
                workerFactory.createWorker("EUI"),
                workerFactory.createWorker("WheelAssembler")
        );

//        List<Workforce> workforcesB = List.of(
//                machineFactory.createMachine("GlassCutter"),
//                machineFactory.createMachine("CaseMaker"),
//                robotFactory.createRobot("AssemblyRobot"),
//                robotFactory.createRobot("PaintRobotPhone"),
//                workerFactory.createWorker("EUI")
//                );

        this.lineA = new ProductionLine("Line Scooter", workforcesA, MaterialType.PAINTED_SCOOTER);
        //this.lineB = new ProductionLine("Line Phone", workforcesB, MaterialType.PAINTED_PHONE);

        Factory factory = new Factory("Factory one", List.of(lineA));


        RepairTechPool pool = new RepairTechPool(
                List.of(
                        new RepairTech("Bob"),
                        new RepairTech("Job")
                )
        );

        RepairManager repairManager = new RepairManager(pool, eventBus);

        this.engine = new SimulationEngine(factory, repairManager, eventBus);

        for(int i = 0; i < 6; i++){
            lineA.addInput(new Material(MaterialType.STEEL));
            lineA.addInput(new Material(MaterialType.PLASTIC));
            lineA.addInput(new Material(MaterialType.RUBBER));
            lineA.addInput(new Material(MaterialType.ELECTRONICS));
            lineA.addInput(new Material(MaterialType.PAINT));
        }

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
        return String.format("Tick %d done. LineA outputs: %d, Storage size: %d",
                engine.getCurrentTick(),
                lineA.getOutputs().size(),
                lineA.getLineStorageCount());
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
