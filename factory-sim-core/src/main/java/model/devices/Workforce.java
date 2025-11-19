package model.devices;

import lombok.Getter;
import model.devices.state.IdleState;
import model.devices.state.State;
import model.inspection.Visitor;
import model.material.Material;
@Getter

public abstract class Workforce {
    State state;

    public Workforce() {
        this.state = new IdleState();
    }

    public abstract Material process(Material input);

    public abstract void acceptVisitor(Visitor v);

}
