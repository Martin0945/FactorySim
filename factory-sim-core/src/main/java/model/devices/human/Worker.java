package model.devices.human;

import lombok.Getter;
import model.devices.Workforce;
import model.inspection.Visitor;
import model.material.Material;

@Getter

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

    @Override
    public void acceptVisitor(Visitor v){
        v.visit(this);
    }
}