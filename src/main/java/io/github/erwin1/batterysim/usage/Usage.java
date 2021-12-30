package io.github.erwin1.batterysim.usage;

import java.math.BigDecimal;

public class Usage {
    private Interval dateTimeInterval;
    private TariffType tariffType;
    private BigDecimal amountIn;
    private BigDecimal amountOut;
    private BigDecimal batteryLevel;
    private BigDecimal amountInFromBattery;
    private BigDecimal amountOutToBattery;

    public Usage() {
        batteryLevel = BigDecimal.ZERO;
        amountInFromBattery = BigDecimal.ZERO;
        amountOutToBattery = BigDecimal.ZERO;
    }

    public Usage(Usage usage) {
        this.dateTimeInterval = usage.dateTimeInterval;
        this.tariffType = usage.tariffType;
        this.amountIn = usage.amountIn;
        this.amountOut = usage.amountOut;
        this.batteryLevel = usage.batteryLevel;
    }

    public Interval getDateTimeInterval() {
        return dateTimeInterval;
    }

    public void setDateTimeInterval(Interval dateTimeInterval) {
        this.dateTimeInterval = dateTimeInterval;
    }

    public TariffType getTariffType() {
        return tariffType;
    }

    public void setTariffType(TariffType tariffType) {
        this.tariffType = tariffType;
    }

    public BigDecimal getAmountIn() {
        return amountIn;
    }

    public void setAmountIn(BigDecimal amountIn) {
        this.amountIn = amountIn;
    }

    public BigDecimal getAmountOut() {
        return amountOut;
    }

    public void setAmountOut(BigDecimal amountOut) {
        this.amountOut = amountOut;
    }

    public BigDecimal getBatteryLevel() {
        return batteryLevel;
    }

    public void setBatteryLevel(BigDecimal batteryLevel) {
        this.batteryLevel = batteryLevel;
    }

    public BigDecimal getAmountInFromBattery() {
        return amountInFromBattery;
    }

    public void setAmountInFromBattery(BigDecimal amountInFromBattery) {
        this.amountInFromBattery = amountInFromBattery;
    }

    public BigDecimal getAmountOutToBattery() {
        return amountOutToBattery;
    }

    public void setAmountOutToBattery(BigDecimal amountOutToBattery) {
        this.amountOutToBattery = amountOutToBattery;
    }
}
