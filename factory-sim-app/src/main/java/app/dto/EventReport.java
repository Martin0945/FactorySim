package app.dto;

import java.util.Map;

public record EventReport(
        Map<String, Integer> counts
) {}
