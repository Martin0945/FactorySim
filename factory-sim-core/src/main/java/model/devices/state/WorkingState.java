package model.devices.state;

import event.Event;
import event.EventType;
import lombok.extern.java.Log;
import model.devices.Station;
import model.devices.Workforce;
import model.devices.human.Worker;
import model.devices.machine.Machine;
import model.material.Material;

import java.util.List;

@Log
public class WorkingState implements State {

    @Override
    public State acceptInput(Workforce workforce, List<Material> materials) {
        log.info("Accepting input: Machine %s is working, state remains the same".formatted(workforce.getName()));

        return this;
    }
        @Override
    public Material process(Machine machine, Material material) {
//        increaseWear(machine);
//        if(material.getType() == machine.getInput().getType()){
//            //also probably check quantity here
//            return machine.getOutput();
//        }
//        else{
//            //probably some failure event here
//            return material;
//        }
        return null;
       }

    @Override
    public State handleTick(Station station){
        station.setCurrentProgress(station.getCurrentProgress()+1);
        increaseWear(station);

        if (station.getCurrentProgress() >= station.getProcessDuration()){
            station.finishWork();
            return new IdleState();
        }
        return this;
    }

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
            System.out.println("Machine " + station.getName() + " broke, wear " + station.getWear() + "%");
            return new BrokenState();
        }
        if(station.getInputBuffer() != null){
            return this;
        }else{
            return new IdleState();
        }
    }

    @Override
    public void increaseWear(Station station) {
        station.setWear(station.getWear() + station.getWearPerTick());
    }

    @Override
    public String getState() {
        return "Working";
    }
}
