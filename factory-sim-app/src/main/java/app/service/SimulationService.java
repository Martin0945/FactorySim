package app.service;

import event.Event;
import event.EventBus;
import model.devices.Station;
import model.devices.Workforce;
import model.devices.human.Worker;
import model.devices.human.WorkerFactory;
import model.devices.machine.*;
import model.devices.robot.Robot;
import model.devices.robot.RobotFactory;
import model.factory.Factory;
import model.inspection.VisitorManager;
import model.material.Material;
import model.material.MaterialType;
import model.production.*;
import org.springframework.http.MediaType;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;
import simulation.*;
import org.springframework.stereotype.Service;


import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class SimulationService {

    private final SimulationEngine engine;
    private final ProductionLine lineA;
    private final ProductionLine lineB;
    private SseEmitter emitter;
    private final SupplyManager supplyManager;


    public SimulationService() {
        EventBus eventBus = new EventBus();
        MachineFactory machineFactory = new MachineFactory(eventBus);
        RobotFactory robotFactory = new RobotFactory(eventBus);
        WorkerFactory workerFactory = new WorkerFactory(eventBus);


        List<Workforce> workforcesA = List.of(
                machineFactory.createMachine("LaserCutter"),
                machineFactory.createMachine("PlasticMolder"),
                robotFactory.createRobot("AssemblyRobotSC"),
                robotFactory.createRobot("PaintRobotSC"),
                workerFactory.createWorker("EUI"),
                workerFactory.createWorker("WheelAssembler")
        );

        List<Workforce> workforcesB = List.of(
                machineFactory.createMachine("GlassCutter"),
                machineFactory.createMachine("CaseMaker"),
                robotFactory.createRobot("AssemblyRobotPhone"),
                robotFactory.createRobot("PaintRobotPhone"),
                workerFactory.createWorker("EUI")
        );

        this.lineA = new ProductionLine("Line Scooter", workforcesA, MaterialType.PAINTED_SCOOTER);
        this.lineB = new ProductionLine("Line Phone", workforcesB, MaterialType.PAINTED_PHONE);

        List<ProductionLine> productionLines = List.of(lineA, lineB);

        Factory factory = new Factory("Factory one", productionLines);

        new VisitorManager(factory, eventBus);

        RepairTechPool pool = new RepairTechPool(
                List.of(
                        new RepairTech("Bob"),
                        new RepairTech("Job")
                )
        );
        RepairManager repairManager = new RepairManager(pool, eventBus);

        supplyManager = new SupplyManager(eventBus, productionLines);
        this.engine = new SimulationEngine(factory, repairManager, eventBus);
        supplyManager.initializeLines();


        eventBus.setUiListener(event -> {
            if (emitter == null) return;
            try {
                emitter.send(buildEventPayload(event), MediaType.APPLICATION_JSON);
            } catch (Exception e) {
                emitter = null; // disconnected client
            }
        });


    }

    private Map<String, Object> buildEventPayload(Event event) {
        Map<String, Object> payload = new HashMap<>();
        String sourceName = "";
        Object src = event.getSource();
        if (src instanceof Station) {
            sourceName = ((Station) src).getName();
        } else {
            sourceName = src.getClass().getSimpleName();
        }
        String message = String.format("[%s] %s sent event [%s]", src.getClass().getSimpleName(), sourceName, event.getType().name());
        payload.put("message", message);
        payload.put("type", event.getType().name());
        payload.put("sourceClass", src.getClass().getSimpleName());
        payload.put("sourceName", sourceName);
        payload.put("priority", event.getPriority());
        return payload;
    }

    public String tick() {
        engine.runTick();
        return String.format("Tick %d done.%n%s outputs: %d %s size: %d.%n%s outputs: %d, %s size: %d",
                engine.getCurrentTick(),
                lineA.getName(),
                lineA.getOutputs().size(),
                lineA.getName(),
                lineA.getLineStorageCount(),
                lineB.getName(),
                lineB.getOutputs().size(),
                lineB.getName(),
                lineB.getLineStorageCount());
    }

    public Object getFactoryState() {
        return null; //placeholder
    }

    public SseEmitter createEventStream() {
        SseEmitter newEmitter = new SseEmitter(0L);
        this.emitter = newEmitter;
        newEmitter.onCompletion(() -> this.emitter = null);
        newEmitter.onTimeout(() -> this.emitter = null);
        newEmitter.onError((err) -> this.emitter = null);

        return newEmitter;
    }

}
