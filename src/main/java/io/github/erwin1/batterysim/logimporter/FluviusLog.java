package io.github.erwin1.batterysim.logimporter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class FluviusLog {

    public enum Register {
        AFNAME_DAG,
        INJECTIE_DAG,
        AFNAME_NACHT,
        INJECTIE_NACHT;

        public static Register parse(String s) {
            switch (s) {
                case "Afname Dag":
                    return AFNAME_DAG;
                case "Injectie Dag":
                    return INJECTIE_DAG;
                case "Afname Nacht":
                    return AFNAME_NACHT;
                case "Injectie Nacht":
                    return INJECTIE_NACHT;
            }
            return null;
        }
    }

    private String date;
    private String time;
    private Register register;
    private BigDecimal volume;

    public FluviusLog() {
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public Register getRegister() {
        return register;
    }

    public void setRegister(Register register) {
        this.register = register;
    }

    public BigDecimal getVolume() {
        return volume;
    }

    public void setVolume(BigDecimal volume) {
        this.volume = volume;
    }

    public LocalDateTime getStartDateTime() {
        return LocalDateTime.parse(getDate()+" "+getTime(), DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss"));
    }

    @Override
    public String toString() {
        return "FluviusLog{" +
                "date='" + date + '\'' +
                ", time='" + time + '\'' +
                ", register=" + register +
                ", volume=" + volume +
                '}';
    }
}
