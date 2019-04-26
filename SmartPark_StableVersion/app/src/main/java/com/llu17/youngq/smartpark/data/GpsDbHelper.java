package com.llu17.youngq.smartpark.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;


/**
 * Created by youngq on 17/3/2.
 */

public class GpsDbHelper extends SQLiteOpenHelper{

    private static final String DATABASE_NAME = "gps_location.db";
    private static final int DATABASE_VERSION = 1;

    public GpsDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        final String SQL_CREATE_GPS_LOACTION_TABLE = "CREATE TABLE IF NOT EXISTS " +
                GpsContract.GpsEntry.TABLE_NAME + " (" +
                GpsContract.GpsEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                GpsContract.GpsEntry.COLUMN_ID + " TEXT NOT NULL, " +
                GpsContract.GpsEntry.COLUMN_TAG + " INTEGER NOT NULL, " +
                GpsContract.GpsEntry.COLUMN_TIMESTAMP + " INTEGER NOT NULL, " +
                GpsContract.GpsEntry.COLUMN_LATITUDE + " REAL NOT NULL, " +
                GpsContract.GpsEntry.COLUMN_LONGITUDE + " REAL NOT NULL, " +
                GpsContract.GpsEntry.COLUMN_BEARING + " REAL NOT NULL, " +
                GpsContract.GpsEntry.COLUMN_SPEED + " REAL NOT NULL, " +
                GpsContract.GpsEntry.COLUMN_FLAG + " INTEGER NOT NULL" +
                ");";
        db.execSQL(SQL_CREATE_GPS_LOACTION_TABLE);
        Log.e("=====GPS table=====","success!");
        final String SQL_CREATE_ACCELEROMETER_TABLE = "CREATE TABLE IF NOT EXISTS " +
                GpsContract.AccelerometerEntry.TABLE_NAME + " (" +
                GpsContract.AccelerometerEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                GpsContract.AccelerometerEntry.COLUMN_ID + " TEXT NOT NULL, " +
                GpsContract.AccelerometerEntry.COLUMN_TAG + " INTEGER NOT NULL, " +
                GpsContract.AccelerometerEntry.COLUMN_TIMESTAMP + " INTEGER NOT NULL, " +
                GpsContract.AccelerometerEntry.COLUMN_X + " REAL NOT NULL, " +
                GpsContract.AccelerometerEntry.COLUMN_Y + " REAL NOT NULL, " +
                GpsContract.AccelerometerEntry.COLUMN_Z + " REAL NOT NULL" +
                ");";
        db.execSQL(SQL_CREATE_ACCELEROMETER_TABLE);
        Log.e("=====ACCELE table=====","success!");
        final String SQL_CREATE_GYROSCOPE_TABLE = "CREATE TABLE IF NOT EXISTS " +
                GpsContract.GyroscopeEntry.TABLE_NAME + " (" +
                GpsContract.GyroscopeEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                GpsContract.GyroscopeEntry.COLUMN_ID + " TEXT NOT NULL, " +
                GpsContract.GyroscopeEntry.COLUMN_TAG + " INTEGER NOT NULL, " +
                GpsContract.GyroscopeEntry.COLUMN_TIMESTAMP + " INTEGER NOT NULL, " +
                GpsContract.GyroscopeEntry.COLUMN_X + " REAL NOT NULL, " +
                GpsContract.GyroscopeEntry.COLUMN_Y + " REAL NOT NULL, " +
                GpsContract.GyroscopeEntry.COLUMN_Z + " REAL NOT NULL" +
                ");";
        db.execSQL(SQL_CREATE_GYROSCOPE_TABLE);
        Log.e("=====GYRO table=====","success!");
        final String SQL_CREATE_MOTIONSTATE_TABLE = "CREATE TABLE IF NOT EXISTS " +
                GpsContract.MotionStateEntry.TABLE_NAME + " (" +
                GpsContract.MotionStateEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                GpsContract.MotionStateEntry.COLUMN_ID + " TEXT NOT NULL, " +
                GpsContract.MotionStateEntry.COLUMN_TAG + " INTEGER NOT NULL, " +
                GpsContract.MotionStateEntry.COLUMN_TIMESTAMP + " INTEGER NOT NULL, " +
                GpsContract.MotionStateEntry.COLUMN_STATE + " INTEGER NOT NULL" +
                ");";
        db.execSQL(SQL_CREATE_MOTIONSTATE_TABLE);
        Log.e("=====MOTION table=====","success!");
        final String SQL_CREATE_STEPCOUNT_TABLE = "CREATE TABLE IF NOT EXISTS " +
                GpsContract.StepEntry.TABLE_NAME + " (" +
                GpsContract.StepEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                GpsContract.StepEntry.COLUMN_ID + " INTEGER NOT NULL, " +
                GpsContract.StepEntry.COLUMN_TAG + " INTEGER NOT NULL, " +
                GpsContract.StepEntry.COLUMN_TIMESTAMP + " INTEGER NOT NULL, " +
                GpsContract.StepEntry.COLUMN_COUNT + " INTEGER NOT NULL" +
                ");";
        db.execSQL(SQL_CREATE_STEPCOUNT_TABLE);
        Log.e("=====STEP table=====","success!");
        final String SQL_CREATE_BATTERY_TABLE = "CREATE TABLE IF NOT EXISTS " +
                GpsContract.BatteryEntry.TABLE_NAME + " (" +
                GpsContract.BatteryEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                GpsContract.BatteryEntry.COLUMN_ID + " INTEGER NOT NULL, " +
                GpsContract.BatteryEntry.COLUMN_TAG + " INTEGER NOT NULL, " +
                GpsContract.BatteryEntry.COLUMN_TIMESTAMP + " INTEGER NOT NULL, " +
                GpsContract.BatteryEntry.COLUMN_Percentage + " INTEGER NOT NULL" +
                ");";
        db.execSQL(SQL_CREATE_BATTERY_TABLE);
        Log.e("=====BATTERY table=====","success!");
        final String SQL_CREATE_WiFi_TABLE = "CREATE TABLE IF NOT EXISTS " +
                GpsContract.WiFiEntry.TABLE_NAME + " (" +
                GpsContract.WiFiEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                GpsContract.WiFiEntry.COLUMN_ID + " INTEGER NOT NULL, " +
                GpsContract.WiFiEntry.COLUMN_TAG + " INTEGER NOT NULL, " +
                GpsContract.WiFiEntry.COLUMN_TIMESTAMP + " INTEGER NOT NULL, " +
                GpsContract.WiFiEntry.COLUMN_State + " INTEGER NOT NULL" +
                ");";
        db.execSQL(SQL_CREATE_WiFi_TABLE);
        Log.e("=====WiFi table=====","success!");
        final String SQL_CREATE_MAGNETOMETER_TABLE = "CREATE TABLE IF NOT EXISTS " +
                GpsContract.MagnetometerEntry.TABLE_NAME + " (" +
                GpsContract.MagnetometerEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                GpsContract.MagnetometerEntry.COLUMN_ID + " TEXT NOT NULL, " +
                GpsContract.MagnetometerEntry.COLUMN_TAG + " INTEGER NOT NULL, " +
                GpsContract.MagnetometerEntry.COLUMN_TIMESTAMP + " INTEGER NOT NULL, " +
                GpsContract.MagnetometerEntry.COLUMN_X + " REAL NOT NULL, " +
                GpsContract.MagnetometerEntry.COLUMN_Y + " REAL NOT NULL, " +
                GpsContract.MagnetometerEntry.COLUMN_Z + " REAL NOT NULL" +
                ");";
        db.execSQL(SQL_CREATE_MAGNETOMETER_TABLE);
        Log.e("=====Mage table=====","success!");
        final String SQL_CREATE_PARKINGSTATE_TABLE = "CREATE TABLE IF NOT EXISTS " +
                GpsContract.ParkingStateEntry.TABLE_NAME + " (" +
                GpsContract.ParkingStateEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                GpsContract.ParkingStateEntry.COLUMN_ID + " TEXT NOT NULL, " +
                GpsContract.ParkingStateEntry.COLUMN_TAG + " INTEGER NOT NULL, " +
                GpsContract.ParkingStateEntry.COLUMN_TIMESTAMP + " INTEGER NOT NULL, " +
                GpsContract.ParkingStateEntry.COLUMN_STATE + " INTEGER NOT NULL" +
                ");";
        db.execSQL(SQL_CREATE_PARKINGSTATE_TABLE);
        Log.e("=====Park table=====","success!");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + GpsContract.GpsEntry.TABLE_NAME);
        Log.e("===update GPS table===","success!");
        onCreate(db);
    }
}
