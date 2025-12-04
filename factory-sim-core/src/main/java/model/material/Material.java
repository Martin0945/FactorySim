package model.material;

import lombok.Getter;
import lombok.ToString;

@ToString
@Getter
public class Material {
    private final MaterialType type;

    public Material(MaterialType type) {
        this.type = type;
    }
}
