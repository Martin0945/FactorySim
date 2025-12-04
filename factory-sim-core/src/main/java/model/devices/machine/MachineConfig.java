package model.devices.machine;

import model.material.MaterialType;

import java.util.List;

public enum MachineConfig {
    GLASS_CUTTER("GlassCutter", List.of(MaterialType.GLASS), MaterialType.PHONE_SCREEN, 10, 1, 3, 4, 5),
    LASER_CUTTER("LaserCutter", List.of(MaterialType.STEEL), MaterialType.SCOOTER_FRAME, 20, 1, 3, 3, 7),
    PLASTIC_MOLDER("PlasticMolder", List.of(MaterialType.PLASTIC), MaterialType.PLASTIC_GUARD, 15, 2, 2, 3, 3),
    CASE_MAKER("CaseMaker", List.of(MaterialType.ELECTRONICS), MaterialType.PHONE_BODY, 18, 2, 2, 4, 6),
    ;

    public final String name;
    public final List<MaterialType> recipe;
    public final MaterialType outputType;
    public final int electricity;
    public final int oil;
    public final int priority;
    public final int processDuration;
    public final double wearPerTick;


    MachineConfig(String name, List<MaterialType> recipe, MaterialType outputType, int electricity, int oil, int priority, int processDuration, double wearPerTick) {
        this.name = name;
        this.recipe = recipe;
        this.outputType = outputType;
        this.electricity = electricity;
        this.oil = oil;
        this.priority = priority;
        this.processDuration = processDuration;
        this.wearPerTick = wearPerTick;
    }
}
