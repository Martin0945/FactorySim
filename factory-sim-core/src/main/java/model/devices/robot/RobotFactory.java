package model.devices.robot;

import event.EventBus;
import model.material.MaterialType;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RobotFactory {

    private final EventBus eventBus;
    private final Map<String, RobotConfig> presets = new HashMap<>();

    public RobotFactory(EventBus eventBus) {
        this.eventBus = eventBus;
        for (RobotConfig cfg : RobotConfig.values()) {
            presets.put(cfg.name, cfg);
        }
    }

    /**
     * Creates a new Robot instance with custom parameters, bypassing presets.
     */
    public Robot createRobot(String name, int electricity, int wearPerTick, int priority,
                               int duration, MaterialType outType,  List<MaterialType> recipe) {

        return new Robot(name, electricity, wearPerTick, priority, duration, outType, recipe, eventBus);
    }

    /**
     * Creates a new Robot based on a registered preset name.
     * @throws IllegalArgumentException if the preset name is unknown.
     */
    public Robot createRobot(String presetName) {
        RobotConfig cfg = presets.get(presetName);
        if (cfg == null) throw new IllegalArgumentException("Unknown robot preset: " + presetName);

        return new Robot(
                cfg.name,
                cfg.electricity,
                cfg.wearPerTick,
                cfg.priority,
                cfg.processDuration,
                cfg.outputType,
                cfg.recipe,
                eventBus
        );
    }

    /**
     * Registers a new robot configuration preset or overwrites an existing one.
     */
    public void registerPreset(String name, RobotConfig cfg) {
        presets.put(name, cfg);
    }
}