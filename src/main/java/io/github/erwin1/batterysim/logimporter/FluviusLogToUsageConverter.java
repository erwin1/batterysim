package io.github.erwin1.batterysim.logimporter;

import io.github.erwin1.batterysim.usage.Interval;
import io.github.erwin1.batterysim.usage.TariffType;
import io.github.erwin1.batterysim.usage.Usage;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.temporal.ChronoUnit;
import java.util.LinkedList;
import java.util.List;

public class FluviusLogToUsageConverter {

    public List<Usage> convert(List<FluviusLog> list) throws IOException {
        List<Usage> usages = new LinkedList<>();
        Usage prevUsage = null;
        for(FluviusLog fluviusLog : list) {
            if (prevUsage != null
                    && prevUsage.getDateTimeInterval().getStartDate().equals(fluviusLog.getStartDateTime())) {
                addAmount(prevUsage, fluviusLog.getRegister(), fluviusLog.getVolume());
            } else {
                Usage usage = new Usage();
                usage.setTariffType(registerToTariffType(fluviusLog.getRegister()));
                usage.setDateTimeInterval(new Interval(fluviusLog.getStartDateTime(), 15, ChronoUnit.MINUTES));
                addAmount(usage, fluviusLog.getRegister(), fluviusLog.getVolume());
                usages.add(usage);
                prevUsage = usage;
            }
        }
        return usages;
    }

    private TariffType registerToTariffType(FluviusLog.Register register) {
        switch(register) {
            case INJECTIE_DAG:
            case AFNAME_DAG:
                return TariffType.DAY;
            case INJECTIE_NACHT:
            case AFNAME_NACHT:
                return TariffType.NIGHT;
        }
        return null;
    }

    private void addAmount(Usage usage, FluviusLog.Register register, BigDecimal volume) {
        switch(register) {
            case INJECTIE_DAG:
            case INJECTIE_NACHT:
                if (usage.getAmountOut() == null) {
                    usage.setAmountOut(volume);
                } else {
                    usage.setAmountOut(usage.getAmountOut().add(volume));
                }
                break;
            case AFNAME_DAG:
            case AFNAME_NACHT:
                if (usage.getAmountIn() == null) {
                    usage.setAmountIn(volume);
                } else {
                    usage.setAmountIn(usage.getAmountIn().add(volume));
                }
                break;
        }
    }


}
