package event;

public interface EventListener {

    /**
     * Method triggered when an event of a subscribed type is dispatched.
     */
    void handleEvent(Event event);
}