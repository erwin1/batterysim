package io.github.erwin1.batterysim.simulator;

import io.github.erwin1.batterysim.usage.Interval;
import io.github.erwin1.batterysim.usage.TariffType;
import io.github.erwin1.batterysim.usage.Usage;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.List;

import static org.testng.Assert.assertEquals;

public class BatterySimulatorTest {
    BigDecimal maxBatteryCapacity = new BigDecimal("5.00");
    BigDecimal chargeEffiency = new BigDecimal("0.9");
    BigDecimal dechargeEffiency = new BigDecimal("0.9");
    BatterySimulator batterySimulator;

    @BeforeMethod
    public void setup() {
        batterySimulator = new BatterySimulator(maxBatteryCapacity, chargeEffiency, dechargeEffiency);
    }

    @Test
    public void testBatterySimulation1() {
        Usage simalatedUsage =  simulateUsage(new BigDecimal("5.00"), new BigDecimal("2.0"), new BigDecimal("2.0"));
        assertLog(simalatedUsage, 0.0, 2.0, 2.78);
    }

    @Test
    public void testBatterySimulation2() {
        Usage simalatedUsage =  simulateUsage(new BigDecimal("0.00"), new BigDecimal("2.0"), new BigDecimal("2.0"));
        assertLog(simalatedUsage, 2.0, 0.0, 1.8);
    }

    @Test
    public void testBatterySimulation3() {
        Usage simalatedUsage =  simulateUsage(new BigDecimal("2.5"), new BigDecimal("2.0"), new BigDecimal("2.0"));
        assertLog(simalatedUsage, 0.0, 0.0, 2.08);
    }

    @Test
    public void testBatterySimulation4() {
        Usage simalatedUsage =  simulateUsage(new BigDecimal("2.5"), new BigDecimal("5.0"), new BigDecimal("0.0"));
        assertLog(simalatedUsage, 2.75, 0.0, 0.00);
    }

    @Test
    public void testBatterySimulation5() {
        Usage simalatedUsage =  simulateUsage(new BigDecimal("2.5"), new BigDecimal("0.0"), new BigDecimal("5.0"));
        assertLog(simalatedUsage, 0.0, 2.22, 5.0);
    }

    @Test
    public void testBatterySimulation6() {
        Usage simalatedUsage =  simulateUsage(new BigDecimal("0.0"), new BigDecimal("0.0"), new BigDecimal("5.0"));
        assertLog(simalatedUsage, 0.0, 0.0, 4.5);
    }

    @Test
    public void testBatterySimulation7() {
        Usage simalatedUsage =  simulateUsage(new BigDecimal("1.0"), new BigDecimal("10.0"), new BigDecimal("10.0"));
        assertLog(simalatedUsage, 9.1, 5.56, 4.0);
    }

    private void assertLog(Usage log, double in, double out, double batteryCapacity) {
        assertEquals(log.getAmountIn().doubleValue(), in, "in");
        assertEquals(log.getAmountOut().doubleValue(), out, "out");
        assertEquals(log.getBatteryLevel().doubleValue(), batteryCapacity, "bat");
    }

    private Usage simulateUsage(BigDecimal startBatteryLevel, BigDecimal in, BigDecimal out) {
        List<Usage> usages = List.of(
                createUsage(LocalDateTime.parse("2021-05-21T09:00:00Z", DateTimeFormatter.ISO_DATE_TIME), TariffType.DAY, BigDecimal.ZERO, startBatteryLevel.divide(chargeEffiency, 2, RoundingMode.HALF_UP)),
                createUsage(LocalDateTime.parse("2021-05-21T09:15:00Z", DateTimeFormatter.ISO_DATE_TIME), TariffType.DAY, in, out)
        );
        List<Usage> simalatedUsages = batterySimulator.simulateBattery(usages);
        return simalatedUsages.get(1);
    }

    private Usage createUsage(LocalDateTime date, TariffType tariffType, BigDecimal in, BigDecimal out) {
        Usage usage = new Usage();
        usage.setDateTimeInterval(new Interval(date, 15, ChronoUnit.MINUTES));
        usage.setTariffType(tariffType);
        usage.setAmountIn(in);
        usage.setAmountOut(out);
        return usage;
    }
}
