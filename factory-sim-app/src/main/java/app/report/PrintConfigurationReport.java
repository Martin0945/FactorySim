package app.report;

import app.dto.FactoryStateReport;
import lombok.extern.java.Log;
import org.springframework.stereotype.Component;

import java.io.FileWriter;
import java.io.PrintWriter;
@Log

@Component
public class PrintConfigurationReport implements PrintReport {
    @Override
    public void printReport(FactoryStateReport factoryData){
        try (PrintWriter writer = new PrintWriter(new FileWriter("configuration.txt"))) {

            for (var line : factoryData.configuration().productionLines()) {
                writer.println("=== Production Line: " + line.name() + " ===");

                for (var station : line.stations()) {
                    String inputs = (station.input() == null || station.input().isEmpty()) ? "null" : station.input();

                    writer.printf("%s \"%s\" | Input: %s | Output: %s%n",
                            station.type(), station.name(), inputs, station.output());
                }

                writer.println();
            }


            log.info("Configuration Report generated");
        }

        catch (Exception e) {
            log.warning(e.getMessage());
        }
    }
}
