package model.devices.state;

public abstract class State {
    public abstract State work();

    public abstract State brake();

    public abstract State repair();

    public abstract State idle();
}
