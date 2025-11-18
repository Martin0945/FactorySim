package model.devices;

import lombok.Getter;
import model.material.Material;
@Getter

public abstract class Workforce {

    public abstract Material process(Material input);

}
