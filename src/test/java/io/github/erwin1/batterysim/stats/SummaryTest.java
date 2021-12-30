package io.github.erwin1.batterysim.stats;

import io.github.erwin1.batterysim.logimporter.usage.UsageMother;
import io.github.erwin1.batterysim.simulator.BatterySimulator;
import org.testng.annotations.Test;

import java.math.BigDecimal;

import static org.testng.Assert.assertEquals;

public class SummaryTest {
    BigDecimal maxBatteryCapacity = new BigDecimal("20.00");
    BigDecimal chargeEffiency = new BigDecimal("1.0");
    BigDecimal dechargeEffiency = new BigDecimal("1.0");

    private static final PriceConfig priceConfig = new PriceConfig(
            new BigDecimal("1.0"),
            new BigDecimal("0.5"),
            new BigDecimal("0.5"),
            new BigDecimal("0.25"));

    @Test
    public void testRegular() {
        Summary summary = Summary.calculateSummary(UsageMother.createUsages(), priceConfig);
        assertEquals(summary.getTotalAmountIn(), new BigDecimal("36.0"));
        assertEquals(summary.getTotalAmountOut(), new BigDecimal("30.5"));
        assertEquals(summary.getPriceIn(), new BigDecimal("21.00"));
        assertEquals(summary.getPriceOut(), new BigDecimal("-10.50"));
        assertEquals(summary.getTotalPrice(), new BigDecimal("10.50"));
    }

    @Test
    public void testWithSimulatedBattery() {
        BatterySimulator batterySimulator = new BatterySimulator(maxBatteryCapacity, chargeEffiency, dechargeEffiency);
        Summary summary = Summary.calculateSummary(batterySimulator.simulateBattery(UsageMother.createUsages()), priceConfig);
        assertEquals(summary.getTotalAmountIn(), new BigDecimal("18.5"));
        assertEquals(summary.getTotalAmountOut(), new BigDecimal("0.0"));
        assertEquals(summary.getPriceIn(), new BigDecimal("10.25"));
        assertEquals(summary.getPriceOut(), new BigDecimal("0.00"));
        assertEquals(summary.getTotalPrice(), new BigDecimal("10.25"));
    }


}
