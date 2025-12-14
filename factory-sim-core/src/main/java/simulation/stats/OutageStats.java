package simulation.stats;

import lombok.Getter;

public class OutageStats {

    @Getter
    private int totalBreakdowns;
    @Getter
    private int totalOutageTicks;
    @Getter
    private int longestOutage;
    private int shortestOutage = Integer.MAX_VALUE;
    @Getter
    private int totalWaitTicks;

    public void recordBreakdown() {
        totalBreakdowns++;
    }

    public void recordOutageDuration(int ticks) {
        totalOutageTicks += ticks;
        longestOutage = Math.max(longestOutage, ticks);
        shortestOutage = Math.min(shortestOutage, ticks);
    }

    public void recordWaitTime(int ticks) {
        totalWaitTicks += ticks;
    }

    public int getShortestOutage() {
        return shortestOutage == Integer.MAX_VALUE ? 0 : shortestOutage;
    }

    public double getAverageOutage() {
        return totalOutageTicks / (double) totalBreakdowns;
    }

    public double getAverageWaitTime() {
        return totalWaitTicks / (double) totalBreakdowns;
    }
}
