package model.devices.machine;

import event.EventBus;
import model.material.MaterialType; // Importujeme Type, ne Material

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MachineFactory {

    private final EventBus eventBus;
    private final Map<String, MachineConfig> presets = new HashMap<>();

    public MachineFactory(EventBus eventBus) {
        this.eventBus = eventBus;
        for (MachineConfig cfg : MachineConfig.values()) {
            presets.put(cfg.name, cfg);
        }
    }

    /**
     * Creates a new Machine instance with custom parameters, bypassing presets.
     */
    public Machine createMachine(String name, int electricity, int oil, int priority, int wearPerTick,
                                 MaterialType outType, int duration, List<MaterialType> recipe) {

        return new Machine(electricity, wearPerTick, priority, eventBus, oil,
                outType, duration, name, recipe);
    }

    /**
     * Creates a new Worker based on a registered preset name.
     * @throws IllegalArgumentException if the preset name is unknown.
     */
    public Machine createMachine(String presetName) {
        MachineConfig cfg = presets.get(presetName);
        if (cfg == null) throw new IllegalArgumentException("Unknown preset: " + presetName);

        return new Machine(
                cfg.electricity,
                cfg.wearPerTick,
                cfg.priority,
                eventBus,
                cfg.oil,
                cfg.outputType,
                cfg.processDuration,
                cfg.name,
                cfg.recipe
                );
    }

    /**
     * Registers a new machine configuration preset or overwrites an existing one.
     */
    public void registerPreset(String name, MachineConfig cfg) {
        presets.put(name, cfg);
    }
}