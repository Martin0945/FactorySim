package app.report;

import app.dto.FactoryStateReport;
import lombok.extern.java.Log;
import org.springframework.stereotype.Component;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

@Log
@Component
public class PrintEventReport implements PrintReport {

    @Override
    public void printReport(FactoryStateReport factoryData) {
        if (factoryData.events() == null) return;

        try (PrintWriter writer = new PrintWriter(new FileWriter("events.txt"))) {

            writer.println("--- Event Summary ---");

            factoryData.events().counts().forEach((type, count) ->
                    writer.printf("%s: %d%n", type, count)
            );

            log.info("Event Report generated");

        } catch (IOException e) {
            log.warning(e.getMessage());
        }
    }
}
