package model.devices.state;

public class RepairingState extends State {
    public State work(){
        return new WorkingState();
    }
    public State breakDown(){
        return new BrokenState();
    }
    public State repair(){
        return this;
    }
    public State idle(){
        return new IdleState();
    }
}
