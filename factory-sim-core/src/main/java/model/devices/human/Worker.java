package model.devices.human;

import model.production.Station;
import model.material.Material;

public class Worker extends Station {

    public Worker(String id) {
        super(id);
    }

    @Override
    public Material process(Material input) {
        System.out.println("Worker " + getId() + " processed " + input.getName());
        return input;
    }
}