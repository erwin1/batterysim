package io.github.erwin1.batterysim.stats;

import io.github.erwin1.batterysim.usage.Usage;

import java.time.LocalDate;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

public class MonthSummary {
    private LocalDate month;
    private Summary summary;

    public MonthSummary(LocalDate month, Summary summary) {
        this.month = month;
        this.summary = summary;
    }

    public static List<MonthSummary> calculateMonthSummary(List<Usage> logs, PriceConfig priceConfig) {
        LocalDate start = logs.get(0).getDateTimeInterval().getStartDate().toLocalDate().withDayOfMonth(1);
        LocalDate end = logs.get(logs.size() - 1).getDateTimeInterval().getStartDate().toLocalDate();

        List<MonthSummary> list = new LinkedList<>();

        while(start.compareTo(end) <= 0) {
            final LocalDate month = start;
            List<Usage> filtered = logs.stream().filter(u -> u.getDateTimeInterval().isInSameMonth(month)).collect(Collectors.toList());
            Summary summary = Summary.calculateSummary(filtered, priceConfig);
            list.add(new MonthSummary(start, summary));
            start = start.plusMonths(1);
        }

        return list;
    }

    public LocalDate getMonth() {
        return month;
    }

    public void setMonth(LocalDate month) {
        this.month = month;
    }

    public Summary getSummary() {
        return summary;
    }

    public void setSummary(Summary summary) {
        this.summary = summary;
    }
}
