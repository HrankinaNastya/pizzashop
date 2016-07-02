package com.hrankina.pizzashop.util;

import java.time.LocalTime;

/**
 * creation date 25.06.2016
 *
 * @author A.Hrankina
 */
public final class MySQLIntervalWrapper {

    private MySQLIntervalWrapper() {
        //there are no constructor
    }

    public static String getDayToSecondInterval(CharSequence value) {
        return getDayToSecondInterval(LocalTime.parse(value));
    }

    public static String getDayToSecondInterval(LocalTime value) {
        StringBuilder buf = new StringBuilder(22);
        int hourValue = value.getHour();
        int minuteValue = value.getMinute();
        int secondValue = value.getSecond();
        int nanoValue = value.getNano();
        buf.append("+00 ");
        buf.append(hourValue < 10 ? "0" : "").append(hourValue).append(minuteValue < 10 ? ":0" : ":").append(minuteValue);
        buf.append(secondValue < 10 ? ":0" : ":").append(secondValue);
        buf.append('.').append(Integer.toString((nanoValue) + 1000_000_000).substring(1));
        return buf.toString();
    }

    public static String getTimeFromInterval(String s) {
        Long days = Long.parseLong(s.substring(0, s.indexOf(" ")));
        String splitter[] = s.substring(s.indexOf(" ") + 1, s.lastIndexOf(":")).split(":");
        if ((splitter.length == 2) || (days != null)) {
            String hs = splitter[0];
            String min = splitter[1];
            if ((Long.parseLong(hs) == 0) && (days.longValue() > 0)) {
                return days.toString() + " дней";
            }
            return (hs.length() == 1 ? "0" + hs : hs) + ":" + (min.length() == 1 ? "0" + min : min);
        }
        return "00:00";
    }

}
