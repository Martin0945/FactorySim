package model.inspection;

import event.Event;
import event.EventBus;
import event.EventListener;
import event.EventType;
import model.factory.Factory;

public class VisitorManager implements EventListener {
    private final Factory factory;

    public VisitorManager(Factory factory, EventBus eventBus) {
        this.factory = factory;

        eventBus.register(EventType.DIRECTOR_VISIT, this);
        eventBus.register(EventType.INSPECTION, this);
    }

    /**
     * Based on the event type, calls either director or inspector.
     */
    @Override
    public void handleEvent(Event event){
        if(event.getType() == EventType.DIRECTOR_VISIT){
            handleDirector();
        }else if(event.getType() == EventType.INSPECTION){
            handleInspection();
        }
    }

    private void handleDirector(){
        Director director = new Director();
        factory.acceptVisitor(director);
    }

    private void handleInspection(){
        Inspector inspector = new Inspector();

        factory.acceptVisitor(inspector);
        inspector.reportInspection();
    }
}
