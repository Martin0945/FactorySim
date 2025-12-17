package simulation.stats;

import lombok.Getter;

@Getter

public class OutageStats {

    private int totalBreakdowns;
    private int totalOutageTicks;
    private int longestOutage;
    private int shortestOutage = Integer.MAX_VALUE;
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
        return totalBreakdowns == 0 ? 0 : totalOutageTicks / (double) totalBreakdowns;    }

    public double getAverageWaitTime() {
        return totalBreakdowns == 0 ? 0 : totalWaitTicks / (double) totalBreakdowns;    }
}
