package com.system.management.attendance.wams;

import android.Manifest;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.system.management.attendance.wams.file.AttendanceData;
import com.system.management.attendance.wams.file.BasicInfoData;
import com.system.management.attendance.wams.file.FileManager;

import java.util.Random;


public class MainActivity extends AppCompatActivity {
    private static final String TAG = MainActivity.class.getSimpleName();
    private static final int PERMISSION_REQUEST_CODE = 1;

    FileManager fm = new FileManager();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (!Utils.hasSelfPermissions(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                String[] permission = {Manifest.permission.WRITE_EXTERNAL_STORAGE};
                requestPermissions(permission, PERMISSION_REQUEST_CODE);
                return;
            }
        }
        startSample();
    }

    private void startSample() {

        fm.createCurrentAttendanceFiles();

        findViewById(R.id.button1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int result = fm.createAttendanceFiles(2017,11);
                if (result == FileManager.FILE_SUCCESS) {
                    Toast.makeText(getApplicationContext(), "Success", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getApplicationContext(), "Failed", Toast.LENGTH_SHORT).show();
                }
            }
        });

        findViewById(R.id.button2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int result = fm.createAttendanceFiles(2017,12);
                if (result == FileManager.FILE_SUCCESS) {
                    Toast.makeText(getApplicationContext(), "Success", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getApplicationContext(), "Failed", Toast.LENGTH_SHORT).show();
                }
            }
        });

        findViewById(R.id.button3).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int result = fm.createAttendanceFiles(2018,1);
                if (result == FileManager.FILE_SUCCESS) {
                    Toast.makeText(getApplicationContext(), "Success", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getApplicationContext(), "Failed", Toast.LENGTH_SHORT).show();
                }
            }
        });

        findViewById(R.id.button4).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AttendanceData[] datas = fm.readAttendanceFile(2017, 11);
                for (int i = 0; i < datas.length; i++ ) {
                    AttendanceData data = datas[i];
                    Log.d(TAG, "data : " + data.getDate() + "," + data.getDayOfWeek() + "," +
                            data.getSituation() + "," + data.getNote() + "," + data.getStartHour() +
                            "," + data.getStartMinute() + "," + data.getEndHour() + "," +
                            data.getEndMinute() + "," + data.getAttendanceTimeNum() + "," +
                            data.getBreakTimeNum());
                }
                if (datas != null) {
                    Toast.makeText(getApplicationContext(), "Success", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getApplicationContext(), "Failed", Toast.LENGTH_SHORT).show();
                }
            }
        });

        findViewById(R.id.button5).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AttendanceData[] datas = fm.readAttendanceFile(2017, 12);
                for (int i = 0; i < datas.length; i++ ) {
                    AttendanceData data = datas[i];
                    Log.d(TAG, "data : " + data.getDate() + "," + data.getDayOfWeek() + "," +
                            data.getSituation() + "," + data.getNote() + "," + data.getStartHour() +
                            "," + data.getStartMinute() + "," + data.getEndHour() + "," +
                            data.getEndMinute() + "," + data.getAttendanceTimeNum() + "," +
                            data.getBreakTimeNum());
                }
                if (datas != null) {
                    Toast.makeText(getApplicationContext(), "Success", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getApplicationContext(), "Failed", Toast.LENGTH_SHORT).show();
                }
            }
        });

        findViewById(R.id.button6).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AttendanceData[] datas = fm.readAttendanceFile(2018, 1);
                for (int i = 0; i < datas.length; i++ ) {
                    AttendanceData data = datas[i];
                    Log.d(TAG, "data : " + data.getDate() + "," + data.getDayOfWeek() + "," +
                            data.getSituation() + "," + data.getNote() + "," + data.getStartHour() +
                            "," + data.getStartMinute() + "," + data.getEndHour() + "," +
                            data.getEndMinute() + "," + data.getAttendanceTimeNum() + "," +
                            data.getBreakTimeNum());
                }
                if (datas != null) {
                    Toast.makeText(getApplicationContext(), "Success", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getApplicationContext(), "Failed", Toast.LENGTH_SHORT).show();
                }
            }
        });

        findViewById(R.id.button7).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (readFile(2017,11)) {
                    Toast.makeText(getApplicationContext(), "Success", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getApplicationContext(), "Failed", Toast.LENGTH_SHORT).show();
                }
            }
        });

        findViewById(R.id.button8).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (readFile(2017,12)) {
                    Toast.makeText(getApplicationContext(), "Success", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getApplicationContext(), "Failed", Toast.LENGTH_SHORT).show();
                }
            }
        });

        findViewById(R.id.button9).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (readFile(2018,1)) {
                    Toast.makeText(getApplicationContext(), "Success", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getApplicationContext(), "Failed", Toast.LENGTH_SHORT).show();
                }
            }
        });

        findViewById(R.id.button10).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AttendanceData[] datas = fm.readAttendanceFile(2017, 11);
                Random rnd = new Random();

                int date = rnd.nextInt(30);
                datas[date].setStartHour("10");
                datas[date].setStartMinute("30");
                datas[date].setEndHour("19");
                datas[date].setEndMinute("45");

                int result = fm.updateAttendanceFile(2017, 11, datas[date]);
                if (result == FileManager.FILE_SUCCESS) {
                    Toast.makeText(getApplicationContext(), "Success", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getApplicationContext(), "Failed", Toast.LENGTH_SHORT).show();
                }
            }
        });

        findViewById(R.id.button11).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AttendanceData[] datas = fm.readAttendanceFile(2017, 12);
                Random rnd = new Random();

                int date = rnd.nextInt(31);
                datas[date].setStartHour("9");
                datas[date].setStartMinute("30");
                datas[date].setEndHour("21");
                datas[date].setEndMinute("45");

                int result = fm.updateAttendanceFile(2017, 12, datas[date]);
                if (result == FileManager.FILE_SUCCESS) {
                    Toast.makeText(getApplicationContext(), "Success", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getApplicationContext(), "Failed", Toast.LENGTH_SHORT).show();
                }
            }
        });

        findViewById(R.id.button12).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AttendanceData[] datas = fm.readAttendanceFile(2018, 1);
                Random rnd = new Random();

                int date = rnd.nextInt(30);
                datas[date].setStartHour("8");
                datas[date].setStartMinute("30");
                datas[date].setEndHour("23");
                datas[date].setEndMinute("45");

                int result = fm.updateAttendanceFile(2018, 1, datas[date]);
                if (result == FileManager.FILE_SUCCESS) {
                    Toast.makeText(getApplicationContext(), "Success", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getApplicationContext(), "Failed", Toast.LENGTH_SHORT).show();
                }
            }
        });

        findViewById(R.id.button13).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                BasicInfoData basicInfoData = fm.readBasicInfoFile(2017, 11);
                Random rnd = new Random();

                int index = rnd.nextInt(4) + 1;
                BasicInfoData.SpecifiedTime time = basicInfoData.getBasicInfoAttendanceTime(index);
                time.setStartHour(10);
                time.setStartMinute(15);
                time.setEndHour(20);
                time.setEndMinute(45);

                index = rnd.nextInt(2) + 1;
                BasicInfoData.SpecifiedTime[] breakTimes = basicInfoData.getBasicInfoBreakTime(index);
                index = rnd.nextInt(9);
                breakTimes[index].setStartHour(15);
                breakTimes[index].setStartMinute(45);
                breakTimes[index].setEndHour(16);
                breakTimes[index].setEndMinute(5);

                int result = fm.updateBasicInfoFile(2017, 11, basicInfoData);
                if (result == FileManager.FILE_SUCCESS) {
                    Toast.makeText(getApplicationContext(), "Success", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getApplicationContext(), "Failed", Toast.LENGTH_SHORT).show();
                }
                readFile(2017, 11);
            }
        });

        findViewById(R.id.button14).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                BasicInfoData basicInfoData = fm.readBasicInfoFile(2017, 12);
                Random rnd = new Random();

                int index = rnd.nextInt(4) + 1;
                BasicInfoData.SpecifiedTime time = basicInfoData.getBasicInfoAttendanceTime(index);
                time.setStartHour(12);
                time.setStartMinute(45);
                time.setEndHour(23);
                time.setEndMinute(15);

                index = rnd.nextInt(2) + 1;
                BasicInfoData.SpecifiedTime[] breakTimes = basicInfoData.getBasicInfoBreakTime(index);
                index = rnd.nextInt(9);
                breakTimes[index].setStartHour(23);
                breakTimes[index].setStartMinute(30);
                breakTimes[index].setEndHour(24);
                breakTimes[index].setEndMinute(0);

                int result = fm.updateBasicInfoFile(2017, 12, basicInfoData);
                if (result == FileManager.FILE_SUCCESS) {
                    Toast.makeText(getApplicationContext(), "Success", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getApplicationContext(), "Failed", Toast.LENGTH_SHORT).show();
                }
                readFile(2017, 12);
            }
        });

        findViewById(R.id.button15).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                BasicInfoData basicInfoData = fm.readBasicInfoFile(2018, 1);
                Random rnd = new Random();

                int index = rnd.nextInt(4) + 1;
                BasicInfoData.SpecifiedTime time = basicInfoData.getBasicInfoAttendanceTime(index);
                time.setStartHour(11);
                time.setStartMinute(30);
                time.setEndHour(21);
                time.setEndMinute(45);

                index = rnd.nextInt(2) + 1;
                BasicInfoData.SpecifiedTime[] breakTimes = basicInfoData.getBasicInfoBreakTime(index);
                index = rnd.nextInt(9);
                breakTimes[index].setStartHour(17);
                breakTimes[index].setStartMinute(15);
                breakTimes[index].setEndHour(18);
                breakTimes[index].setEndMinute(10);

                int result = fm.updateBasicInfoFile(2018, 1, basicInfoData);
                if (result == FileManager.FILE_SUCCESS) {
                    Toast.makeText(getApplicationContext(), "Success", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getApplicationContext(), "Failed", Toast.LENGTH_SHORT).show();
                }
                readFile(2018, 1);
            }
        });

        findViewById(R.id.button16).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int result = fm.deleteAttendanceFile(2017, 11);
                if (result == FileManager.FILE_SUCCESS) {
                    Toast.makeText(getApplicationContext(), "Success", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getApplicationContext(), "Failed", Toast.LENGTH_SHORT).show();
                }
            }
        });

        findViewById(R.id.button17).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int result = fm.deleteAttendanceFile(2017, 12);
                if (result == FileManager.FILE_SUCCESS) {
                    Toast.makeText(getApplicationContext(), "Success", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getApplicationContext(), "Failed", Toast.LENGTH_SHORT).show();
                }
            }
        });

        findViewById(R.id.button18).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int result = fm.deleteAttendanceFile(2018, 1);
                if (result == FileManager.FILE_SUCCESS) {
                    Toast.makeText(getApplicationContext(), "Success", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getApplicationContext(), "Failed", Toast.LENGTH_SHORT).show();
                }
            }
        });

        findViewById(R.id.button19).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int result = fm.deleteBasicInfoFile(2017, 11);
                if (result == FileManager.FILE_SUCCESS) {
                    Toast.makeText(getApplicationContext(), "Success", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getApplicationContext(), "Failed", Toast.LENGTH_SHORT).show();
                }
            }
        });

        findViewById(R.id.button20).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int result = fm.deleteBasicInfoFile(2017, 12);
                if (result == FileManager.FILE_SUCCESS) {
                    Toast.makeText(getApplicationContext(), "Success", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getApplicationContext(), "Failed", Toast.LENGTH_SHORT).show();
                }
            }
        });

        findViewById(R.id.button21).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int result = fm.deleteBasicInfoFile(2018, 1);
                if (result == FileManager.FILE_SUCCESS) {
                    Toast.makeText(getApplicationContext(), "Success", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getApplicationContext(), "Failed", Toast.LENGTH_SHORT).show();
                }
            }
        });

        findViewById(R.id.button22).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Random rnd = new Random();

                int date = rnd.nextInt(30);

                AttendanceData data = fm.readAttendanceFile(2017, 12, date);
                if (data != null) {
                    data.setStartHour("8");
                    data.setStartMinute("0");
                    data.setEndHour("23");
                    data.setEndMinute("30");

                    data.setAttendanceTimeNum("3");
                    data.setBreakTimeNum("3");

                    int result = fm.updateAttendanceFile(2017, 12, data);
                    if (result == FileManager.FILE_SUCCESS) {
                        AttendanceData[] datas = fm.readAttendanceFile(2017, 12);
                        for (int i = 0; i < datas.length; i++) {
                            Log.d(TAG, "data : " + datas[i].getDate() + "," + datas[i].getDayOfWeek() + "," +
                                    datas[i].getSituation() + "," + datas[i].getNote() + "," + datas[i].getStartHour() +
                                    "," + datas[i].getStartMinute() + "," + datas[i].getEndHour() + "," +
                                    datas[i].getEndMinute() + "," + datas[i].getAttendanceTimeNum() + "," +
                                    datas[i].getBreakTimeNum());
                        }
                        Toast.makeText(getApplicationContext(), "Success 2017.12." + date, Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(getApplicationContext(), "Failed", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(getApplicationContext(), "Create data for 2017.12", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == PERMISSION_REQUEST_CODE && grantResults.length > 0) {
            if (!Utils.checkGrantResults(grantResults)) {
                Toast.makeText(this, "WRITE_EXTERNAL_STORAGE permission is nothing", Toast.LENGTH_SHORT).show();
                finish();
            } else {
                startSample();
            }
        }
    }

    private boolean readFile(int year, int month) {
        BasicInfoData basicInfoData = fm.readBasicInfoFile(year, month);
        if (basicInfoData != null) {
            for (int index = 1; index <= 5; index++) {
                BasicInfoData.SpecifiedTime time = basicInfoData.getBasicInfoAttendanceTime(index);
                Log.d(TAG, "Attendance Time " + index + " START " + time.getStartHour() + ":"
                        + time.getStartMinute() + " / END " + time.getEndHour() + ":" +
                        time.getEndMinute());
            }

            for (int index = 1; index <= 3; index++) {
                BasicInfoData.SpecifiedTime[] breakTimes = basicInfoData.getBasicInfoBreakTime(index);
                for (int i = 0; i < breakTimes.length; i++) {
                    Log.d(TAG, "Break Time " + index + "-" + (i + 1) + " START " +
                            breakTimes[i].getStartHour() + ":" + breakTimes[i].getStartMinute()
                            + " / END " + breakTimes[i].getEndHour() + ":" +
                            breakTimes[i].getEndMinute());
                }
            }
            return true;
        }
        return false;
    }

}
