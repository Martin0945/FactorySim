package model.devices.state;

import lombok.extern.java.Log;
import model.devices.Station;
import model.devices.Workforce;
import model.devices.human.Worker;
import model.material.Material;

import java.util.List;

@Log
public class BrokenState implements State {

    /**
     * Checks if the station has been repaired (wear == 0).
     * @return IdleState if repaired, otherwise this.
     */
    @Override
    public State checkWear(Station station) {
        if(station.getWear() >= station.getBreakThreshold()) {
            log.info("Station %s is already broken ".formatted(station.getName()));
            return this;
        }
        return new IdleState();
    }

    /**
     * Doesn't increase wear while already broken.
     */
    @Override
    public void increaseWear(Station station) {
        log.info("%s already broken, cannot increase wear".formatted(station.getName()));
    }

    /**
     * Always rejects input due to breakdown.
     */
    public State acceptInput(Workforce workforce, List<Material> materials) {
        log.info("%s is broken, cannot accept input".formatted(workforce.getName()));
        return this;
    }

    public String getDirectorReport(Workforce w) {
        return "is broken!";
    }

    @Override
    public Boolean isWorking(Workforce workforce){
        return false;
    }

    @Override
    public State handleTick(Station station) {
        return this;
    }

    /**
     * Should never occur.
     */
    @Override
    public State handleTick(Worker worker) {
        return new IdleState();
    }


}
