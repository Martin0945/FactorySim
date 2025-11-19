package model.devices.machine;

import event.EventBus;
import model.material.Material;
import model.material.MaterialType;

public class LaserCutter extends Machine{

    public LaserCutter(EventBus eventBus) {
        super(MachineType.LASER_CUTTER, 20, 1, 3, eventBus);
    }

    public Material process(Material steel) {
        //inventory - input (steel)

        return new Material(MaterialType.SCOOTER_FRAME, 1);
    }
}
