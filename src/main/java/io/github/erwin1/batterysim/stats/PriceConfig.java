package io.github.erwin1.batterysim.stats;

import java.math.BigDecimal;

public class PriceConfig {
    private BigDecimal priceInDay;
    private BigDecimal priceOutDay;
    private BigDecimal priceInNight;
    private BigDecimal priceOutNight;

    public PriceConfig(BigDecimal priceInDay, BigDecimal priceOutDay, BigDecimal priceInNight, BigDecimal priceOutNight) {
        this.priceInDay = priceInDay;
        this.priceOutDay = priceOutDay;
        this.priceInNight = priceInNight;
        this.priceOutNight = priceOutNight;
    }

    public BigDecimal getPriceInDay() {
        return priceInDay;
    }

    public BigDecimal getPriceOutDay() {
        return priceOutDay;
    }

    public BigDecimal getPriceInNight() {
        return priceInNight;
    }

    public BigDecimal getPriceOutNight() {
        return priceOutNight;
    }
}
