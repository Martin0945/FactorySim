package model.devices.human;


import model.material.Material;
import model.material.MaterialType;

public class EUIWorker extends Worker {

    public EUIWorker() {
        super(WorkerType.EUI, 350);
    }

    public Material process(Material electronics) {
        //inventory - input (electronics)

        return new Material(MaterialType.CHIPSET, 1);
    }
}
