package app.dto;

import model.production.ProductionLine;

import java.util.List;
import java.util.stream.Collectors;

public record FactoryConfigReport(List<ProductionLineConfig> productionLines) {


    public static FactoryConfigReport fromProductionLines(List<ProductionLine> lines) {
        List<ProductionLineConfig> lineConfigs = lines.stream()
                .map(line -> new ProductionLineConfig(
                        line.getName(),
                        line.getWorkforces().stream()
                                .map(w -> new StationConfig(
                                        w.getName(),
                                        w.getClass().getSimpleName(),
                                        w.getInput() != null && !w.getInput().isEmpty()
                                                ? w.getInput().stream().map(Enum::name).collect(Collectors.joining(", "))
                                                : null,
                                        w.getOutput() != null ? w.getOutput().name() : null
                                ))
                                .collect(Collectors.toList())
                ))
                .collect(Collectors.toList());

        return new FactoryConfigReport(lineConfigs);
    }

    public record ProductionLineConfig(String name, List<StationConfig> stations) {}
    public record StationConfig(String name, String type, String input, String output) {}
}
