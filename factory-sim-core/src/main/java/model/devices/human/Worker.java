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
        this.workerType = workerType;
        this.pay = pay;
    }


    @Override
    public void acceptVisitor(Visitor v){
        v.visit(this);
    }
}