package model.inspection;

import model.devices.human.Worker;
import model.devices.machine.Machine;
import model.devices.robot.Robot;
import model.factory.Factory;
import model.production.ProductionLine;

public interface Visitor {
    /**
     * Collects data about machine.
     */
    void visit(Machine machine);

    /**
     * Collects data about robot.
     */
    void visit(Robot robot);

    /**
     * Collects data about worker. only director.
     */
    void visit(Worker worker);

    /**
     * Collects data about factory.
     */
    void visit(Factory factory);

    /**
     * Collects data about production line.
     */
    void visit(ProductionLine prodLine);

}
