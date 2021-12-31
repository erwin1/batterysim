package io.github.erwin1.batterysim.stats;

import io.github.erwin1.batterysim.logimporter.usage.UsageMother;
import io.github.erwin1.batterysim.simulator.BatterySimulator;
import io.github.erwin1.batterysim.usage.Usage;
import org.testng.annotations.Test;

import java.math.BigDecimal;
import java.time.Month;
import java.util.LinkedList;
import java.util.List;

import static org.testng.Assert.assertEquals;

public class MonthSummaryTest {
    private static final PriceConfig priceConfig = new PriceConfig(
            new BigDecimal("1.0"),
            new BigDecimal("0.5"),
            new BigDecimal("0.5"),
            new BigDecimal("0.25"));

    @Test
    public void test3MonthSummary1() {
        List<Usage> usages = new LinkedList<>();
        usages.addAll(UsageMother.createUsages("2021-05-01"));
        usages.addAll(UsageMother.createUsages("2021-06-02"));
        usages.addAll(UsageMother.createUsages("2021-07-03"));
        List<MonthSummary> monthSummaries = MonthSummary.calculateMonthSummary(usages, priceConfig);
        assertEquals(monthSummaries.size(), 3);
        assertEquals(monthSummaries.get(0).getMonth().getMonth(), Month.MAY);
        assertEquals(monthSummaries.get(1).getMonth().getMonth(), Month.JUNE);
        assertEquals(monthSummaries.get(2).getMonth().getMonth(), Month.JULY);
    }

    @Test
    public void test3MonthSummary2() {
        List<Usage> usages = new LinkedList<>();
        usages.addAll(UsageMother.createUsages("2021-05-03"));
        usages.addAll(UsageMother.createUsages("2021-06-02"));
        usages.addAll(UsageMother.createUsages("2021-07-01"));
        List<MonthSummary> monthSummaries = MonthSummary.calculateMonthSummary(usages, priceConfig);
        assertEquals(monthSummaries.size(), 3);
        assertEquals(monthSummaries.get(0).getMonth().getMonth(), Month.MAY);
        assertEquals(monthSummaries.get(1).getMonth().getMonth(), Month.JUNE);
        assertEquals(monthSummaries.get(2).getMonth().getMonth(), Month.JULY);
    }


    @Test
    public void test1MonthSummary() {
        List<MonthSummary> monthSummaries = MonthSummary.calculateMonthSummary(UsageMother.createUsages("2021-12-31"), priceConfig);
        assertEquals(monthSummaries.size(), 1);
        assertEquals(monthSummaries.get(0).getMonth().getMonth(), Month.DECEMBER);
    }

}
