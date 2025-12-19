package simulation.prices;

import lombok.extern.java.Log;
import model.material.MaterialType;

import java.util.HashMap;
import java.util.Map;
@Log

public class PriceList {
    private final Map<MaterialType, Integer> materialPrices = new HashMap<>();

    public PriceList(){
        materialPrices.put(MaterialType.STEEL, 150);
        materialPrices.put(MaterialType.PLASTIC, 50);
        materialPrices.put(MaterialType.RUBBER, 60);
        materialPrices.put(MaterialType.GLASS, 100);
        materialPrices.put(MaterialType.ELECTRONICS, 500);
        materialPrices.put(MaterialType.PAINT, 80);
    }

    /**
     * Retrieves the cost per unit for a specific material type.
     * @return The price integer, or 0 if the material is not listed.
     */
    public int getMaterialPrice(MaterialType type) {
        if(materialPrices.containsKey(type)) {
            return materialPrices.get(type);
        } else {
            log.warning("No price for %s".formatted(type));
            return 0;
        }
    }

}
