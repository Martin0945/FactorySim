package model.devices.human;

import model.material.MaterialType;

import java.util.List;

public enum WorkerConfig {
    WHEEL_ASSEMBLER("WheelAssembler", List.of(MaterialType.RUBBER, MaterialType.PLASTIC), MaterialType.WHEEL,
                    3,2,300),
    EUI("EUI", List.of(MaterialType.ELECTRONICS), MaterialType.CHIPSET,3,3,350),
    ;

    public final String name;
    public final List<MaterialType> recipe;
    public final MaterialType outputType;
    public final int priority;
    public final int processDuration;
    public final int pay;

    WorkerConfig(String name, List<MaterialType> recipe, MaterialType outputType, int priority,
                 int processDuration, int pay) {
        this.name = name;
        this.recipe = recipe;
        this.outputType = outputType;
        this.priority = priority;
        this.processDuration = processDuration;
        this.pay = pay;
    }
}
