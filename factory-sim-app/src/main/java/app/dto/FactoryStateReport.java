package app.dto;


public record FactoryStateReport(
        FactoryConfigReport configuration,
        EventReport events,
        ConsumptionReport consumption,
        OutagesReport outages
) {}
