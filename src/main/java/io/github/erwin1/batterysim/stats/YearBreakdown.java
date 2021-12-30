package io.github.erwin1.batterysim.stats;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.time.Month;
import java.time.YearMonth;
import java.time.temporal.ChronoUnit;

public class YearBreakdown {
    private YearBreakdownConfig config;

    /**
     * Default year breakdown
     */
    public YearBreakdown() {
        config = new YearBreakdownConfig();
        config.setPercentageForMonth(Month.JANUARY, new BigDecimal(3.63));
        config.setPercentageForMonth(Month.FEBRUARY, new BigDecimal(5.62));
        config.setPercentageForMonth(Month.MARCH, new BigDecimal(9.13));
        config.setPercentageForMonth(Month.APRIL, new BigDecimal(10.75));
        config.setPercentageForMonth(Month.MAY, new BigDecimal(11.98));
        config.setPercentageForMonth(Month.JUNE, new BigDecimal(11.77));
        config.setPercentageForMonth(Month.JULY, new BigDecimal(12.03));
        config.setPercentageForMonth(Month.AUGUST, new BigDecimal(11.39));
        config.setPercentageForMonth(Month.SEPTEMBER, new BigDecimal(9.49));
        config.setPercentageForMonth(Month.OCTOBER, new BigDecimal(7.16));
        config.setPercentageForMonth(Month.NOVEMBER, new BigDecimal(4.33));
        config.setPercentageForMonth(Month.DECEMBER, new BigDecimal(2.72));
    }

    public YearBreakdown(YearBreakdownConfig config) {
        this.config = config;
    }


    public BigDecimal calculatePercentageOfFullYear(LocalDateTime firstDate, LocalDateTime lastDate) {
        if (firstDate.compareTo(lastDate) > 0) {
            throw new RuntimeException("lastDate must be after firstDate");
        }
        BigDecimal total = BigDecimal.ZERO;
        LocalDateTime start = LocalDateTime.from(firstDate);
        while (start.compareTo(lastDate) < 0) {
            LocalDateTime end = null;
            if (inSameMonth(start, lastDate)) {
                end = lastDate;
            } else {
                end = start.withDayOfMonth(1).withHour(0).withMinute(0).withSecond(0).withNano(0)
                        .plus(1, ChronoUnit.MONTHS)
                        .minus(1, ChronoUnit.SECONDS);
            }

            BigDecimal pct = calc(start.getMonth(), calculatePercentageOfMonth(start, end));
            total = total.add(pct);

            start = start.withDayOfMonth(1).withHour(0).withMinute(0).withSecond(0).withNano(0).plus(1, ChronoUnit.MONTHS);
        }
        return total;
    }

    private BigDecimal calculatePercentageOfMonth(LocalDateTime firstDate, LocalDateTime lastDate) {
        BigDecimal daysInMonth = new BigDecimal(YearMonth.from(firstDate).lengthOfMonth());
        BigDecimal daysOfMonth = new BigDecimal(lastDate.getDayOfMonth() - firstDate.getDayOfMonth() + 1);
        return daysOfMonth.multiply(new BigDecimal(100)).divide(daysInMonth, 2, RoundingMode.HALF_UP);
    }

    private BigDecimal calc(Month month, BigDecimal percentageOfMonth) {
        BigDecimal estimatedMonthlyPercentage = config.getPercentageForMonth(month);
        return estimatedMonthlyPercentage.multiply(percentageOfMonth).divide(new BigDecimal("100.0"), 2, RoundingMode.HALF_UP);
    }

    private boolean inSameMonth(LocalDateTime start, LocalDateTime lastDate) {
        return YearMonth.from(start).equals(YearMonth.from(lastDate));
    }

}
