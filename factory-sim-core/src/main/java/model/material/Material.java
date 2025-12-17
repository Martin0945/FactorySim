package model.material;

import lombok.Getter;
import lombok.ToString;

@ToString
@Getter
public class Material {
    private final MaterialType type;
    private final double value;

    public Material(MaterialType type, double value) {
        this.type = type;
        this.value = value;
    }
}
