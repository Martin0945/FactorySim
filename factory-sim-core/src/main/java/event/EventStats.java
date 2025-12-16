package event;


import java.util.EnumMap;
import java.util.Map;

public class EventStats {

    private final Map<EventType, Integer> counts = new EnumMap<>(EventType.class);

    public void record(EventType type) {
        counts.merge(type, 1, Integer::sum);
    }

    public Map<EventType, Integer> snapshot() {
        return Map.copyOf(counts);
    }
}
