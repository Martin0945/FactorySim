package app.service;

import app.dto.*;
import event.Event;
import event.EventBus;
import model.devices.Station;
import model.material.MaterialType;
import model.production.*;
import org.springframework.http.MediaType;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;
import simulation.*;
import org.springframework.stereotype.Service;


import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class SimulationService {

    private final SimulationEngine engine;
    private final List<ProductionLine> productionLines;
    private SseEmitter emitter;
    private final SupplyManager supplyManager;
    private final RepairManager repairManager;


    public SimulationService(JsonSimulatorLoader jsonLoader) {
        SimulationBuilder builder = new SimulationBuilder();


        //JSON loader

        //jsonLoader.loadConfig(builder, "factory_config.json");

        //JSON loader end


        //class loader

        builder
            .addProductionLine("Line Scooter",List.of(
                builder.createMachine("LaserCutter"),
                builder.createMachine("PlasticMolder"),
                builder.createRobot("AssemblyRobotSC"),
                builder.createRobot("PaintRobotSC"),
                builder.createWorker("EUI"),
                builder.createWorker("WheelAssembler"))
                    , MaterialType.PAINTED_SCOOTER).

            addProductionLine("Line Phone", List.of(
                builder.createMachine("GlassCutter"),
                builder.createMachine("CaseMaker"),
                builder.createRobot("AssemblyRobotPhone"),
                builder.createRobot("PaintRobotPhone"),
                builder.createWorker("EUI")),
                    MaterialType.PAINTED_PHONE).

            addRepairTech("Bob")
            .addRepairTech("Job");

        //class loader end

        SimulationRecord simulationRecord = builder.build("Factory one");

        this.engine = simulationRecord.engine();
        this.supplyManager = simulationRecord.supplyManager();
        this.repairManager = simulationRecord.repairManager();
        this.productionLines = simulationRecord.lines();
        EventBus eventBus = simulationRecord.eventBus();


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
        String report = productionLines.stream()
                .map(line -> String.format("%s outputs: %d, Storage: %d",
                        line.getName(), line.getOutputs().size(), line.getLineStorageCount()))
                .collect(Collectors.joining("\n"));
        return String.format("Tick %d done.%n%s", engine.getCurrentTick(), report);
    }

    public FactoryStateReport getFactoryState() {
        FactoryConfigReport config = FactoryConfigReport.fromProductionLines(productionLines);
        ConsumptionReport consumption = ConsumptionReport.fromProductionLines(productionLines);
        OutagesReport outages = OutagesReport.fromStats(repairManager.getOutageStats());

        return new FactoryStateReport(config, consumption, outages);
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
