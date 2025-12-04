package model.devices.robot;

import event.EventBus;
import java.util.HashMap;
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

    public void registerPreset(String name, RobotConfig cfg) {
        presets.put(name, cfg);
    }
}