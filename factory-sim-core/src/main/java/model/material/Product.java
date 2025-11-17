package model.material;

public class Product extends Material{
    private int quality;

    public Product(String name, int quantity, int quality) {
        super(name, quantity);
        this.quality = quality;
    }
}
