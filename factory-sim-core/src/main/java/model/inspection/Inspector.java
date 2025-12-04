package model.inspection;

import model.devices.human.Worker;
import model.devices.machine.Machine;
import model.devices.robot.Robot;

public class Inspector implements Visitor {

    @Override
    public void visit(Machine m){
        System.out.println("Report from inspector: machine %s has wear of %d%%".formatted(m.getName(), m.getWear()));
    }

    @Override
    public void visit(Robot r){
        //System.out.println("Report from inspector: robot %s has wear of %d%%".formatted(r.getRobotType(), r.getWear()));
    }

    public void visit(Worker w){
        //System.out.println("Report from inspector: worker %s in in state %s".formatted(w.getWorkerType(), w.getState()));
    }
}
