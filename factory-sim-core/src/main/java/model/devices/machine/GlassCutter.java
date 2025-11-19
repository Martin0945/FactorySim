package model.devices.machine;

import event.EventBus;
import model.material.Material;
import model.material.MaterialType;

public class GlassCutter extends Machine{
    public GlassCutter(EventBus eventBus) {
        super(MachineType.GLASS_CUTTER, 10, 1, 3, eventBus);
    }

    public Material process(Material glass){

        return new Material(MaterialType.PHONE_SCREEN,1);
    }
}
