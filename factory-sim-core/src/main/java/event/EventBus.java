package event;

import java.util.*;

public class EventBus {

    private final Map<EventType, List<EventListener>> listeners = new HashMap<>();
    //order events by their priority
    private final PriorityQueue<Event> eventQueue = new PriorityQueue<>(Comparator.comparing(Event::getPriority).reversed());

    public void register(EventType type, EventListener listener) {
        listeners.computeIfAbsent(type, k -> new ArrayList<>()).add(listener);
    }

    public void publish(Event event) {
        eventQueue.add(event);
    }

    //handle all events
    public void dispatchAll() {
        while (!eventQueue.isEmpty()) {
            Event event = eventQueue.poll();
            List<EventListener> lst = listeners.get(event.getType());
            if (lst != null) {
                lst.forEach(listener -> listener.handleEvent(event));
            }
        }
    }
}