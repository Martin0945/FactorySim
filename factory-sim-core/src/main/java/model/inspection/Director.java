package model.inspection;

import model.devices.human.Worker;
import model.devices.machine.Machine;
import model.devices.robot.Robot;

public class Director implements Visitor {
    @Override
    public void visit(Machine m){
        System.out.println("Report from director: machine %s in in state %s".formatted(m.getName(), m.getState()));
    }

    @Override
    public void visit(Robot r){
        //System.out.println("Report from director: robot %s in in state %s".formatted(r.getRobotType(), r.getState()));
    }

    public void visit(Worker w){
        //System.out.println("Report from director: worker %s in in state %s".formatted(w.getWorkerType(), w.getState()));
    }
}
