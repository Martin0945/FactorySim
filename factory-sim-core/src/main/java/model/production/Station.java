package model.production;

import lombok.Getter;
import model.material.Material;

@Getter
public abstract class Station{
    private final String name;

    public Station(String name) {
        this.name = name;
    }

    public abstract Material process(Material input);

}