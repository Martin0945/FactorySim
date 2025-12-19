package app.dto;

import model.production.ProductionLine;
import model.devices.Workforce;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public record ConsumptionReport(List<LineConsumption> lineConsumptions,double totalElectricity,
                                double totalOil,double totalWages, double totalMaterialCosts,
                                double totalRevenue) {

    /**
     * Iterates through production lines and their workforce to sum up all consumption stats.
     * Converts domain objects into this lightweight report structure.
     */
    public static ConsumptionReport fromProductionLines(List<ProductionLine> lines) {
        List<LineConsumption> lineConsumptions = lines.stream().map(line -> {
            double lineElectricity = 0;
            double lineOil = 0;
            double lineWages = 0;
            double lineMaterialCosts = line.getMaterialCosts();
            double lineRevenue = line.getRevenue();

            List<MachineConsumption> machineDetails = new ArrayList<>();

            for (Workforce w : line.getWorkforces()) {
                var snapshot = w.getConsumption().snapshot();
                lineElectricity += snapshot.electricity();
                lineOil += snapshot.oil();
                lineWages += snapshot.wages();

                machineDetails.add(new MachineConsumption(w.getName(), w.getClass().getSimpleName(),
                        snapshot.electricity(), snapshot.oil(), snapshot.wages()));
            }

            return new LineConsumption(line.getName(), lineElectricity, lineOil, lineWages, lineMaterialCosts, lineRevenue, machineDetails);
        }).collect(Collectors.toList());

        // compute totals separately
        double totalElectricity = lineConsumptions.stream().mapToDouble(l -> l.electricity).sum();
        double totalOil = lineConsumptions.stream().mapToDouble(l -> l.oil).sum();
        double totalWages = lineConsumptions.stream().mapToDouble(l -> l.wages).sum();
        double totalMaterialCosts = lineConsumptions.stream().mapToDouble(l -> l.materialCost).sum();
        double totalRevenue = lineConsumptions.stream().mapToDouble(l -> l.revenue).sum();

        return new ConsumptionReport(lineConsumptions, totalElectricity, totalOil, totalWages, totalMaterialCosts, totalRevenue);
    }


    public record LineConsumption(String lineName, double electricity, double oil, double wages, double materialCost, double revenue,
                                  List<MachineConsumption> machines) {}

    public record MachineConsumption (
            String name,
            String type,
            double electricity,
            double oil,
            double wages
    ) {}
}
