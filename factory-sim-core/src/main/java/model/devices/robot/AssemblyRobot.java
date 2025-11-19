package model.devices.robot;

import event.EventBus;
import model.material.Material;
import model.material.MaterialType;

import java.util.List;

public class AssemblyRobot extends Robot{
    public AssemblyRobot() {
        super(RobotType.ASSEMBLY_ROBOT, 12);
    }

    public Material process(Material frame, Material guard, List<Material> wheels, Material chipset) {
        //inventory - inputs (frame, guard, 2x wheel, chipset)

        return new Material(MaterialType.SCOOTER, 1);
    }

    public Material process(Material phoneScreen, Material chipset, Material phoneFrame) {

        return new Material(MaterialType.PHONE, 1);
    }
}
