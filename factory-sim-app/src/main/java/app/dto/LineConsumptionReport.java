package app.dto;

public record LineConsumptionReport(
        String lineName,
        double electricity,
        double oil,
        double wages
) {}
