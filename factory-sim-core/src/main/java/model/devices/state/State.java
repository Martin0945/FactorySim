package model.devices.state;

import model.devices.Station;
import model.devices.Workforce;
import model.devices.human.Worker;
import model.material.Material;
import java.util.List;

public interface State {
    /**
     * Increases the wear level of a station based on specific state logic.
     */
    void increaseWear(Station station);

    /**
     * Checks if the station has exceeded its break threshold or if it was repaired.
     * @return BrokenState if threshold exceeded, IdleState if repaired, or current state.
     */
    State checkWear(Station station);

    /**
     * Attempts to process input materials matching the workforce recipe.
     * @return WorkingState if materials match, otherwise returns current state.
     */
    State acceptInput(Workforce workforce, List<Material> materials);

    /**
     * Handles one simulation tick for a Machine/Station.
     * Updates wear and progresses work.
     */
    State handleTick(Station station);

    /**
     * Handles one simulation tick for a Worker.
     * Manages work progress.
     */
    State handleTick(Worker worker);

    /**
     * Generates a status report for UI or logging purposes explaining why the entity is in this state.
     */
    String getDirectorReport(Workforce workforce);

    /**
     * @return true if the entity is currently processing materials.
     */
    Boolean isWorking(Workforce workforce);
}
