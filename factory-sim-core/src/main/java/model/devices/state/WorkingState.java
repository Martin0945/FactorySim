package model.devices.state;

import event.Event;
import event.EventType;
import lombok.extern.java.Log;
import model.devices.Station;
import model.devices.Workforce;
import model.devices.human.Worker;
import model.material.Material;

import java.util.List;

@Log
public class WorkingState implements State {

    /**
     * Rejects new input while currently working.
     */
    @Override
    public State acceptInput(Workforce workforce, List<Material> materials) {
        log.info("Accepting input: Machine %s is working, state remains the same"
                .formatted(workforce.getName()));
        return this;
    }

    /**
     * Increases work progress. If progress meets duration, finishes work and checks wear.
     * @return IdleState (or BrokenState) upon completion, otherwise this.
     */
    @Override
    public State handleTick(Station station){
        station.setCurrentProgress(station.getCurrentProgress()+1);
        increaseWear(station);

        if (station.getCurrentProgress() >= station.getProcessDuration()){
            station.finishWork();
            return checkWear(station);
        }
        return this;
    }

    /**
     * Increases work progress. If progress meets duration, finishes work.
     * @return Idle state if work finished, otherwise this.
     */
    @Override
    public State handleTick(Worker worker){
        worker.setCurrentProgress(worker.getCurrentProgress()+1);

        if (worker.getCurrentProgress() >= worker.getProcessDuration()){
            worker.finishWork();
            return new IdleState();
        }
        return this;
    }

    @Override
    public State checkWear(Station station) {
        if (station.getWear() >= station.getBreakThreshold()) {
            if (station.getEventBus() != null) {
                station.getEventBus().publish(new Event(EventType.BREAKDOWN, station, station.getPriority()));
            }
            log.info("%s broke down. Wear = %f%%".formatted(station.getName(), station.getWear()));
            return new BrokenState();
        }
        if(station.getInputBuffer() != null){
            return this;
        }else{
            return new IdleState();
        }
    }

    /**
     * Increases wear at a standard rate.
     */
    @Override
    public void increaseWear(Station station) {
        double newWear = station.getWear() + (station.getWearPerTick() * (Math.random() + 0.5)); //between 0.5x and 1.5x the normal wear
        station.setWear(Math.min(newWear, 100));
    }

    @Override
    public String getDirectorReport(Workforce w){
        return "is %d/%d done with current job."
                .formatted(w.getCurrentProgress(), w.getProcessDuration());
    }

    @Override
    public Boolean isWorking(Workforce workforce){
        return true;
    }

}
