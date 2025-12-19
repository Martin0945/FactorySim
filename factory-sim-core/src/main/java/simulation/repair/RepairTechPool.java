package simulation.repair;

import java.util.*;

public class RepairTechPool {

    private final Queue<RepairTech> availableTechs = new LinkedList<>();

    public RepairTechPool(List<RepairTech> technicians) {
        availableTechs.addAll(technicians);
    }

    /**
     * Retrieves an available technician from the pool.
     * @return A RepairTech instance or null if the pool is empty.
     */
    public RepairTech acquireTech() {
        return availableTechs.poll();
    }

    /**
     * Returns a technician back to the pool, making them available for new tasks.
     */
    public void releaseTech(RepairTech technician) {
        availableTechs.add(technician);
    }

    /**
     * Checks if there is at least one technician ready to work.
     */
    public boolean hasAvailable() {
        return !availableTechs.isEmpty();
    }
}
