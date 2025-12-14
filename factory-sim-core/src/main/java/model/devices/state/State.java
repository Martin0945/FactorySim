package model.devices.state;

import event.Event;
import event.EventType;
import model.devices.Station;
import model.devices.Workforce;
import model.devices.human.Worker;
import model.devices.machine.Machine;
import model.material.Material;

import java.util.List;

public interface State {
    void increaseWear(Station station);

    String getState();

    State checkWear(Station station);

    State acceptInput(Workforce workforce, List<Material> materials);

    State handleTick(Station station);

    State handleTick(Worker worker);

    String getDirectorReport(Workforce workforce);

    Boolean isWorking(Workforce workforce);
}
