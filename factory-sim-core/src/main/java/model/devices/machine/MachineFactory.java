package model.devices.machine;

import event.EventBus;
import model.material.Material;

import java.util.HashMap;
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

    public Machine createMachine(String name, int electricity, int oil, int priority, Material input, Material output) {
        return new Machine(name, electricity, oil, priority, eventBus, input, output);
    }

    public Machine createMachine(String presetName) {
        MachineConfig cfg = presets.get(presetName);
        if (cfg == null) throw new IllegalArgumentException("Unknown machine preset: " + presetName);
        Material inMaterial = new Material(cfg.input, cfg.inQuantity);
        Material outMaterial = new Material(cfg.output, 1);
        return new Machine(
                presetName,
                cfg.electricity,
                cfg.oil,
                cfg.priority,
                eventBus,
                inMaterial,
                outMaterial
        );
    }
    public void registerPreset(String name, MachineConfig cfg) {
        presets.put(name, cfg);
    }
}
