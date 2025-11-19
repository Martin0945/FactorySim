package model.devices.robot;

import event.EventBus;
import model.material.Material;
import model.material.MaterialType;
import model.material.Product;
import model.material.ProductType;

import java.util.HashMap;
import java.util.Map;

public class PaintRobot extends Robot{
    private final Map<MaterialType, ProductType> paintMap = new HashMap<>();


    public PaintRobot() {
        super(RobotType.PAINT_STATION, 10);
        paintMap.put(MaterialType.SCOOTER, ProductType.PAINTED_SCOOTER);
        paintMap.put(MaterialType.PHONE, ProductType.PAINTED_PHONE);
    }

    public Product process(Material paint, Material input) {
        ProductType outputType = paintMap.get(input.getType());

        return new Product(outputType, 1);
    }
}
