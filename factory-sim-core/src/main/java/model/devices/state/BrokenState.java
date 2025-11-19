package model.devices.state;

public class BrokenState extends State {
    public State work(){
        return new WorkingState();
    }
    public State breakDown(){
        return this;
    }
    public State repair(){
        return new RepairingState();
    }
    public State idle(){
        return new IdleState();
    }
}
