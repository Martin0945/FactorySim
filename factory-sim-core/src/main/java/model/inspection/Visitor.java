package model.inspection;

import model.devices.human.Worker;
import model.devices.machine.Machine;
import model.devices.robot.Robot;
import model.factory.Factory;
import model.production.ProductionLine;

public interface Visitor {
    void visit(Machine machine);

    void visit(Robot robot);

    void visit(Worker worker);

    void visit(Factory factory);

    void visit(ProductionLine prodLine);

}
