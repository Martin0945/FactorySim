package model.inspection;

import model.devices.human.Worker;
import model.devices.machine.Machine;
import model.devices.robot.Robot;

public interface Visitor {
    void visit(Machine m);

    void visit(Robot r);

    void visit(Worker w);

}
