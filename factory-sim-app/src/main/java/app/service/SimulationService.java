package app.service;

import app.dto.*;
import event.Event;
import event.EventBus;
import event.EventSource;
import model.material.MaterialType;
import model.production.*;
import org.springframework.http.MediaType;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;
import simulation.*;
import org.springframework.stereotype.Service;
import simulation.repair.RepairManager;


import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class SimulationService {

    private final SimulationEngine engine;
    private final EventBus eventBus;
    private final List<ProductionLine> productionLines;
    private SseEmitter emitter;
    private final SupplyManager supplyManager;
    private final RepairManager repairManager;

    /**
     * Initializes the simulation service.
     * Sets up the factory scenario using the SimulationBuilder and registers the UI event listener.
     */
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
        this.eventBus = simulationRecord.eventBus();


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

        EventSource src = event.getSource();
        String sourceName = src.getName();
        String className = src.getClass().getSimpleName();

        String message = String.format("[%s] %s sent event [%s]", className, sourceName,
                event.getType().name());
        payload.put("message", message);
        payload.put("type", event.getType().name());
        payload.put("sourceClass", className);
        payload.put("sourceName", sourceName);
        payload.put("priority", event.getPriority());

        return payload;
    }

    /**
     * Advances the simulation by one tick.
     * @return A simple text report of the tick result.
     */
    public String tick() {
        engine.runTick();
        String report = productionLines.stream()
                .map(line -> String.format("%s outputs: %d, Storage: %d",
                        line.getName(), line.getOutputs().size(), line.getLineStorageCount()))
                .collect(Collectors.joining("\n"));
        return String.format("Tick %d done.%n%s", engine.getCurrentTick(), report);
    }

    /**
     * Collects data from various managers to create a comprehensive snapshot of the factory state.
     * Used by the frontend to render the dashboard.
     */
    public FactoryStateReport getFactoryState() {
        FactoryConfigReport config = FactoryConfigReport.fromProductionLines(productionLines);
        ConsumptionReport consumption = ConsumptionReport.fromProductionLines(productionLines);
        OutagesReport outages = OutagesReport.fromStats(repairManager.getOutageStats());
        EventReport events = buildEventReport();

        return new FactoryStateReport(config, events, consumption, outages);
    }

    private EventReport buildEventReport() {
        return new EventReport(
                eventBus.getEventStats()
                        .snapshot()
                        .entrySet()
                        .stream()
                        .collect(Collectors.toMap(
                                e -> e.getKey().name(),
                                Map.Entry::getValue
                        ))
        );
    }

    /**
     * Registers a new SSE (Server-Sent Events) client.
     */
    public SseEmitter createEventStream() {
        SseEmitter newEmitter = new SseEmitter(0L);
        this.emitter = newEmitter;
        newEmitter.onCompletion(() -> this.emitter = null);
        newEmitter.onTimeout(() -> this.emitter = null);
        newEmitter.onError((err) -> this.emitter = null);

        return newEmitter;
    }
}
