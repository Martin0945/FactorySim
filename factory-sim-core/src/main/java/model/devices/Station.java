package model.devices;

import lombok.Getter;
import model.devices.state.BrokenState;
import model.devices.state.IdleState;
import model.devices.state.State;

@Getter
public abstract class Station extends Workforce {
    int electricity;
    State state;
    public Station(int electricity) {
        this.state = new IdleState();
        this.electricity = electricity;
    }

    protected abstract void increaseWear();

    public abstract void repair();

}