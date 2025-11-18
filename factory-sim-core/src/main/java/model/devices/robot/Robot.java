package model.devices.robot;

import model.devices.Station;
import model.devices.state.State;
import model.material.Material;

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

}
