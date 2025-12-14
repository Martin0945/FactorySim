package model.devices.state;

import event.Event;
import event.EventType;
import lombok.extern.java.Log;
import model.devices.Station;
import model.devices.Workforce;
import model.devices.human.Worker;
import model.devices.machine.Machine;
import model.material.Material;
import model.material.MaterialType;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Log
public class IdleState implements State {
    private boolean helpRequested = false;

    @Override
    public State acceptInput(Workforce workforce, List<Material> materials) {

        List<MaterialType> neededMaterials = workforce.getRecipe();

        List<Material> foundMaterials = new ArrayList<>();
        List<Material> tempAvailable = new ArrayList<>(materials);

        for(MaterialType typeNeeded : neededMaterials) {
            Material found = null;
            for(Material materialAvailable : tempAvailable) {
                if(materialAvailable.getType() == typeNeeded) {
                    found = materialAvailable;
                    break;
                }
            }
            if(found != null) {
                //debug log.info(workforce.getName()+ "found material:" + found);
                foundMaterials.add(found);
                tempAvailable.remove(found);
            }else{
                if (workforce.getEventBus() != null) {
                    if(typeNeeded.isRaw() && !helpRequested) {
                        workforce.getEventBus().publish(new Event(EventType.OUT_OF_INPUT, workforce, 3));
                        helpRequested = true;
                    }
                }
                return this;
            }
        }

        materials.removeAll(foundMaterials);
        workforce.setInputBuffer(foundMaterials);
        workforce.setCurrentProgress(0);

        return new WorkingState();
    }

    @Override
    public State handleTick(Station station){
        increaseWear(station);
        return checkWear(station);
    }

    public State handleTick(Worker worker){
        return this;
    }

    @Override
    public State checkWear(Station station) {
        if (station.getWear() >= station.getBreakThreshold()) {
            if (station.getEventBus() != null) {
                station.getEventBus().publish(new Event(EventType.BREAKDOWN, station, station.getPriority()));
            }
            log.info("%s has broke down. Wear = %f%%".formatted(station.getName(), station.getWear()));
            return new BrokenState();
        }
        return this;
    }

    @Override
    public void increaseWear(Station station) {
        station.setWear(((station.getWear() + station.getWearPerTick()) * (Math.random() + 0.5)) / 2);  //slower wear when idle
    }

    @Override
    public String getDirectorReport(Workforce workforce){
        if(workforce.getInputBuffer() == null) {
            return "is not working because input buffer is empty, waiting for %s.".formatted(workforce.getRecipe());
        }
        else if(workforce.getOutputBuffer() != null) {
            return "is not working because output buffer is full, waiting to be emptied.";
        }else{
            return "is not working because of unknown circumstances.";
        }
    }

    @Override
    public Boolean isWorking(Workforce workforce){
        return false;
    }


    @Override
    public String getState() {
        return "Idle";
    }
}
