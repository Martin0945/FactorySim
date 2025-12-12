package model.devices;

import event.EventBus;
import lombok.Getter;
import lombok.Setter;
import model.devices.state.IdleState;
import model.devices.state.State;
import model.inspection.Director;
import model.inspection.Visitor;
import model.material.Material;
import model.material.MaterialType;

import java.util.List;

@Getter
@Setter
public abstract class Workforce {
    protected State state;
    protected String name;
    protected List<MaterialType> recipe;
    protected List<Material> inputBuffer;
    protected Material outputBuffer;
    protected int currentProgress = 0;
    private int processDuration;
    private EventBus eventBus;
    private MaterialType outputType;



    public Workforce() {
        this.state = new IdleState();
    }

    public abstract void acceptVisitor(Visitor visitor);

    public abstract void tick();

    public abstract Material takeOutput();

    public abstract boolean acceptInput(List<Material> material);

    public abstract void finishWork();
}