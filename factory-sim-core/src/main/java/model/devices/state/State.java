package model.devices.state;

public abstract class State {
    public abstract State work();

    public abstract State breakDown();

    public abstract State repair();

    public abstract State idle();
}
