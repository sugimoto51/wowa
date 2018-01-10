package com.system.management.attendance.wams.file;

import android.util.Log;

import java.io.BufferedWriter;
import java.io.IOException;

/**
 * Created by koichi on 2017/11/20.
 */

public class AttendanceData {

    public enum SituationType {
        PAID_HOLIDAY_ALL,       //有給（全）
        PAID_HOLIDAY_AM,        //有給（AM）
        PAID_HOLIDAY_PM,        //有給（PM）
        ALL_NIGHT_HOLIDAY,      //明休
        SPECIAL_HOLIDAY,        //特休
        ABSENCE,                //欠勤
        SUBSTITUTE_HOLIDAY,     //代休
        TRANSFER_ATTENDANCE,    //振出
        TRANSFER_HOLIDAY,       //振休
        LEAVE_OF_ABSENCE,       //休職
        BEREAVEMENT_LEAVE,      //忌引
        HOLIDAY,                //休暇
        NONE                    //未設定
    }

    public static final String ATTENDANCE_ITEMS = "日付,曜日,勤務状況,備考,,出社時刻（時）," +
            "出社時刻（分）,退社時刻（時）,退社時刻（分）,####,出勤時間設定No.,休憩時間設定No.";

    private static final String TAG = AttendanceData.class.getSimpleName();

    private static final String ATTENDANCE_HEADER = "WOW勤怠入力,";
    private static final String[] DAY_OF_WEEK_NAME_LIST = {"日", "月", "火", "水", "木", "金", "土"};
    private static final String ATTENDANCE_LINE_FORMAT1 = "%d,%s,,,,,,,,####,,";
    private static final String ATTENDANCE_LINE_FORMAT2 = "%d,%s,%s,%s,,%s,%s,%s,%s,####,%s,%s";

    private static final String SITUATION_TYPE_DATA_PAID_HOLIDAY_ALL = "有給（全）";
    private static final String SITUATION_TYPE_DATA_PAID_HOLIDAY_AM = "有給（AM）";
    private static final String SITUATION_TYPE_DATA_PAID_HOLIDAY_PM = "有給（PM）";
    private static final String SITUATION_TYPE_DATA_ALL_NIGHT_HOLIDAY = "明休";
    private static final String SITUATION_TYPE_DATA_SPECIAL_HOLIDAY = "特休";
    private static final String SITUATION_TYPE_DATA_ABSENCE = "欠勤";
    private static final String SITUATION_TYPE_DATA_SUBSTITUTE_HOLIDAY = "代休";
    private static final String SITUATION_TYPE_DATA_TRANSFER_ATTENDANCE = "振出";
    private static final String SITUATION_TYPE_DATA_TRANSFER_HOLIDAY = "振休";
    private static final String SITUATION_TYPE_DATA_LEAVE_OF_ABSENCE = "休職";
    private static final String SITUATION_TYPE_DATA_BEREAVEMENT_LEAVE = "忌引";
    private static final String SITUATION_TYPE_DATA_HOLIDAY = "休暇";

    private static final int BASIC_INFO_ATTENDANCE_TIME_ITEM_MAX = 5;
    private static final int BASIC_INFO_BREAK_TIME_ITEM_MAX = 3;

    private int mDate = -1;
    private String mDayOfWeek = "";
    private SituationType mSituation = SituationType.NONE;
    private String mNote = "";
    private int mStartHour = -1;
    private int mStartMinute = -1;
    private int mEndHour = -1;
    private int mEndMinute = -1;
    private int mAttendanceTimeNum = -1;
    private int mBreakTimeNum = -1;


    public int getDate() {
        return mDate;
    }

    public String getDayOfWeek() {
        return mDayOfWeek;
    }

    public SituationType getSituation() {
        return mSituation;
    }

    public String getNote() {
        return mNote;
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

    public int getAttendanceTimeNum() {
        return mAttendanceTimeNum;
    }

    public int getBreakTimeNum() {
        return mBreakTimeNum;
    }

    public boolean setDate(String date) {
        int result = Utils.parseDate(date);
        if (result != -1) {
            mDate = result;
            return true;
        } else {
            Log.e(TAG, "invalid date data: " + date);
            return false;
        }
    }

    public boolean setDayOfWeek(String dayOfWeek) {
        mDayOfWeek = dayOfWeek;
        return true;
    }

    public boolean setSituation(String situation) {
        SituationType type = parseSituationType(situation);
        if (type != SituationType.NONE) {
            mSituation = type;
            return true;
        } else if (situation.length() == 0) {
            return true;
        } else {
            Log.e(TAG, "invalid situation data: " + situation);
            return false;
        }
    }

    public boolean setNote(String note) {
        mNote = note;
        return true;
    }

    public boolean setStartHour(String startHour) {
        int hour = Utils.parseHour(startHour);
        if (startHour.length() == 0 || hour != -1) {
            mStartHour = hour;
            return true;
        } else if (startHour.length() == 0) {
            return true;
        } else {
            Log.e(TAG, "invalid start hour data: " + startHour);
            return false;
        }
    }

    public boolean setStartMinute(String startMinute) {
        int minute = Utils.parseMinute(startMinute);
        if (minute != -1) {
            mStartMinute = minute;
            return true;
        } else if (startMinute.length() == 0) {
            return true;
        } else {
            Log.e(TAG, "invalid start minute data: " + startMinute);
            return false;
        }
    }

    public boolean setEndHour(String endHour) {
        int hour = Utils.parseHour(endHour);
        if (hour != -1) {
            mEndHour = hour;
            return true;
        } else if (endHour.length() == 0) {
            return true;
        } else {
            Log.e(TAG, "invalid end hour data: " + endHour);
            return false;
        }
    }

    public boolean setEndMinute(String endMinute) {
        int minute = Utils.parseMinute(endMinute);
        if (minute != -1) {
            mEndMinute = minute;
            return true;
        } else if (endMinute.length() == 0) {
            return true;
        } else {
            Log.e(TAG, "invalid end minute data: " + endMinute);
            return false;
        }
    }

    public boolean setAttendanceTimeNum(String attendanceTimeNum) {
        int num = parseAttendanceTimeNum(attendanceTimeNum);
        if (num != -1) {
            mAttendanceTimeNum = num;
            return true;
        } else if (attendanceTimeNum.length() == 0) {
            return true;
        } else {
            Log.e(TAG, "invalid attendance time number data: " + attendanceTimeNum);
            return false;
        }
    }

    public boolean setBreakTimeNum(String breakTimeNum) {
        int num = parseBreakTimeNum(breakTimeNum);
        if (num != -1) {
            mBreakTimeNum = num;
            return true;
        } else if (breakTimeNum.length() == 0) {
            return true;
        } else {
            Log.e(TAG, "invalid break time number data: " + breakTimeNum);
            return false;
        }
    }

    public static void writeAttendanceFileHeader(BufferedWriter bufferedWriter, String dir)
            throws IOException {
        bufferedWriter.write(ATTENDANCE_HEADER + dir);
        bufferedWriter.newLine();
        bufferedWriter.write(ATTENDANCE_ITEMS);
        bufferedWriter.newLine();
    }

    public static void writeAttendanceFileDataLine(BufferedWriter bufferedWriter, int date,
                                                    int dayOfWeek) throws IOException {
        String line = String.format(ATTENDANCE_LINE_FORMAT1, date, DAY_OF_WEEK_NAME_LIST[dayOfWeek]);
        bufferedWriter.write(line);
        bufferedWriter.newLine();
    }

    public static boolean writeAttendanceFileDataLine(BufferedWriter bufferedWriter, int date,
                                                      String dayOfWeek, String situation, String note,
                                                      int startHour, int startMinute, int endHour,
                                                      int endMinute, int attendanceTimeNum,
                                                      int breakTimeNum) throws IOException {
        if (date == -1) return false;
        String startH = startHour != -1 ? String.valueOf(startHour) : "";
        String startM = startMinute != -1 ? String.valueOf(startMinute) : "";
        String endH = endHour != -1 ? String.valueOf(endHour) : "";
        String endM = endMinute != -1 ? String.valueOf(endMinute) : "";
        String aTimeNum = attendanceTimeNum != -1 ? String.valueOf(attendanceTimeNum) : "";
        String bTimeNum = breakTimeNum != -1 ? String.valueOf(breakTimeNum) : "";

        String line = String.format(ATTENDANCE_LINE_FORMAT2, date, dayOfWeek, situation, note,
                startH, startM, endH, endM, aTimeNum, bTimeNum);
        bufferedWriter.write(line);
        bufferedWriter.newLine();
        return true;
    }

    public static String getDayOfWeek(int dayOfWeek) {
       return DAY_OF_WEEK_NAME_LIST[dayOfWeek - 1];
    }

        public static String getSituationType(SituationType type) {
        switch (type) {
            case PAID_HOLIDAY_ALL:
                return SITUATION_TYPE_DATA_PAID_HOLIDAY_ALL;
            case PAID_HOLIDAY_AM:
                return SITUATION_TYPE_DATA_PAID_HOLIDAY_AM;
            case PAID_HOLIDAY_PM:
                return SITUATION_TYPE_DATA_PAID_HOLIDAY_PM;
            case ALL_NIGHT_HOLIDAY:
                return SITUATION_TYPE_DATA_ALL_NIGHT_HOLIDAY;
            case ABSENCE:
                return SITUATION_TYPE_DATA_ABSENCE;
            case SUBSTITUTE_HOLIDAY:
                return SITUATION_TYPE_DATA_SUBSTITUTE_HOLIDAY;
            case TRANSFER_ATTENDANCE:
                return SITUATION_TYPE_DATA_TRANSFER_ATTENDANCE;
            case TRANSFER_HOLIDAY:
                return SITUATION_TYPE_DATA_TRANSFER_HOLIDAY;
            case LEAVE_OF_ABSENCE:
                return SITUATION_TYPE_DATA_LEAVE_OF_ABSENCE;
            case BEREAVEMENT_LEAVE:
                return SITUATION_TYPE_DATA_BEREAVEMENT_LEAVE;
            case HOLIDAY:
                return SITUATION_TYPE_DATA_HOLIDAY;
            case NONE:
            default:
                return "";
        }
   }

    private SituationType parseSituationType(String type) {
        if (type.equals(SITUATION_TYPE_DATA_PAID_HOLIDAY_ALL)) {
            return AttendanceData.SituationType.PAID_HOLIDAY_ALL;
        } else if (type.equals(SITUATION_TYPE_DATA_PAID_HOLIDAY_AM)) {
            return AttendanceData.SituationType.PAID_HOLIDAY_AM;
        } else if (type.equals(SITUATION_TYPE_DATA_PAID_HOLIDAY_PM)) {
            return AttendanceData.SituationType.PAID_HOLIDAY_PM;
        } else if (type.equals(SITUATION_TYPE_DATA_ALL_NIGHT_HOLIDAY)) {
            return AttendanceData.SituationType.ALL_NIGHT_HOLIDAY;
        } else if (type.equals(SITUATION_TYPE_DATA_SPECIAL_HOLIDAY)) {
            return AttendanceData.SituationType.SPECIAL_HOLIDAY;
        } else if (type.equals(SITUATION_TYPE_DATA_ABSENCE)) {
            return AttendanceData.SituationType.ABSENCE;
        } else if (type.equals(SITUATION_TYPE_DATA_SUBSTITUTE_HOLIDAY)) {
            return AttendanceData.SituationType.SUBSTITUTE_HOLIDAY;
        } else if (type.equals(SITUATION_TYPE_DATA_TRANSFER_ATTENDANCE)) {
            return AttendanceData.SituationType.TRANSFER_ATTENDANCE;
        } else if (type.equals(SITUATION_TYPE_DATA_TRANSFER_HOLIDAY)) {
            return AttendanceData.SituationType.TRANSFER_HOLIDAY;
        } else if (type.equals(SITUATION_TYPE_DATA_LEAVE_OF_ABSENCE)) {
            return AttendanceData.SituationType.LEAVE_OF_ABSENCE;
        } else if (type.equals(SITUATION_TYPE_DATA_BEREAVEMENT_LEAVE)) {
            return AttendanceData.SituationType.BEREAVEMENT_LEAVE;
        } else if (type.equals(SITUATION_TYPE_DATA_HOLIDAY)) {
            return AttendanceData.SituationType.HOLIDAY;
        } else {
            return AttendanceData.SituationType.NONE;
        }
    }

    private int parseAttendanceTimeNum(String attendanceTimeNum) {
        try {
            int num = Integer.parseInt(attendanceTimeNum);
            if (num >= 1 && num <= BASIC_INFO_ATTENDANCE_TIME_ITEM_MAX) {
                return num;
            }
        } catch (NumberFormatException e) {
        }
        return -1;
    }

    private int parseBreakTimeNum(String breakTimeNum) {
        try {
            int num = Integer.parseInt(breakTimeNum);
            if (num >= 1 && num <= BASIC_INFO_BREAK_TIME_ITEM_MAX) {
                return num;
            }
        } catch (NumberFormatException e) {
        }
        return -1;
    }
}
