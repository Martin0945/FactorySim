package simulation;

import java.util.*;

public class RepairTechPool {

    private final Queue<RepairTech> availableTechs = new LinkedList<>();

    public RepairTechPool(List<RepairTech> technicians) {
        availableTechs.addAll(technicians);
    }

    public RepairTech acquireTech() {
        return availableTechs.poll();
    }

    public void releaseTech(RepairTech technician) {
        availableTechs.add(technician);
    }

    public boolean hasAvailable() {
        return !availableTechs.isEmpty();
    }
}
