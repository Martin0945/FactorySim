package event;

import java.time.LocalDateTime;
import java.util.UUID;

public class Event {
    private final String id = UUID.randomUUID().toString();
    private final EventType type;
    private final Object source;
    private final LocalDateTime timestamp = LocalDateTime.now();
    private final int priority;

    public Event(EventType type, Object source, int priority) {
        this.type = type;
        this.source = source;
        this.priority = priority;
    }

    public EventType getType() { return type; }
    public Object getSource() { return source; }
    public LocalDateTime getTimestamp() { return timestamp; }
    public String getId() { return id; }
    public int getPriority() { return priority; }
}