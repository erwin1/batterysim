package io.github.erwin1.batterysim.logimporter.usage;

import io.github.erwin1.batterysim.usage.Interval;
import io.github.erwin1.batterysim.usage.TariffType;
import io.github.erwin1.batterysim.usage.Usage;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.LinkedList;
import java.util.List;

public class UsageMother {

    public static List<Usage> createUsages() {
        List<Usage> logs = new LinkedList<>();
        logs.add(createUsage(LocalDateTime.parse("2021-05-21T09:00:00Z", DateTimeFormatter.ISO_DATE_TIME), TariffType.DAY, 0.5, 0.0));//0
        logs.add(createUsage(LocalDateTime.parse("2021-05-21T09:15:00Z", DateTimeFormatter.ISO_DATE_TIME), TariffType.DAY, 1.0, 1.0));//1
        logs.add(createUsage(LocalDateTime.parse("2021-05-21T09:30:00Z", DateTimeFormatter.ISO_DATE_TIME), TariffType.DAY, 1.5, 2));//2
        logs.add(createUsage(LocalDateTime.parse("2021-05-21T09:45:00Z", DateTimeFormatter.ISO_DATE_TIME), TariffType.DAY, 0.0, 1.5));//3
        logs.add(createUsage(LocalDateTime.parse("2021-05-21T10:00:00Z", DateTimeFormatter.ISO_DATE_TIME), TariffType.DAY, 0.0, 4.0));//4
        logs.add(createUsage(LocalDateTime.parse("2021-05-21T10:15:00Z", DateTimeFormatter.ISO_DATE_TIME), TariffType.NIGHT, 10.0, 0.0));//5
        logs.add(createUsage(LocalDateTime.parse("2021-05-21T10:30:00Z", DateTimeFormatter.ISO_DATE_TIME), TariffType.NIGHT, 10.0, 6.0));//6
        logs.add(createUsage(LocalDateTime.parse("2021-05-21T10:45:00Z", DateTimeFormatter.ISO_DATE_TIME), TariffType.NIGHT, 10.0, 10.0));//7
        logs.add(createUsage(LocalDateTime.parse("2021-05-21T11:00:00Z", DateTimeFormatter.ISO_DATE_TIME), TariffType.NIGHT, 0.0, 3.0));//8
        logs.add(createUsage(LocalDateTime.parse("2021-05-21T11:15:00Z", DateTimeFormatter.ISO_DATE_TIME), TariffType.DAY, 3.0, 3.0));//9
        return logs;
    }

    private static Usage createUsage(LocalDateTime date, TariffType tariffType, double in, double out) {
        Usage usage = new Usage();
        usage.setDateTimeInterval(new Interval(date, 15, ChronoUnit.MINUTES));
        usage.setTariffType(tariffType);
        usage.setAmountIn(new BigDecimal(in));
        usage.setAmountOut(new BigDecimal(out));
        return usage;
    }

}
