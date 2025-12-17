package app.dto;

import simulation.stats.OutageStats;

public record OutagesReport(
        double shortestOutage,
        double longestOutage,
        double averageOutage,
        double averageRepairWait
) {
    /**
     * Extracts values from the internal OutageStats calculator.
     */
    public static OutagesReport fromStats(OutageStats stats) {
        return new OutagesReport(
                stats.getShortestOutage(),
                stats.getLongestOutage(),
                stats.getAverageOutage(),
                stats.getAverageWaitTime()
        );
    }
}
