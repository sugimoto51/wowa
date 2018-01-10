package com.system.management.attendance.wams.file;

import android.os.Environment;
import android.util.Log;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Calendar;

/**
 * Created by koichi on 2017/11/14.
 */

public class FileManager {

    public static final int FILE_SUCCESS = 0;
    public static final int FILE_FAILED = 1;

    private static final String TAG = FileManager.class.getSimpleName();
    private static final String CSV_FILE_NAME_ATTENDANCE = "Attendance.csv";
    private static final String CSV_FILE_NAME_ATTENDANCE_TEMP = "Attendance_temp.csv";
    private static final String CSV_FILE_NAME_BASIC_INFO = "BasicInfo.csv";
    private static final String CSV_FILE_NAME_BASIC_INFO_TEMP = "BasicInfo_temp.csv";
    private static final String PARENT_DIR_NAME = "WOWA";
    private static final String DIR_NAME_FORMAT = "%04d%02d";
    private static final String CHAR_FORMAT = "SJIS";

    private static final String mPath = Environment.getExternalStorageDirectory().getPath();

    private static final int ATTENDANCE_COLUMN_DATE = 0;
    private static final int ATTENDANCE_COLUMN_DAY_OF_WEEK = 1;
    private static final int ATTENDANCE_COLUMN_SITUATION = 2;
    private static final int ATTENDANCE_COLUMN_NOTE = 3;
    private static final int ATTENDANCE_COLUMN_START_HOUR = 5;
    private static final int ATTENDANCE_COLUMN_START_MINUTE = 6;
    private static final int ATTENDANCE_COLUMN_END_HOUR = 7;
    private static final int ATTENDANCE_COLUMN_END_MINUTE = 8;
    private static final int ATTENDANCE_COLUMN_TIME_NUM = 10;
    private static final int ATTENDANCE_COLUMN_BREAK_TIME_NUM = 11;
    private static final int ATTENDANCE_COLUMN_MAX = ATTENDANCE_COLUMN_BREAK_TIME_NUM + 1;

    private static final int BASIC_INFO_COLUMN_INDEX = 0;
    private static final int BASIC_INFO_COLUMN_START_H = 1;
    private static final int BASIC_INFO_COLUMN_START_M = 3;
    private static final int BASIC_INFO_COLUMN_END_H = 5;
    private static final int BASIC_INFO_COLUMN_END_M = 7;
    private static final int BASIC_INFO_COLUMN_MAX = BASIC_INFO_COLUMN_END_M + 1;

    public int createCurrentAttendanceFiles() {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH) + 1;

        return createAttendanceFiles(year, month);
    }

    public int createAttendanceFiles(int year, int month) {
        int result = FILE_FAILED;

        String dirPath = makeDirPath(year, month);

        if (!createDirs(dirPath)) {
            Log.e(TAG, "Directory creation failed.");
            return result;
        }

        result = createBasicInfoFile(dirPath, year, month);
        result |= createAttendanceFile(dirPath, year, month);

        return result;
    }

    public AttendanceData[] readAttendanceFile(int year, int month) {
        AttendanceData[] result = null;
        ArrayList list = new ArrayList<AttendanceData>();
        String filePath = makePath(makeDirPath(year, month), CSV_FILE_NAME_ATTENDANCE);

        try {
            InputStreamReader inputStreamReader = new InputStreamReader(
                    new FileInputStream(filePath), CHAR_FORMAT);
            BufferedReader bufferedReader = null;
            try {
                String line;
                bufferedReader = new BufferedReader(inputStreamReader);
                boolean enableParseLine = false;
                while ((line = bufferedReader.readLine()) != null) {
                    if (line.contains(AttendanceData.ATTENDANCE_ITEMS)) {
                        enableParseLine = true;
                    } else if (enableParseLine) {
                        AttendanceData data = parseAttendanceLineData(year, month, line);
                        if (data != null) {
                            list.add(data);
                        } else {
                            Log.e(TAG, "invalid attendance data");
                            return result;
                        }
                    }
                }

                result = (AttendanceData[]) list.toArray(new AttendanceData[list.size()]);

            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } finally {
                if (bufferedReader != null) bufferedReader.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return result;
    }

    public AttendanceData readAttendanceFile(int year, int month, int date) {
        AttendanceData result = null;

        Calendar calendar = Calendar.getInstance();
        calendar.setLenient(false);
        calendar.set(year, month - 1, date);
        if (!isValidDate(calendar)) {
            Log.e(TAG, "invalid date : " + calendar);
            return result;
        }

        AttendanceData[] monthData = readAttendanceFile(year, month);
        if (monthData == null) {
            Log.e(TAG, "failed read data : " + calendar);
            return result;
        }

        for (AttendanceData data : monthData) {
            if (data.getDate() == date) {
                result = data;
            }
        }

        return result;
    }

    public BasicInfoData readBasicInfoFile(int year, int month) {
        BasicInfoData basicInfoData = new BasicInfoData();
        String filePath = makePath(makeDirPath(year, month), CSV_FILE_NAME_BASIC_INFO);

        try {
            InputStreamReader inputStreamReader = new InputStreamReader(
                    new FileInputStream(filePath), CHAR_FORMAT);
            BufferedReader bufferedReader = null;
            try {
                String line;
                bufferedReader = new BufferedReader(inputStreamReader);
                boolean enableAttendanceParseLine = false;
                boolean enableBreakTimeParseLine = false;
                while ((line = bufferedReader.readLine()) != null) {
                    if (line.length() == 0) {
                        continue;
                    } else if (line.contains(BasicInfoData.BASIC_INFO_ATTENDANCE_TIME)) {
                        enableAttendanceParseLine = true;
                    } else if (line.contains(BasicInfoData.BASIC_INFO_BREAK_TIME)) {
                        enableBreakTimeParseLine = true;
                        enableAttendanceParseLine = false;
                    } else if (enableAttendanceParseLine) {
                        String[] items = line.split(",", BASIC_INFO_COLUMN_MAX);
                        Boolean result = basicInfoData.setBasicInfoAttendanceTime(
                                items[BASIC_INFO_COLUMN_INDEX],
                                items[BASIC_INFO_COLUMN_START_H],
                                items[BASIC_INFO_COLUMN_START_M],
                                items[BASIC_INFO_COLUMN_END_H],
                                items[BASIC_INFO_COLUMN_END_M]);
                        if (!result) {
                            return null;
                        }
                    } else if (enableBreakTimeParseLine) {
                        String[] items = line.split(",", BASIC_INFO_COLUMN_MAX);
                        Boolean result = basicInfoData.setBasicInfoBreakTime(
                                items[BASIC_INFO_COLUMN_INDEX],
                                items[BASIC_INFO_COLUMN_START_H],
                                items[BASIC_INFO_COLUMN_START_M],
                                items[BASIC_INFO_COLUMN_END_H],
                                items[BASIC_INFO_COLUMN_END_M]);
                        if (!result) {
                            return null;
                        }
                    }
                }
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } finally {
                if (bufferedReader != null) bufferedReader.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return basicInfoData;
    }

    public int updateAttendanceFile(int year, int month, AttendanceData attendanceData) {
        int result = FILE_FAILED;
        boolean write = false;
        AttendanceData[] data = readAttendanceFile(year, month);
        for (int i = 0; i < data.length; i++) {
            if (data[i].getDate() == attendanceData.getDate()) {
                data[i] = attendanceData;
                write = true;
                break;
            }
        }

        if (write) {
            result = updateAttendanceFile(makeDirPath(year, month), year, month, data);
        }

        return result;
    }

    public int updateBasicInfoFile(int year, int month, BasicInfoData basicInfoData) {
        int result = FILE_FAILED;
        String dirPath = makeDirPath(year, month);
        String tempFilePath = makePath(dirPath, CSV_FILE_NAME_BASIC_INFO_TEMP);
        String oldFilePath = makePath(dirPath, CSV_FILE_NAME_BASIC_INFO);

        try {
            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(
                    new FileOutputStream(tempFilePath), CHAR_FORMAT);
            BufferedWriter bufferedWriter = null;
            try {
                bufferedWriter = new BufferedWriter(outputStreamWriter);
                BasicInfoData.writeBasicInfoFileHeader(bufferedWriter,
                        makeDirName(year, month));

                BasicInfoData.writeBasicInfoFileData(bufferedWriter);

                if (deleteFile(oldFilePath) == FILE_SUCCESS) {
                    result = renameFile(tempFilePath, oldFilePath);
                } else {
                    deleteFile(tempFilePath);
                }
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } finally {
                if (bufferedWriter != null) bufferedWriter.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return result;
    }

    public int deleteAttendanceFile(int year, int month) {
        return deleteFile(makePath(makeDirPath(year, month), CSV_FILE_NAME_ATTENDANCE));
    }

    public int deleteBasicInfoFile(int year, int month) {
        return deleteFile(makePath(makeDirPath(year, month), CSV_FILE_NAME_BASIC_INFO));
    }

    private String makeDirName(int year, int month) {
        return String.format(DIR_NAME_FORMAT, year, month);
    }

    private String makeDirPath(int year, int month) {
        return mPath + "/" + PARENT_DIR_NAME + "/" + makeDirName(year, month);
    }

    private String makePreviousMonthPath(int year, int month, String fileName) {
        if (month == 1) {
            year = year - 1;
            month = 12;
        } else {
            month = month - 1;
        }
        return makePath(makeDirPath(year, month), fileName);
    }

    private String makePath(String dirPath, String fileName) {
        return dirPath + "/" + fileName;

    }

    private boolean createDirs(String dirPath) {
        boolean result = true;
        File dir = new File(dirPath);
        if (!dir.exists()) {
            result = dir.mkdirs();
        }
        return result;
    }

    private boolean isFileExists(String path) {
        File file = new File(path);
        return file.exists();
    }

    private boolean isValidDate(Calendar calendar) {
        try {
            calendar.getTime();
            return true;
        } catch (IllegalArgumentException e) {
            Log.d(TAG, "Not valid " + calendar.toString());
        }
        return false;
    }

    private int createAttendanceFile(String dirPath, int year, int month) {
        int result = FILE_FAILED;
        int date = 1;
        String filePath = makePath(dirPath, CSV_FILE_NAME_ATTENDANCE);

        if (isFileExists(filePath)) {
            Log.d(TAG, "file is already exists. " + filePath);
            return FILE_SUCCESS;
        }

        Calendar calendar = Calendar.getInstance();
        calendar.setLenient(false);
        calendar.set(year, month - 1, date);

        try {
            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(
                    new FileOutputStream(filePath), CHAR_FORMAT);
            BufferedWriter bufferedWriter = null;
            try {
                bufferedWriter = new BufferedWriter(outputStreamWriter);
                AttendanceData.writeAttendanceFileHeader(bufferedWriter,
                        makeDirName(year, month));

                BasicInfoData data = readBasicInfoFile(year, month);
                BasicInfoData.SpecifiedTime attendanceTime = null;
                boolean enableDefaultData = false;
                int startHour = 0;
                int startMinute = 0;
                int endHour = 0;
                int endMinute = 0;

                if (data != null) {
                    attendanceTime = data.getBasicInfoAttendanceTime(1);
                    startHour = attendanceTime.getStartHour();
                    startMinute = attendanceTime.getStartMinute();
                    endHour = attendanceTime.getEndHour();
                    endMinute = attendanceTime.getEndMinute();
                    if (startHour != -1 && startMinute != -1 && endHour != -1 && endMinute != -1) {
                        enableDefaultData = true;
                    }
                }
                while (isValidDate(calendar)) {
                    if (enableDefaultData) {
                        if (!AttendanceData.writeAttendanceFileDataLine(bufferedWriter, date,
                                AttendanceData.getDayOfWeek(calendar.get(Calendar.DAY_OF_WEEK)), "",
                                "", startHour, startMinute, endHour, endMinute, 1, 1)) {
                            return result;
                        }
                    } else {
                        AttendanceData.writeAttendanceFileDataLine(bufferedWriter, date,
                                calendar.get(Calendar.DAY_OF_WEEK) - 1);
                    }
                    date += 1;
                    calendar.set(Calendar.DATE, date);
                }

                result = FILE_SUCCESS;
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } finally {
                if (bufferedWriter != null) bufferedWriter.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return result;
    }

    private int updateAttendanceFile(String dirPath, int year, int month,
                                     AttendanceData[] attendanceData) {
        int result = FILE_FAILED;
        String tempFilePath = makePath(dirPath, CSV_FILE_NAME_ATTENDANCE_TEMP);
        String oldFilePath = makePath(dirPath, CSV_FILE_NAME_ATTENDANCE);

        try {
            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(
                    new FileOutputStream(tempFilePath), CHAR_FORMAT);
            BufferedWriter bufferedWriter = null;
            try {
                bufferedWriter = new BufferedWriter(outputStreamWriter);
                AttendanceData.writeAttendanceFileHeader(bufferedWriter,
                        makeDirName(year, month));

                for (AttendanceData data : attendanceData) {
                    if (!AttendanceData.writeAttendanceFileDataLine(bufferedWriter, data.getDate(),
                            data.getDayOfWeek(), data.getSituationType(data.getSituation()),
                            data.getNote(), data.getStartHour(), data.getStartMinute(), data.getEndHour(),
                            data.getEndMinute(), data.getAttendanceTimeNum(), data.getBreakTimeNum())) {
                        return result;
                    }
                }

                if (deleteFile(oldFilePath) == FILE_SUCCESS) {
                    result = renameFile(tempFilePath, oldFilePath);
                } else {
                    deleteFile(tempFilePath);
                }
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } finally {
                if (bufferedWriter != null) bufferedWriter.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return result;
    }

    private int copyPreviousMonthBasicInfoFile(String src, String dst, int year, int month) {
        int result = FILE_FAILED;

        try {
            InputStreamReader inputStreamReader = new InputStreamReader(
                    new FileInputStream(src), CHAR_FORMAT);
            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(
                    new FileOutputStream(dst), CHAR_FORMAT);
            BufferedWriter bufferedWriter = null;
            BufferedReader bufferedReader = null;
            try {
                bufferedWriter = new BufferedWriter(outputStreamWriter);
                String line;
                bufferedReader = new BufferedReader(inputStreamReader);
                while ((line = bufferedReader.readLine()) != null) {
                    BasicInfoData.writeBasicInfoFileHeader(bufferedWriter, line,
                            makeDirName(year, month));
                }
                result = FILE_SUCCESS;
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } finally {
                if (bufferedWriter != null) bufferedWriter.close();
                if (bufferedReader != null) bufferedReader.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return result;
    }


    private int createBasicInfoFile(String dirPath, int year, int month) {
        int result = FILE_FAILED;

        String filePath = makePath(dirPath, CSV_FILE_NAME_BASIC_INFO);
        if (isFileExists(filePath)) {
            Log.d(TAG, "file is already exists. " + filePath);
            return FILE_SUCCESS;
        }

        String previousMonthPath = makePreviousMonthPath(year, month, CSV_FILE_NAME_BASIC_INFO);
        if (isFileExists(previousMonthPath)) {
            result = copyPreviousMonthBasicInfoFile(previousMonthPath, filePath, year, month);
        } else {
            try {
                OutputStreamWriter outputStreamWriter = new OutputStreamWriter(
                        new FileOutputStream(filePath), CHAR_FORMAT);
                BufferedWriter bufferedWriter = null;
                try {
                    bufferedWriter = new BufferedWriter(outputStreamWriter);
                    BasicInfoData.writeBasicInfoFileHeader(bufferedWriter,
                            makeDirName(year, month));

                    BasicInfoData.writeBasicInfoFileInitData(bufferedWriter);

                    result = FILE_SUCCESS;
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } finally {
                    if (bufferedWriter != null) bufferedWriter.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return result;
    }

    private AttendanceData parseAttendanceLineData(int year, int month, String line) {
        String[] items = line.split(",", ATTENDANCE_COLUMN_MAX);

        int date = Utils.parseDate(items[ATTENDANCE_COLUMN_DATE]);

        Calendar calendar = Calendar.getInstance();
        calendar.setLenient(false);
        calendar.set(year, month - 1, date);
        if (!isValidDate(calendar)) {
            Log.e(TAG, "invalid date : " + calendar);
            return null;
        }

        AttendanceData data = new AttendanceData();
        if (!data.setDate(items[ATTENDANCE_COLUMN_DATE])) return null;
        if (!data.setDayOfWeek(items[ATTENDANCE_COLUMN_DAY_OF_WEEK])) return null;
        if (!data.setSituation(items[ATTENDANCE_COLUMN_SITUATION])) return null;
        if (!data.setNote(items[ATTENDANCE_COLUMN_NOTE])) return null;
        if (!data.setStartHour(items[ATTENDANCE_COLUMN_START_HOUR])) return null;
        if (!data.setStartMinute(items[ATTENDANCE_COLUMN_START_MINUTE])) return null;
        if (!data.setEndHour(items[ATTENDANCE_COLUMN_END_HOUR])) return null;
        if (!data.setEndMinute(items[ATTENDANCE_COLUMN_END_MINUTE])) return null;
        if (!data.setAttendanceTimeNum(items[ATTENDANCE_COLUMN_TIME_NUM])) return null;
        if (!data.setBreakTimeNum(items[ATTENDANCE_COLUMN_BREAK_TIME_NUM])) return null;

        return data;
    }

    private int deleteFile(String path) {
        File file = new File(path);
        if (!file.exists() || file.delete()) {
            return FILE_SUCCESS;
        } else {
            return FILE_FAILED;
        }
    }

    private int renameFile(String oldPath, String newPath) {
        File oldFile = new File(oldPath);
        File newFile = new File(newPath);
        if (oldFile.renameTo(newFile)) {
            return FILE_SUCCESS;
        } else {
            return FILE_FAILED;
        }
    }
}
