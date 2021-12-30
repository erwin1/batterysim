package io.github.erwin1.batterysim.simulator;

import io.github.erwin1.batterysim.usage.Usage;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.LinkedList;
import java.util.List;

public class BatterySimulator {
    private final BigDecimal maxBatteryCapacity;
    BigDecimal chargeEfficiency = new BigDecimal(1.0);
    BigDecimal dechargeEfficiency = new BigDecimal(1.0);

    public BatterySimulator(BigDecimal maxBatteryCapacity) {
        this.maxBatteryCapacity = maxBatteryCapacity;
    }

    public BatterySimulator(BigDecimal maxBatteryCapacity, BigDecimal chargeEffiency, BigDecimal dechargeEfficiency) {
        this.maxBatteryCapacity = maxBatteryCapacity;
        this.chargeEfficiency = chargeEffiency;
        this.dechargeEfficiency = dechargeEfficiency;
    }

    public List<Usage> simulateBattery(List<Usage> usages) {
        BigDecimal batteryLevel = new BigDecimal(0);

        List<Usage> usagesWithBattery = new LinkedList<>();

        for (Usage usage : usages) {
            Usage usageWithBattery = useBattery(usage, batteryLevel);
            batteryLevel = usageWithBattery.getBatteryLevel();
            usagesWithBattery.add(usageWithBattery);
        }

        return usagesWithBattery;
    }

    private Usage useBattery(Usage usage, BigDecimal batteryLevel) {
        Usage newUsage = new Usage(usage);

        BigDecimal dechargeCapacity = batteryLevel;
        BigDecimal chargeCapacity = maxBatteryCapacity.subtract(batteryLevel);

        BigDecimal charge = usage.getAmountOut().multiply(chargeEfficiency).min(chargeCapacity);
        BigDecimal outDiff = charge.divide(chargeEfficiency, 2, RoundingMode.HALF_UP);

        BigDecimal decharge = usage.getAmountIn().divide(dechargeEfficiency, 2, RoundingMode.HALF_UP).min(dechargeCapacity);
        BigDecimal inDiff = decharge.multiply(dechargeEfficiency);

        BigDecimal batteryDiff = charge.subtract(decharge);

        newUsage.setAmountIn(usage.getAmountIn().subtract(inDiff).setScale(2, RoundingMode.HALF_UP));
        newUsage.setAmountOut(usage.getAmountOut().subtract(outDiff).setScale(2, RoundingMode.HALF_UP));
        newUsage.setBatteryLevel(batteryLevel.add(batteryDiff).setScale(2, RoundingMode.HALF_UP));
        newUsage.setAmountInFromBattery(decharge.setScale(2, RoundingMode.HALF_UP));
        newUsage.setAmountOutToBattery(charge.setScale(2, RoundingMode.HALF_UP));

        return newUsage;
    }



}
