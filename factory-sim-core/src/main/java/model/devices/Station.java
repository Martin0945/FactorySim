package model.devices;

import lombok.Getter;
import model.devices.state.BrokenState;
import model.devices.state.IdleState;
import model.devices.state.State;

@Getter
public abstract class Station extends Workforce {
    int electricity;
    protected int wear;

    public Station(int electricity) {
        this.electricity = electricity;
        this.wear = 0;
    }

    protected abstract void increaseWear();

    public abstract void repair();

}