package event;

import lombok.Setter;

import java.util.*;
import java.util.function.Consumer;


public class EventBus {

    private final Map<EventType, List<EventListener>> listeners = new HashMap<>();
    //order events by their priority
    private final PriorityQueue<Event> eventQueue = new PriorityQueue<>(Comparator.comparing(Event::getPriority).reversed());

    @Setter
    private Consumer<Event> uiListener;

    public void register(EventType type, EventListener listener) {
        listeners.computeIfAbsent(type, k -> new ArrayList<>()).add(listener);
    }

    public void publish(Event event) {
        eventQueue.add(event);
        if (uiListener != null) {
            uiListener.accept(event);
        }
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