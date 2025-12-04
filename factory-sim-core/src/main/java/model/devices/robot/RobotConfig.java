package model.devices.robot;

import model.material.MaterialType;

import java.util.List;

public enum RobotConfig {
    ASSEMBLY_ROBOT("AssemblyRobot", 12, 4, 1, 6, MaterialType.SCOOTER,
            List.of(MaterialType.SCOOTER_FRAME, MaterialType.PLASTIC_GUARD, MaterialType.WHEEL, MaterialType.CHIPSET)),

    PAINT_ROBOT_SC("PaintRobotSC", 10, 3, 2, 4, MaterialType.PAINTED_SCOOTER,
            List.of(MaterialType.SCOOTER, MaterialType.PAINT)),
    PAINT_ROBOT_PHONE("PaintRobotPhone", 10, 2, 2, 3, MaterialType.PAINTED_PHONE,
            List.of(MaterialType.PHONE, MaterialType.PAINT));

    public final String name;
    public final int electricity;
    public final double wearPerTick;
    public final int priority;
    public final int processDuration;
    public final MaterialType outputType;
    public final List<MaterialType> recipe;

    RobotConfig(String name, int electricity, double wearPerTick, int priority, int processDuration,
                MaterialType outputType, List<MaterialType> recipe) {
        this.name = name;
        this.electricity = electricity;
        this.wearPerTick = wearPerTick;
        this.priority = priority;
        this.processDuration = processDuration;
        this.outputType = outputType;
        this.recipe = recipe;
    }
}