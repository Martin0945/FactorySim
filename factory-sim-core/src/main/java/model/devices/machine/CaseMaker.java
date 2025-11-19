package model.devices.machine;

import event.EventBus;
import model.material.Material;
import model.material.MaterialType;

public class CaseMaker extends Machine{
    public CaseMaker(EventBus eventBus) {
        super(MachineType.CASE_MAKER, 18, 2, 2, eventBus);
    }

    public Material process(Material electronics) {

        return new Material(MaterialType.PHONE_BODY, 1);
    }
}
