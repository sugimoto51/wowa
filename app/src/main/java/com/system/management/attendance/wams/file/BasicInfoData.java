package com.system.management.attendance.wams.file;

import android.util.Log;

import java.io.BufferedWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by koichi on 2017/11/23.
 */

public class BasicInfoData {

    public static final String BASIC_INFO_ATTENDANCE_TIME = "出勤時間設定";
    public static final String BASIC_INFO_BREAK_TIME = "休憩時間設定";

    public class SpecifiedTime {
        private int mStartHour = -1;
        private int mStartMinute = -1;
        private int mEndHour = -1;
        private int mEndMinute = -1;

        SpecifiedTime() {
        }

        SpecifiedTime(int startHour, int startMinute, int endHour, int endMinute) {
            mStartHour = startHour;
            mStartMinute = startMinute;
            mEndHour = endHour;
            mEndMinute = endMinute;
        }

        public int getStartHour() {
            return mStartHour;
        }

        public int getStartMinute() {
            return mStartMinute;
        }

        public int getEndHour() {
            return mEndHour;
        }

        public int getEndMinute() {
            return mEndMinute;
        }

        public void setStartHour(int hour) {
            mStartHour = hour;
        }

        public void setStartMinute(int minute) {
            mStartMinute = minute;
        }

        public void setEndHour(int hour) {
            mEndHour = hour;
        }

        public void setEndMinute(int minute) {
            mEndMinute = minute;
        }
    }

    private static final String TAG = BasicInfoData.class.getSimpleName();

    private static final String BASIC_INFO_HEADER = "WOW勤怠基本情報,";
    private static final String BASIC_INFO_SPECIFIED = "指定";
    private static final String BASIC_INFO_LINE_FORMAT1 = "指定%d,,：,,～,,：,";
    private static final String BASIC_INFO_LINE_FORMAT2 = "指定%d,%s,：,%s,～,%s,：,%s";
    private static final String BASIC_INFO_ATTENDANCE_TIME_OBP = "指定1,9,：,00,～,18,：,00";
    private static final String BASIC_INFO_BREAK_TIME_OBP_1 = "指定1,12,：,00,～,13,：,00";
    private static final String BASIC_INFO_BREAK_TIME_OBP_2 = "指定1,18,：,00,～,18,：,30";
    private static final String BASIC_INFO_BREAK_TIME_OBP_3 = "指定1,21,：,30,～,22,：,00";
    private static final String BASIC_INFO_BREAK_TIME_OBP_4 = "指定1,26,：,00,～,27,：,00";
    private static final String BASIC_INFO_BREAK_TIME_OBP_5 = "指定1,28,：,30,～,29,：,00";
    private static final String BASIC_INFO_BREAK_TIME_OBP_6 = "指定1,32,：,30,～,33,：,00";
    private static final String BASIC_INFO_BREAK_TIME_OBP_7 = "指定1,,：,,～,,：,";
    private static final String BASIC_INFO_BREAK_TIME_OBP_8 = "指定1,,：,,～,,：,";
    private static final String BASIC_INFO_BREAK_TIME_OBP_9 = "指定1,,：,,～,,：,";
    private static final String BASIC_INFO_BREAK_TIME_OBP_10 = "指定1,,：,,～,,：,";

    private static final int BASIC_INFO_ATTENDANCE_TIME_ITEM_MAX = 5;
    private static final int BASIC_INFO_BREAK_TIME_ITEM_MAX = 3;
    private static final int BASIC_INFO_BREAK_TIME_LINE_MAX = 10;

    private static HashMap<Integer,SpecifiedTime> attendanceTimes;
    private static HashMap<Integer,ArrayList> breakTimes;

    BasicInfoData(){
        attendanceTimes = new HashMap<Integer, SpecifiedTime>();
        breakTimes = new HashMap<Integer, ArrayList>();
    }

    public static SpecifiedTime getBasicInfoAttendanceTime(int index) {
        if (index >= 1 && index <= BASIC_INFO_ATTENDANCE_TIME_ITEM_MAX) {
            return attendanceTimes.get(index);
        } else {
            return null;
        }
    }

    public static SpecifiedTime[] getBasicInfoBreakTime(int index) {
        if (index >= 1 && index <= BASIC_INFO_BREAK_TIME_ITEM_MAX) {
            ArrayList breakTime = breakTimes.get(index);
            if (breakTime != null) {
                return (SpecifiedTime[]) breakTime.toArray(new SpecifiedTime[breakTime.size()]);
            }
        }

        return null;
    }

    public boolean setBasicInfoAttendanceTime(String index, String startHour, String startMinute,
                                              String endHour, String endMinute) {
        int i = parseAttendanceIndex(index);
        if (i != -1) {
            if (startHour.length() == 0 && startMinute.length() == 0 &&
                    endHour.length() == 0 && endMinute.length() == 0) {
                attendanceTimes.put(i, new SpecifiedTime());
            } else {
                int startH = Utils.parseHour(startHour);
                int startM = Utils.parseHour(startMinute);
                int endH = Utils.parseHour(endHour);
                int endM = Utils.parseHour(endMinute);
                if (startH == -1 || startM == -1 || endH == -1 || endM == -1) {
                    Log.e(TAG, "invalid times. startHour: " + startH + " startMinute: " + startM +
                            " endHour: " + endH + " endMinute: " + endM);
                    return false;
                }

                SpecifiedTime time = new SpecifiedTime(startH, startM, endH, endM);
                attendanceTimes.put(i, time);
            }
            return true;
        } else {
            Log.e(TAG, "invalid index number: " + index);
            return false;
        }
    }

    public boolean setBasicInfoBreakTime(String index, String startHour, String startMinute,
                                              String endHour, String endMinute) {
        int i = parseBreakTimeIndex(index, BASIC_INFO_BREAK_TIME_ITEM_MAX);
        if (i != -1) {
            SpecifiedTime time;
            if (startHour.length() == 0 && startMinute.length() == 0 &&
                    endHour.length() == 0 && endMinute.length() == 0) {
                time = new SpecifiedTime();
            } else {
                int startH = Utils.parseHour(startHour);
                int startM = Utils.parseHour(startMinute);
                int endH = Utils.parseHour(endHour);
                int endM = Utils.parseHour(endMinute);
                if (startH == -1 || startM == -1 || endH == -1 || endM == -1) {
                    Log.e(TAG, "invalid times. startHour: " + startH + " startMinute: " + startM +
                            " endHour: " + endH + " endMinute: " + endM);
                    return false;
                }

                time = new SpecifiedTime(startH, startM, endH, endM);
            }

            ArrayList<SpecifiedTime> breakTime = breakTimes.get(i);
            if (breakTime == null) {
                breakTime = new ArrayList<SpecifiedTime>();
            }
            breakTime.add(time);

            breakTimes.put(i, breakTime);

            return true;
        } else {
            Log.e(TAG, "invalid index number: " + index);
            return false;
        }
    }

    public static void writeBasicInfoFileHeader(BufferedWriter bufferedWriter, String src,
                                                String dir) throws IOException {
        if (src.startsWith(BASIC_INFO_HEADER)) {
            src = BASIC_INFO_HEADER + dir;
        }
        bufferedWriter.write(src);
        bufferedWriter.newLine();
    }

    public static void writeBasicInfoFileHeader(BufferedWriter bufferedWriter, String dir)
            throws IOException {
        bufferedWriter.write(BASIC_INFO_HEADER + dir);
        bufferedWriter.newLine();
        bufferedWriter.write(BASIC_INFO_ATTENDANCE_TIME);
        bufferedWriter.newLine();
    }

    public static void writeBasicInfoFileInitData(BufferedWriter bufferedWriter)
            throws IOException {

        bufferedWriter.write(BASIC_INFO_ATTENDANCE_TIME_OBP);
        bufferedWriter.newLine();

        for (int i = 2; i <= BASIC_INFO_ATTENDANCE_TIME_ITEM_MAX; i++) {
            bufferedWriter.write(String.format(BASIC_INFO_LINE_FORMAT1, i));
            bufferedWriter.newLine();
        }

        bufferedWriter.newLine();

        bufferedWriter.write(BASIC_INFO_BREAK_TIME);
        bufferedWriter.newLine();

        bufferedWriter.write(BASIC_INFO_BREAK_TIME_OBP_1);
        bufferedWriter.newLine();
        bufferedWriter.write(BASIC_INFO_BREAK_TIME_OBP_2);
        bufferedWriter.newLine();
        bufferedWriter.write(BASIC_INFO_BREAK_TIME_OBP_3);
        bufferedWriter.newLine();
        bufferedWriter.write(BASIC_INFO_BREAK_TIME_OBP_4);
        bufferedWriter.newLine();
        bufferedWriter.write(BASIC_INFO_BREAK_TIME_OBP_5);
        bufferedWriter.newLine();
        bufferedWriter.write(BASIC_INFO_BREAK_TIME_OBP_6);
        bufferedWriter.newLine();
        bufferedWriter.write(BASIC_INFO_BREAK_TIME_OBP_7);
        bufferedWriter.newLine();
        bufferedWriter.write(BASIC_INFO_BREAK_TIME_OBP_8);
        bufferedWriter.newLine();
        bufferedWriter.write(BASIC_INFO_BREAK_TIME_OBP_9);
        bufferedWriter.newLine();
        bufferedWriter.write(BASIC_INFO_BREAK_TIME_OBP_10);
        bufferedWriter.newLine();

        for (int i = 2; i <= BASIC_INFO_BREAK_TIME_ITEM_MAX; i++) {
            for (int j = 1; j <= BASIC_INFO_BREAK_TIME_LINE_MAX; j++) {
                bufferedWriter.write(String.format(BASIC_INFO_LINE_FORMAT1, i));
                bufferedWriter.newLine();
            }
        }
    }

    public static void writeBasicInfoFileData(BufferedWriter bufferedWriter)
            throws IOException {

        for (int i = 1; i <= BASIC_INFO_ATTENDANCE_TIME_ITEM_MAX; i++) {
            SpecifiedTime time = getBasicInfoAttendanceTime(i);
            bufferedWriter.write(formatLineData(time, i));
            bufferedWriter.newLine();
        }

        bufferedWriter.newLine();

        bufferedWriter.write(BASIC_INFO_BREAK_TIME);
        bufferedWriter.newLine();

        for (int i = 1; i <= BASIC_INFO_BREAK_TIME_ITEM_MAX; i++) {
            SpecifiedTime[] times = getBasicInfoBreakTime(i);
            for (SpecifiedTime time : times) {
                bufferedWriter.write(formatLineData(time, i));
                bufferedWriter.newLine();
            }
        }
    }

    private static String getDataTime(int time) {
        return time == -1 ? "" : String.valueOf(time);
    }

    private static String formatLineData(SpecifiedTime time, int index) {
        String startH = getDataTime(time.getStartHour());
        String startM = getDataTime(time.getStartMinute());
        String endH = getDataTime(time.getEndHour());
        String endM = getDataTime(time.getEndMinute());
        return String.format(BASIC_INFO_LINE_FORMAT2, index, startH, startM, endH, endM);
    }

    private static int parseAttendanceIndex(String index) {
        try {
            int result = Integer.parseInt(index.replace(BASIC_INFO_SPECIFIED, ""));
            if (result > 0 && result <= BASIC_INFO_ATTENDANCE_TIME_ITEM_MAX) {
                return result;
            }
        } catch (NumberFormatException e) {
        }
        return -1;
    }

    private static int parseBreakTimeIndex(String index, int max) {
        try {
            int result = Integer.parseInt(index.replace(BASIC_INFO_SPECIFIED, ""));
            if (result > 0 && result <= max) {
                return result;
            }
        } catch (NumberFormatException e) {
        }
        return -1;
    }

}
