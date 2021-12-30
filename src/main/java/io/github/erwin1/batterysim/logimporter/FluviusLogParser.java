package io.github.erwin1.batterysim.logimporter;

import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.LinkedList;
import java.util.List;

public class FluviusLogParser {
    public List<FluviusLog> parseUsages(Path file) throws IOException {
        List<String> list = Files.readAllLines(file);
        list.remove(0);
        return parseUsages(list);
    }

    List<FluviusLog> parseUsages(List<String> list) throws IOException {
        List<FluviusLog> logs = new LinkedList<>();
        for(String line : list) {
            String[] l = line.split(";");
            String volume = l[8];
            if (volume.length() == 0) {
                volume = "0.000";
            }
            FluviusLog log = new FluviusLog();
            log.setDate(l[0]);
            log.setTime(l[1]);
            log.setRegister(FluviusLog.Register.parse(l[7]));
            log.setVolume(new BigDecimal(volume.replaceAll(",", ".")));
            logs.add(log);
        }
        return logs;
    }


}
