package model.devices.state;

public class WorkingState extends State {
    public State work(){
        return this;
    }
    public State breakDown(){
        return new BrokenState();
    }
    public State repair(){
        return new RepairingState();
    }
    public State idle(){
        return new IdleState();
    }
}
