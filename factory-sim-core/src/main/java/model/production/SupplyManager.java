package model.production;

import event.Event;
import event.EventBus;
import event.EventListener;
import event.EventType;
import lombok.extern.java.Log;
import model.devices.Workforce;
import model.material.Material;
import model.material.MaterialType;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Log

public class SupplyManager implements EventListener {
    private final int REFILL_SIZE = 6;
    private final int MAX_NUM_OF_MATERIALS = 10;
    private final int MAX_NUM_OF_PRODUCTS = 10;

    private final List<ProductionLine> allLines;
    private final Map<String, List<MaterialType>> lineRecipes = new HashMap<>();



    public SupplyManager(EventBus eventBus, List<ProductionLine> AllLines) {
        this.allLines = AllLines;
        eventBus.register(EventType.OUT_OF_INPUT, this);
        initRecipes();
    }

    private void initRecipes() {
        lineRecipes.put("Line Scooter", List.of(
                MaterialType.STEEL, MaterialType.PLASTIC,
                MaterialType.RUBBER, MaterialType.ELECTRONICS, MaterialType.PAINT
        ));

        lineRecipes.put("Line Phone", List.of(
                MaterialType.GLASS, MaterialType.STEEL,
                MaterialType.ELECTRONICS, MaterialType.PAINT
        ));
    }

    public void initializeLines() {
        for(ProductionLine line : allLines) {
            List<MaterialType> recipe = lineRecipes.get(line.getName());

            for (int i = 0; i < REFILL_SIZE; i++) {
                for (MaterialType material : recipe) {
                    line.addInput(new Material(material));
                }
            }
            log.info("First fill of %s ".formatted(line.getName()));
        }
    }

    @Override
    public void handleEvent(Event event) {
        if(event.getType() == EventType.OUT_OF_INPUT) {
            Workforce workforce = (Workforce) event.getSource();
            ProductionLine lineToBeRestocked = findLine(workforce);

            if(lineToBeRestocked != null) {
                smartRestock(lineToBeRestocked, workforce);
            }else{
                log.warning("Workforce %s doesn't belong to any line!".formatted(workforce.getName()));
            }
        }
    }

    public void smartRestock(ProductionLine line, Workforce workforce) {
        List<MaterialType> recipe =  workforce.getRecipe();
        boolean found = false;

        long amountOfOutput = countMaterialOnLine(line, workforce.getOutputType());
        if(amountOfOutput >= MAX_NUM_OF_PRODUCTS) {
            log.info("%s is full of %s, not restocking!".formatted(line.getName(), workforce.getOutputType()));
            return;
        }

        for(MaterialType type : recipe) {
            found = false;
            long currentAmount = countMaterialOnLine(line, type);

            long missing = MAX_NUM_OF_MATERIALS - currentAmount;
            long toAdd = Math.min(REFILL_SIZE, Math.max(missing, 0));

            for(int i = 0; i < toAdd; i++) {
                 line.addInput(new Material(type));
                 found = true;
            }
        }
        if(found) {
            log.info("Restocking %s: %s ".formatted(line.getName(), workforce.getName()));
        }
    }

    ProductionLine findLine(Workforce workforce) {
        for(ProductionLine line : allLines) {
            List<Workforce> lineWorkforces = line.getWorkforces();
            for(Workforce w : lineWorkforces) {
                if(w.equals(workforce)) {
                    return line;
                }
            }
        }
        return null;
    }

    private long countMaterialOnLine(ProductionLine line, MaterialType type) {
        return line.getLineStorage().stream()
                .filter(m -> m.getType() == type)
                .count();
    }
}
