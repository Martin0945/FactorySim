package app.report;

import app.dto.FactoryStateReport;

public interface PrintReport {
    /**
     * Based on strategy, generates txt file with report about factory
     * @param factoryData The current factory state data
     */
    void printReport(FactoryStateReport factoryData);
}
