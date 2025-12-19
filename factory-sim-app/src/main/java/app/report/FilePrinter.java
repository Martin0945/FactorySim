package app.report;

import app.dto.FactoryStateReport;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class FilePrinter {
    private final List<PrintReport> reports;

    public FilePrinter(List<PrintReport> reports) {
        this.reports = reports;
    }

    /**
     * Generates text files for all registered report types.
     * @param data The current factory state data
     */
    public void saveReports(FactoryStateReport data){
        reports.forEach(report -> report.printReport(data));
    }
}


