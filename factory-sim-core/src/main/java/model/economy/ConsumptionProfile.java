package model.economy;

public record ConsumptionProfile(
        double electricityPerTick,
        double oilPerTick,
        double wagePerTick
) {
    public static final ConsumptionProfile NONE =
            new ConsumptionProfile(0, 0, 0);
}
