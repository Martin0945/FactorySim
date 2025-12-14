package app.report;

import app.dto.FactoryStateReport;
import lombok.extern.java.Log;
import org.springframework.stereotype.Component;

import java.io.FileWriter;
import java.io.PrintWriter;

@Log

@Component
public class PrintConsumptionReport implements PrintReport {
    @Override
    public void printReport(FactoryStateReport factoryData){
        try (PrintWriter writer = new PrintWriter(new FileWriter("consumption.txt"))) {

            writer.println("--- Consumption ---");

            for (var lc : factoryData.consumption().lineConsumptions()) {
                writer.printf("LINE: %s (Total: Electricity = %.1fkWh, Oil = %.1fl, Wages = %.1fKč)%n",
                        lc.lineName(),lc.electricity(),lc.oil(), lc.wages());

                for (var m : lc.machines()) {
                    writer.printf("   • [%s] %s: Electricity = %.1fkWh, Oil = %.1fl, Wages = %.1fKč%n",
                            m.type(), m.name(), m.electricity(), m.oil(), m.wages());
                }

                writer.println("   --------------------------------");
            }

            var total = factoryData.consumption();
            writer.printf("TOTAL: Electricity = %.1fkWh, Oil = %.1fl, Wages = %.1fKč%n",
                    total.totalElectricity(), total.totalOil(), total.totalWages());

            log.info("Consumption Report generated");
        }

        catch (Exception e) {
            log.warning(e.getMessage());
        }
    }
}
