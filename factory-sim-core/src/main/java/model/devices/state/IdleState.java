package model.devices.state;

public class IdleState extends State {
    public State work(){
        return new WorkingState();
    }
    public State breakDown(){
        return new BrokenState();
    }
    public State repair(){
        return new RepairingState();
    }
    public State idle(){
        return this;
    }
}
