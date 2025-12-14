package model.economy;

public record ConsumptionSnapshot(
        double electricity,
        double oil,
        double wages
) {}
