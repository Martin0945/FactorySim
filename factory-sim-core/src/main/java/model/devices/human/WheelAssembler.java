package model.devices.human;

import model.material.Material;
import model.material.MaterialType;

import java.util.List;

public class WheelAssembler extends Worker {
    public WheelAssembler() {
        super(WorkerType.WHEEL_ASSEMBLER, 300);
    }

    public Material process(Material plastic, Material rubber) {
        //inventory - inputs (rubber, plastic)

        return new Material(MaterialType.WHEEL, 2);
    }
}
