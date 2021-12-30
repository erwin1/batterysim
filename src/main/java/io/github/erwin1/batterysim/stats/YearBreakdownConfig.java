package io.github.erwin1.batterysim.stats;

import java.math.BigDecimal;
import java.time.Month;
import java.util.HashMap;
import java.util.Map;

public class YearBreakdownConfig {

    private Map<Month, BigDecimal> mapping = new HashMap<>();

    public BigDecimal getPercentageForMonth(Month month) {
        return mapping.get(month);
    }

    public YearBreakdownConfig setPercentageForMonth(Month month, BigDecimal pct) {
        mapping.put(month, pct);
        return this;
    }

}
