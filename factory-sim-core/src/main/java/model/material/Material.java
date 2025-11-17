package model.material;

import lombok.Getter;

public class Material {
    @Getter
    private String name;
    private int quantity;

    public Material(String name, int quantity) {
        this.name = name;
        this.quantity = quantity;
    }
}
