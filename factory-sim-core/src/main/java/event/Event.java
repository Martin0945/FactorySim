package event;

import lombok.Getter;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
public class Event {
    private final String id = UUID.randomUUID().toString();
    private final EventType type;
    private final EventSource source;
    private final LocalDateTime timestamp = LocalDateTime.now();
    private final int priority;

    public Event(EventType type, EventSource source, int priority) {
        this.type = type;
        this.source = source;
        this.priority = priority;
    }

}