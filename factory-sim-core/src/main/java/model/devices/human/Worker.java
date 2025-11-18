package model.devices.human;

import model.devices.Station;
import model.devices.Workforce;
import model.material.Material;

public class Worker extends Workforce {
    int pay;
    WorkerType workerType;

    public Worker(WorkerType workerType, int pay) {
        this.pay = pay;
        this.workerType = workerType;
    }

    @Override
    public Material process(Material input) {
        return input;
    }
}