package app.report;


import app.dto.FactoryStateReport;
import lombok.extern.java.Log;
import org.springframework.stereotype.Component;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
@Log

@Component
public class PrintOutagesReport implements PrintReport {

    @Override
    public void printReport(FactoryStateReport factoryData) {
        if (factoryData.outages() == null) return;

        try (PrintWriter writer = new PrintWriter(new FileWriter("outages.txt"))) {

            writer.println("--- Outages ---");

            var out = factoryData.outages();
            writer.printf("Longest: %.1f ticks, Shortest: %.1f ticks, Average: %.1f ticks, Avg wait: %.1f ticks%n",
                    out.longestOutage(),
                    out.shortestOutage(),
                    out.averageOutage(),
                    out.averageRepairWait());

            log.info("Outages Report generated");
            //System.out.println("Saved: outages.txt");

        } catch (IOException e) {
            log.warning(e.getMessage());
        }
    }
}
