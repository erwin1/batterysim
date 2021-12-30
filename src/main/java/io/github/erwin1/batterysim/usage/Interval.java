package io.github.erwin1.batterysim.usage;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalUnit;

public class Interval {
    private LocalDateTime startDate;
    private LocalDateTime endDate;

    public Interval(LocalDateTime startDate, LocalDateTime endDate) {
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public Interval(LocalDateTime startDate, int duration, TemporalUnit unit) {
        this.startDate = startDate;
        this.endDate = startDate.plus(duration, unit);
    }

    public LocalDateTime getStartDate() {
        return startDate;
    }

    public LocalDateTime getEndDate() {
        return endDate;
    }

    public boolean isInSameMonth(LocalDate month) {
        return startDate.getYear() == month.getYear() && startDate.getMonth().equals(month.getMonth());
    }

    public BigDecimal relativePerHour() {
        return new BigDecimal(ChronoUnit.MINUTES.between(startDate, endDate) / 60.0);
    }
}
