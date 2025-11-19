package model.devices.robot;

import lombok.Getter;
import model.devices.Station;
import model.inspection.Visitor;
import model.material.Material;
@Getter

public class Robot extends Station {
    private RobotType robotType;
    public Robot(RobotType robotType, int electricity) {
        super(electricity);
        this.robotType = robotType;
    }

    public Material process(Material input) {
        return input;
    }

    public void repair(){

    }

    protected void increaseWear(){

    }

    public void acceptVisitor(Visitor v){
        v.visit(this);
    }

}
