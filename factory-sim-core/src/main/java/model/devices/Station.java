package model.devices;

import lombok.Getter;

@Getter
public abstract class Station extends Workforce {
    int electricity;
    protected double wear;

    public Station(int electricity) {
        this.electricity = electricity;
        this.wear = 0;
    }

    protected abstract void increaseWear();

    public abstract void repair();

}