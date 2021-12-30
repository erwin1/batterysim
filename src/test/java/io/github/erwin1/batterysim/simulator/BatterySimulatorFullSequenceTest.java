package io.github.erwin1.batterysim.simulator;

import io.github.erwin1.batterysim.logimporter.usage.UsageMother;
import io.github.erwin1.batterysim.usage.Usage;
import org.testng.annotations.Test;

import java.math.BigDecimal;
import java.util.List;

import static org.testng.Assert.assertEquals;

public class BatterySimulatorFullSequenceTest {
    BigDecimal maxBatteryCapacity = new BigDecimal("5.00");
    BigDecimal chargeEffiency = new BigDecimal("1.0");
    BigDecimal dechargeEffiency = new BigDecimal("1.0");
    BatterySimulator batterySimulator;

    @Test
    public void testBatterySimulation1() {
        batterySimulator = new BatterySimulator(maxBatteryCapacity, chargeEffiency, dechargeEffiency);
        List<Usage> simalatedUsages = batterySimulator.simulateBattery(UsageMother.createUsages());

        assertLog(simalatedUsages.get(0), 0.5, 0.0, 0.0);
        assertLog(simalatedUsages.get(1), 1.0, 0.0, 1.0);
        assertLog(simalatedUsages.get(2), 0.5, 0.0, 2.0);
        assertLog(simalatedUsages.get(3), 0.0, 0.0, 3.5);
        assertLog(simalatedUsages.get(4), 0.0, 2.5, 5.0);
        assertLog(simalatedUsages.get(5), 5.0, 0.0, 0.0);
        assertLog(simalatedUsages.get(6), 10.0, 1.0, 5.0);
        assertLog(simalatedUsages.get(7), 5.0, 10.0, 0.0);
        assertLog(simalatedUsages.get(8), 0.0, 0.0, 3.0);
        assertLog(simalatedUsages.get(9), 0.0, 1.0, 2.0);
    }

    @Test
    public void testBatterySimulation2() {
        batterySimulator = new BatterySimulator(new BigDecimal(5.0d), new BigDecimal(0.9d), new BigDecimal(0.9d));
        List<Usage> simalatedUsages = batterySimulator.simulateBattery(UsageMother.createUsages());

        assertLog(simalatedUsages.get(0), 0.5, 0.0, 0.0);
        assertLog(simalatedUsages.get(1), 1.0, 0.0, 0.9);
        assertLog(simalatedUsages.get(2), 0.69, 0.0, 1.8);
        assertLog(simalatedUsages.get(3), 0.0, 0.0, 3.15);
        assertLog(simalatedUsages.get(4), 0.0, 1.94, 5.0);
        assertLog(simalatedUsages.get(5), 5.5, 0.0, 0.0);
        assertLog(simalatedUsages.get(6), 10.0, 0.44, 5.0);
        assertLog(simalatedUsages.get(7), 5.5, 10.0, 0.0);
        assertLog(simalatedUsages.get(8), 0.0, 0.0, 2.7);
        assertLog(simalatedUsages.get(9), 0.57, 0.44, 2.3);
    }

    private void assertLog(Usage log, double in, double out, double batteryCapacity) {
        assertEquals(log.getAmountIn().doubleValue(), in, "in");
        assertEquals(log.getAmountOut().doubleValue(), out, "out");
        assertEquals(log.getBatteryLevel().doubleValue(), batteryCapacity, "bat");
    }


}
