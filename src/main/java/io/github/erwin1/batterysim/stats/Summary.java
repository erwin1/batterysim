package io.github.erwin1.batterysim.stats;

import io.github.erwin1.batterysim.usage.TariffType;
import io.github.erwin1.batterysim.usage.Usage;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

public class Summary {
    private BigDecimal amountInDay;
    private BigDecimal amountOutDay;
    private BigDecimal amountInNight;
    private BigDecimal amountOutNight;
    private BigDecimal amountInFromBattery;
    private BigDecimal amountOutToBattery;
    private PriceConfig priceConfig;

    public static Summary calculateSummary(List<Usage> logs, PriceConfig priceConfig) {
        BigDecimal totalInDay = BigDecimal.valueOf(logs.stream()
                .filter(u -> u.getTariffType().equals(TariffType.DAY))
                .mapToDouble(u -> u.getAmountIn().doubleValue()).sum());

        BigDecimal totalInNight = BigDecimal.valueOf(logs.stream()
                .filter(u -> u.getTariffType().equals(TariffType.NIGHT))
                .mapToDouble(u -> u.getAmountIn().doubleValue()).sum());

        BigDecimal totalOutDag = BigDecimal.valueOf(logs.stream()
                .filter(u -> u.getTariffType().equals(TariffType.DAY))
                .mapToDouble(u -> u.getAmountOut().doubleValue()).sum());

        BigDecimal totalOutNacht = BigDecimal.valueOf(logs.stream()
                .filter(u -> u.getTariffType().equals(TariffType.NIGHT))
                .mapToDouble(u -> u.getAmountOut().doubleValue()).sum());

        BigDecimal inFromBattery = BigDecimal.valueOf(logs.stream()
                .mapToDouble(u -> u.getAmountInFromBattery().doubleValue()).sum());

        BigDecimal outToBattery = BigDecimal.valueOf(logs.stream()
                .mapToDouble(u -> u.getAmountOutToBattery().doubleValue()).sum());

        return new Summary(totalInDay, totalOutDag, totalInNight, totalOutNacht,inFromBattery, outToBattery, priceConfig);
    }

    public Summary(BigDecimal amountInDay, BigDecimal amountOutDay, BigDecimal amountInNight, BigDecimal amountOutNight,
                   BigDecimal amountInFromBattery, BigDecimal amountOutToBattery,
                   PriceConfig priceConfig) {
        this.amountInDay = amountInDay;
        this.amountOutDay = amountOutDay;
        this.amountInNight = amountInNight;
        this.amountOutNight = amountOutNight;
        this.amountInFromBattery = amountInFromBattery;
        this.amountOutToBattery = amountOutToBattery;
        this.priceConfig = priceConfig;
    }

    public BigDecimal getAmountInDay() {
        return amountInDay;
    }

    public BigDecimal getAmountOutDay() {
        return amountOutDay;
    }

    public BigDecimal getAmountInNight() {
        return amountInNight;
    }

    public BigDecimal getAmountOutNight() {
        return amountOutNight;
    }

    public PriceConfig getPriceConfig() {
        return priceConfig;
    }

    public BigDecimal getAmountInFromBattery() {
        return amountInFromBattery;
    }

    public BigDecimal getAmountOutToBattery() {
        return amountOutToBattery;
    }

    public BigDecimal getTotalAmountIn() {
        return getAmountInDay().add(getAmountInNight());
    }

    public BigDecimal getTotalAmountOut() {
        return getAmountOutDay().add(getAmountOutNight());
    }

    public BigDecimal getPriceIn() {
        return getAmountInDay().multiply(priceConfig.getPriceInDay())
                .add(getAmountInNight().multiply(priceConfig.getPriceInNight()))
                .setScale(2, RoundingMode.HALF_UP);
    }

    public BigDecimal getPriceOut() {
        return getAmountOutDay().multiply(priceConfig.getPriceOutDay())
                .add(getAmountOutNight().multiply(priceConfig.getPriceOutNight()))
                .setScale(2, RoundingMode.HALF_UP)
                .negate();
    }

    public BigDecimal getTotalPrice() {
        return getPriceIn().add(getPriceOut());
    }

}
