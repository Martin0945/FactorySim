package model.devices.machine;

import event.EventBus;
import model.material.Material;
import model.material.MaterialType;

public class PlasticMolder extends Machine{
    public PlasticMolder(EventBus eventBus) {
        super(MachineType.PLASTIC_MOLDER, 15, 2, 2, eventBus);
    }

    public Material process(Material plastic) {
        //inventory - input (plastic)

        return new Material(MaterialType.PLASTIC_GUARD, 1);
    }
}
