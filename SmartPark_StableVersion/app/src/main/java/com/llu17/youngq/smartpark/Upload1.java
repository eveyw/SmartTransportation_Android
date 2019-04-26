package com.llu17.youngq.smartpark;

import android.content.ContentValues;
import android.database.SQLException;
import android.util.Log;

import com.llu17.youngq.smartpark.data.GpsContract;

import java.util.TimerTask;
import java.util.concurrent.Semaphore;

import static com.llu17.youngq.smartpark.CollectorService.id;
import static com.llu17.youngq.smartpark.CollectorService.mDb;

/**
 * Created by youngq on 17/4/14.
 */

public class Upload1 extends TimerTask {
    int count = 0;
    int[] nums1, nums2; //1: battery percentage     2: wifi state
    int tag = 0;
    private Semaphore semaphoreTransaction = new Semaphore(1);

    public Upload1(int[] a, int[] b){
        nums1 = a;
        nums2 = b;
    }


    @Override
    public void run() {
        count++;
        long temp_time = System.currentTimeMillis();

        ContentValues cv_battery = new ContentValues();
        cv_battery.put(GpsContract.BatteryEntry.COLUMN_ID,id);
        cv_battery.put(GpsContract.BatteryEntry.COLUMN_TAG, tag);
        cv_battery.put(GpsContract.BatteryEntry.COLUMN_TIMESTAMP,temp_time);
        cv_battery.put(GpsContract.BatteryEntry.COLUMN_Percentage, nums1[0]);

        ContentValues cv_wifi = new ContentValues();
        cv_wifi.put(GpsContract.WiFiEntry.COLUMN_ID,id);
        cv_wifi.put(GpsContract.WiFiEntry.COLUMN_TAG, tag);
        cv_wifi.put(GpsContract.WiFiEntry.COLUMN_TIMESTAMP,temp_time);
        cv_wifi.put(GpsContract.WiFiEntry.COLUMN_State, nums2[0]);

        mDb.beginTransaction();
        try
        {
            semaphoreTransaction.acquire();
            mDb.insert(GpsContract.BatteryEntry.TABLE_NAME, null, cv_battery);
            mDb.insert(GpsContract.WiFiEntry.TABLE_NAME, null, cv_wifi);
            mDb.setTransactionSuccessful();
            Log.e("===insert B&W===","success!" + count);
        }
        catch (SQLException e) {
            //too bad :(
        } catch (InterruptedException e) {
            Log.e("semaphoreTransaction1","error");
            e.printStackTrace();
        } finally
        {
            mDb.endTransaction();
            semaphoreTransaction.release();
        }
    }

}
