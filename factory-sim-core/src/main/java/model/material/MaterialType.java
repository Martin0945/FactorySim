package model.material;

public enum MaterialType {
    //RAW MATERIALS - true
    STEEL(true), PLASTIC(true), RUBBER(true), GLASS(true), ELECTRONICS(true), PAINT(true),


    SCOOTER_FRAME(false), PLASTIC_GUARD(false), WHEEL(false), CHIPSET(false), SCOOTER(false),
    PHONE_SCREEN(false), PHONE_BODY(false), PHONE(false),
    PAINTED_SCOOTER(false), PAINTED_PHONE(false);

    private final boolean isRawMaterial;
    MaterialType(boolean isRawMaterial) {
        this.isRawMaterial = isRawMaterial;
    }

    public boolean isRaw() {
        return isRawMaterial;
    }

}
