package io.github.erwin1.batterysim.logimporter;

import io.github.erwin1.batterysim.logimporter.FluviusLog;
import io.github.erwin1.batterysim.logimporter.FluviusLogToUsageConverter;
import io.github.erwin1.batterysim.usage.TariffType;
import io.github.erwin1.batterysim.usage.Usage;
import org.testng.annotations.Test;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.LinkedList;
import java.util.List;

import static org.testng.Assert.assertEquals;

public class FluviusLogToUsageConverterTest {

    @Test
    public void testConversion() throws IOException {
        FluviusLogToUsageConverter converter = new FluviusLogToUsageConverter();
        List<Usage> usages = converter.convert(createLogs());

        assertEquals(usages.size(), 3);
        assertEquals(usages.get(0).getAmountIn(), new BigDecimal(3.0));
        assertEquals(usages.get(0).getAmountOut(), new BigDecimal(1.0));
        assertEquals(usages.get(0).getTariffType(), TariffType.DAY);
        assertEquals(usages.get(1).getAmountIn(), new BigDecimal(0.5));
        assertEquals(usages.get(1).getAmountOut(), new BigDecimal(2.0));
        assertEquals(usages.get(1).getTariffType(), TariffType.DAY);
        assertEquals(usages.get(2).getAmountIn(), new BigDecimal(1.5));
        assertEquals(usages.get(2).getAmountOut(), new BigDecimal(2.5));
        assertEquals(usages.get(2).getTariffType(), TariffType.NIGHT);
    }

    private List<FluviusLog> createLogs() {
        List<FluviusLog> logs = new LinkedList<>();
        logs.add(createLog("05-02-2021", "09:00:00", FluviusLog.Register.AFNAME_DAG, new BigDecimal(3.0)));
        logs.add(createLog("05-02-2021", "09:00:00", FluviusLog.Register.INJECTIE_DAG, new BigDecimal(1.0)));
        logs.add(createLog("05-02-2021", "09:15:00", FluviusLog.Register.AFNAME_DAG, new BigDecimal(0.5)));
        logs.add(createLog("05-02-2021", "09:15:00", FluviusLog.Register.INJECTIE_DAG, new BigDecimal(2.0)));
        logs.add(createLog("05-02-2021", "09:30:00", FluviusLog.Register.AFNAME_NACHT, new BigDecimal(1.5)));
        logs.add(createLog("05-02-2021", "09:30:00", FluviusLog.Register.INJECTIE_NACHT, new BigDecimal(2.5)));
        return logs;
    }

    private FluviusLog createLog(String date, String time, FluviusLog.Register register, BigDecimal volume) {
        FluviusLog log1 = new FluviusLog();
        log1.setVolume(volume);
        log1.setRegister(register);
        log1.setDate(date);
        log1.setTime(time);
        return log1;
    }

}
