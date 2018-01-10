package com.system.management.attendance.wams.file;

/**
 * Created by koichi on 2017/11/25.
 */

public class Utils {

    public static int parseDate(String date) {
        try {
            return Integer.parseInt(date);
        } catch (NumberFormatException e) {
        }
        return -1;
    }

    public static int parseHour(String hour) {
        try {
            int result = Integer.parseInt(hour);
            if (result >= 0) {
                return result;
            }
        } catch (NumberFormatException e) {
        }
        return -1;
    }

    public static int parseMinute(String minute) {
        try {
            int result = Integer.parseInt(minute);
            if (result >= 0 && result < 60) {
                return result;
            }
        } catch (NumberFormatException e) {
        }
        return -1;
    }
}
