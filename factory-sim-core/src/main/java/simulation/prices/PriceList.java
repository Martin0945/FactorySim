package simulation.prices;

import model.material.MaterialType;

import java.util.HashMap;
import java.util.Map;

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

    public int getMaterialPrice(MaterialType type) {
        if(materialPrices.containsKey(type)) {
            return materialPrices.get(type);
        } else {
            System.out.println("No price for " + type);
            return 0;
        }
    }

}
