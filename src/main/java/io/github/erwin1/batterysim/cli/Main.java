package io.github.erwin1.batterysim.cli;

import io.github.erwin1.batterysim.logimporter.FluviusLog;
import io.github.erwin1.batterysim.logimporter.FluviusLogParser;
import io.github.erwin1.batterysim.logimporter.FluviusLogToUsageConverter;
import io.github.erwin1.batterysim.simulator.BatterySimulator;
import io.github.erwin1.batterysim.stats.YearBreakdown;
import io.github.erwin1.batterysim.stats.PriceConfig;
import io.github.erwin1.batterysim.stats.Summary;
import io.github.erwin1.batterysim.usage.Usage;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.List;

public class Main {
    private static final PriceConfig priceConfig = new PriceConfig(
            new BigDecimal("0.35"),
            new BigDecimal("0.045"),
            new BigDecimal("0.25"),
            new BigDecimal("0.0225"));


    public static void main(String[] args) throws IOException {
        BatterySimulator batterySimulator = new BatterySimulator(new BigDecimal(10), new BigDecimal(0.9), new BigDecimal(0.9));
        FluviusLogParser logParser = new FluviusLogParser();
        YearBreakdown yearBreakdown = new YearBreakdown();
        FluviusLogToUsageConverter logToUsageConverter = new FluviusLogToUsageConverter();

        List<FluviusLog> logs = logParser.parseUsages(Paths.get(args[0]));

        List<Usage> usages = logToUsageConverter.convert(logs);

        List<Usage> usagesWithBattery = batterySimulator.simulateBattery(usages);

        Summary withoutBattery = Summary.calculateSummary(usages, priceConfig);
        Summary withBattery = Summary.calculateSummary(usagesWithBattery, priceConfig);

        System.out.println("WITHOUT BATTERY");
        System.out.println("IN:  DAG: "+withoutBattery.getAmountInDay()+" NACHT: "+withoutBattery.getAmountInNight()+" "+withoutBattery.getPriceIn()+" EUR");
        System.out.println("OUT: DAG: "+withoutBattery.getAmountOutDay()+" NACHT: "+withoutBattery.getAmountOutNight()+" "+withoutBattery.getPriceOut()+" EUR");
        System.out.println("TO PAY: "+withoutBattery.getTotalPrice());
        System.out.println("\nWITH BATTERY");
        System.out.println("IN:  DAG: "+withBattery.getAmountInDay()+" NACHT: "+withBattery.getAmountInNight()+" "+withBattery.getPriceIn()+" EUR");
        System.out.println("OUT: DAG: "+withBattery.getAmountOutDay()+" NACHT: "+withBattery.getAmountOutNight()+" "+withBattery.getPriceOut()+" EUR");
        System.out.println("TO PAY: "+withBattery.getTotalPrice());
        System.out.println();
        BigDecimal diff = withoutBattery.getTotalPrice().subtract(withBattery.getTotalPrice());
        System.out.println("DIFF "+diff);


        LocalDateTime first = logs.get(0).getStartDateTime();
        LocalDateTime last = logs.get(logs.size() - 1).getStartDateTime();
        BigDecimal pct = yearBreakdown.calculatePercentageOfFullYear(first, last);
        System.out.println("LOGS MAKE UP "+pct+"% OF YEARLY TOTAL");

        BigDecimal yearEstimate = diff.divide(pct, 2, RoundingMode.HALF_UP).multiply(new BigDecimal(100));
        System.out.println("ESTIMATED DIFF IN 1 YEAR "+yearEstimate);

        BigDecimal batteryPrice = new BigDecimal("4000");
        System.out.println("ESTIMATED ROI IN "+batteryPrice.divide(yearEstimate, 2, RoundingMode.HALF_UP)+" YEARS");
    }
}
