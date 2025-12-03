package model.devices.machine;

import model.material.MaterialType;

public enum MachineConfig {
    GLASS_CUTTER("GlassCutter", MaterialType.GLASS, 1, MaterialType.PHONE_SCREEN, 10, 1, 3),
    LASER_CUTTER("LaserCutter", MaterialType.STEEL, 1, MaterialType.SCOOTER_FRAME, 20, 1, 3),
    PLASTIC_MOLDER("PlasticMolder", MaterialType.PLASTIC, 1, MaterialType.PLASTIC_GUARD, 15, 2, 2),
    CASE_MAKER("CaseMaker", MaterialType.ELECTRONICS, 1, MaterialType.PHONE_BODY, 18, 2, 2),
    ;

    public final String name;
    public final MaterialType input;
    public final int inQuantity;
    public final MaterialType output;
    public final int electricity;
    public final int oil;
    public final int priority;

    MachineConfig(String name, MaterialType input, int inQuantity, MaterialType output, int electricity, int oil, int priority) {
        this.name = name;
        this.input = input;
        this.output = output;
        this.electricity = electricity;
        this.oil = oil;
        this.priority = priority;
        this.inQuantity = inQuantity;
    }
}
