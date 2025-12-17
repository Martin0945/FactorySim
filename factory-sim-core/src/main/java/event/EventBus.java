package event;

import lombok.Getter;
import lombok.Setter;

import java.util.*;
import java.util.function.Consumer;

public class EventBus {
    private final Map<EventType, List<EventListener>> listeners = new HashMap<>();

    @Getter
    private final EventStats eventStats = new EventStats();

    @Setter
    private Consumer<Event> uiListener;

    //Order events by their priority
    private final PriorityQueue<Event> eventQueue = new PriorityQueue<>(Comparator.comparing(Event::getPriority).reversed());

    /**
     * Subscribes a listener to a specific type of event.
     */
    public void register(EventType type, EventListener listener) {
        listeners.computeIfAbsent(type, k -> new ArrayList<>()).add(listener);
    }

    /**
     * Adds an event to the processing queue and updates statistics.
     * <p>
     * This does not trigger listeners immediately.
     * Processing happens when dispatchAll() is called.
     */
    public void publish(Event event) {
        eventQueue.add(event);
        eventStats.record(event.getType());

        if (uiListener != null) {
            uiListener.accept(event);
        }
    }

    /**
     * Processes all queued events in order of their priority.
     * Notifies all registered listeners for each event.
     */
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