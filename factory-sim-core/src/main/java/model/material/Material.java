package model.material;

import lombok.Getter;

public class Material {
    @Getter
    private MaterialType type;
    private int quantity;

    public Material(MaterialType type, int quantity) {
        this.type = type;
        this.quantity = quantity;
    }
}
