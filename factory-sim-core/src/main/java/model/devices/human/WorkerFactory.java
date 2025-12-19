package model.devices.human;

import event.EventBus;
import model.material.MaterialType;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class WorkerFactory {
    private final EventBus eventBus;
    private final Map<String, WorkerConfig> presets = new HashMap<>();

    public WorkerFactory(EventBus eventBus) {
        this.eventBus = eventBus;
        for (WorkerConfig cfg : WorkerConfig.values()) {
            presets.put(cfg.name, cfg);
        }
    }

    /**
     * Creates a new Worker instance with custom parameters, bypassing presets.
     *
     */
    public Worker createWorker(String name, List<MaterialType> recipe, MaterialType outputType,
                               int priority, int processDuration, int pay) {

        return new Worker(pay, priority, eventBus, outputType,processDuration,
                recipe, name);
    }

    /**
     * Creates a new Worker based on a registered preset name.
     * @throws IllegalArgumentException if the preset name is unknown.
     */
    public Worker createWorker(String presetName) {
        WorkerConfig cfg = presets.get(presetName);
        if (cfg == null) throw new IllegalArgumentException("Unknown preset: " + presetName);

        return new Worker(
                cfg.pay,
                cfg.priority,
                eventBus,
                cfg.outputType,
                cfg.processDuration,
                cfg.recipe,
                cfg.name
        );
    }

    /**
     * Registers a new worker configuration preset or overwrites an existing one.
     */
    public void registerPreset(String name, WorkerConfig cfg) {
        presets.put(name, cfg);
    }
}