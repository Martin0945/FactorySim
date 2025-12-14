package app.dto;

import model.production.ProductionLine;
import model.devices.Workforce;

import java.util.List;
import java.util.stream.Collectors;

public record ConsumptionReport(List<LineConsumption> lineConsumptions,double totalElectricity,double totalOil,double totalWages) {
    public static ConsumptionReport fromProductionLines(List<ProductionLine> lines) {
        List<LineConsumption> lineConsumptions = lines.stream().map(line -> {
            double lineElectricity = 0;
            double lineOil = 0;
            double lineWages = 0;

            for (Workforce w : line.getWorkforces()) {
                var snapshot = w.getConsumption().snapshot();
                lineElectricity += snapshot.electricity();
                lineOil += snapshot.oil();
                lineWages += snapshot.wages();
            }

            return new LineConsumption(line.getName(), lineElectricity, lineOil, lineWages);
        }).collect(Collectors.toList());

        // compute totals separately
        double totalElectricity = lineConsumptions.stream().mapToDouble(l -> l.electricity).sum();
        double totalOil = lineConsumptions.stream().mapToDouble(l -> l.oil).sum();
        double totalWages = lineConsumptions.stream().mapToDouble(l -> l.wages).sum();

        return new ConsumptionReport(lineConsumptions, totalElectricity, totalOil, totalWages);
    }


    public record LineConsumption(String lineName, double electricity, double oil, double wages) {}
}
