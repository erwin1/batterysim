package io.github.erwin1.batterysim.stats;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.Month;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.fail;

public class YearBreakdownTest {
    YearBreakdown yearBreakdown;

    @BeforeMethod
    public void setup() {
        YearBreakdownConfig config = new YearBreakdownConfig()
                .setPercentageForMonth(Month.JANUARY, new BigDecimal("2.00"))
                .setPercentageForMonth(Month.FEBRUARY, new BigDecimal("2.00"))
                .setPercentageForMonth(Month.MARCH, new BigDecimal("3.00"))
                .setPercentageForMonth(Month.APRIL, new BigDecimal("3.00"))
                .setPercentageForMonth(Month.MAY, new BigDecimal("4.00"))
                .setPercentageForMonth(Month.JUNE, new BigDecimal("4.00"))
                .setPercentageForMonth(Month.JULY, new BigDecimal("22.00"))
                .setPercentageForMonth(Month.AUGUST, new BigDecimal("20.00"))
                .setPercentageForMonth(Month.SEPTEMBER, new BigDecimal("20.00"))
                .setPercentageForMonth(Month.OCTOBER, new BigDecimal("8.00"))
                .setPercentageForMonth(Month.NOVEMBER, new BigDecimal("6.00"))
                .setPercentageForMonth(Month.DECEMBER, new BigDecimal("6.00"));
        yearBreakdown = new YearBreakdown(config);
    }

    @Test
    public void testFullYear() {
        BigDecimal pct = yearBreakdown.calculatePercentageOfFullYear(LocalDateTime.parse("2021-01-01T00:00:00"), LocalDateTime.parse("2021-12-31T23:59:59"));
        assertEquals(pct, new BigDecimal("100.00"));
    }

    @Test
    public void testJanuary() {
        BigDecimal pct = yearBreakdown.calculatePercentageOfFullYear(LocalDateTime.parse("2021-01-01T00:00:00"), LocalDateTime.parse("2021-01-31T23:59:59"));
        assertEquals(pct, new BigDecimal("2.00"));
    }

    @Test
    public void testFebruary() {
        BigDecimal pct = yearBreakdown.calculatePercentageOfFullYear(LocalDateTime.parse("2021-02-01T00:00:00"), LocalDateTime.parse("2021-02-28T23:59:59"));
        assertEquals(pct, new BigDecimal("2.00"));
    }

    @Test
    public void testMarch() {
        BigDecimal pct = yearBreakdown.calculatePercentageOfFullYear(LocalDateTime.parse("2021-03-01T00:00:00"), LocalDateTime.parse("2021-03-31T23:59:59"));
        assertEquals(pct, new BigDecimal("3.00"));
    }

    @Test
    public void testApril() {
        BigDecimal pct = yearBreakdown.calculatePercentageOfFullYear(LocalDateTime.parse("2021-04-01T00:00:00"), LocalDateTime.parse("2021-04-30T23:59:59"));
        assertEquals(pct, new BigDecimal("3.00"));
    }

    @Test
    public void testMay() {
        BigDecimal pct = yearBreakdown.calculatePercentageOfFullYear(LocalDateTime.parse("2021-05-01T00:00:00"), LocalDateTime.parse("2021-05-31T23:59:59"));
        assertEquals(pct, new BigDecimal("4.00"));
    }

    @Test
    public void testJune() {
        BigDecimal pct = yearBreakdown.calculatePercentageOfFullYear(LocalDateTime.parse("2021-06-01T00:00:00"), LocalDateTime.parse("2021-06-30T23:59:59"));
        assertEquals(pct, new BigDecimal("4.00"));
    }

    @Test
    public void testJuly() {
        BigDecimal pct = yearBreakdown.calculatePercentageOfFullYear(LocalDateTime.parse("2021-07-01T00:00:00"), LocalDateTime.parse("2021-07-31T23:59:59"));
        assertEquals(pct, new BigDecimal("22.00"));
    }

    @Test
    public void testAugust() {
        BigDecimal pct = yearBreakdown.calculatePercentageOfFullYear(LocalDateTime.parse("2021-08-01T00:00:00"), LocalDateTime.parse("2021-08-31T23:59:59"));
        assertEquals(pct, new BigDecimal("20.00"));
    }

    @Test
    public void testSeptember() {
        BigDecimal pct = yearBreakdown.calculatePercentageOfFullYear(LocalDateTime.parse("2021-09-01T00:00:00"), LocalDateTime.parse("2021-09-30T23:59:59"));
        assertEquals(pct, new BigDecimal("20.00"));
    }

    @Test
    public void testOctober() {
        BigDecimal pct = yearBreakdown.calculatePercentageOfFullYear(LocalDateTime.parse("2021-10-01T00:00:00"), LocalDateTime.parse("2021-10-31T23:59:59"));
        assertEquals(pct, new BigDecimal("8.00"));
    }

    @Test
    public void testNovember() {
        BigDecimal pct = yearBreakdown.calculatePercentageOfFullYear(LocalDateTime.parse("2021-11-01T00:00:00"), LocalDateTime.parse("2021-11-30T23:59:59"));
        assertEquals(pct, new BigDecimal("6.00"));
    }

    @Test
    public void testDecember() {
        BigDecimal pct = yearBreakdown.calculatePercentageOfFullYear(LocalDateTime.parse("2021-12-01T00:00:00"), LocalDateTime.parse("2021-12-31T23:59:59"));
        assertEquals(pct, new BigDecimal("6.00"));
    }

    @Test
    public void testMoreThanOneYear() {
        BigDecimal pct = yearBreakdown.calculatePercentageOfFullYear(LocalDateTime.parse("2020-12-01T00:00:00"), LocalDateTime.parse("2022-01-31T23:59:59"));
        assertEquals(pct, new BigDecimal("108.00"));
    }

    @Test
    public void testHalfAMonth() {
        BigDecimal pct = yearBreakdown.calculatePercentageOfFullYear(LocalDateTime.parse("2021-06-01T00:00:00"), LocalDateTime.parse("2021-06-15T23:59:59"));
        assertEquals(pct, new BigDecimal("2.00"));
    }

    @Test
    public void testAThirdOfAMonth() {
        BigDecimal pct = yearBreakdown.calculatePercentageOfFullYear(LocalDateTime.parse("2021-11-01T00:00:00"), LocalDateTime.parse("2021-11-10T23:59:59"));
        assertEquals(pct, new BigDecimal("2.00"));
    }

    @Test
    public void testAThirtiedOfAMonth() {
        BigDecimal pct = yearBreakdown.calculatePercentageOfFullYear(LocalDateTime.parse("2021-11-01T00:00:00"), LocalDateTime.parse("2021-11-01T23:59:59"));
        assertEquals(pct, new BigDecimal("0.20"));
    }

    @Test
    public void testMultipleMonths() {
        BigDecimal pct = yearBreakdown.calculatePercentageOfFullYear(LocalDateTime.parse("2021-11-30T00:00:00"), LocalDateTime.parse("2021-12-15T23:59:59"));
        assertEquals(pct, new BigDecimal("3.10"));//0.2 + 2.9
    }


    @Test(expectedExceptions = RuntimeException.class)
    public void testStartAfterEnd() {
        yearBreakdown.calculatePercentageOfFullYear(LocalDateTime.parse("2021-01-01T00:00:00"), LocalDateTime.parse("2020-12-31T23:59:59"));
        fail();
    }
}
