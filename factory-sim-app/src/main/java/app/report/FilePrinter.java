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

    public void saveReports(FactoryStateReport data){
        reports.forEach(report -> report.printReport(data));
    }
}


