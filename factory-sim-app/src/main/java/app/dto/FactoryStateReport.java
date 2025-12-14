package app.dto;


public record FactoryStateReport(
        FactoryConfigReport configuration,
        ConsumptionReport consumption,
        OutagesReport outages
) {}
