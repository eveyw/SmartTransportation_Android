package com.llu17.youngq.smartpark;


import android.Manifest;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.PowerManager;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.preference.PreferenceManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.stetho.Stetho;
import com.facebook.stetho.okhttp3.StethoInterceptor;
import com.llu17.youngq.smartpark.data.GpsContract;
import com.llu17.youngq.smartpark.data.GpsDbHelper;

import java.io.File;
import java.lang.reflect.Method;

import okhttp3.OkHttpClient;

import static com.llu17.youngq.smartpark.CollectorService.mark;


public class MainActivity extends AppCompatActivity implements SharedPreferences.OnSharedPreferenceChangeListener{


    private String sampling_rate;
    private boolean manualInput;
    static TextView upload_state;
    private Button MarkBusStop;
    private Switch DataCollectionSwitch, AutoCheckUploadSwitch;
    private SharedPreferences.Editor editor;
    private SharedPreferences prefs;
    public static Handler mHandler;
    private LinearLayout manualInputPart;
    //used to save parking lots info
    private GpsDbHelper dbHelper_pl;
    private SQLiteDatabase mDb_pl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        dbHelper_pl = new GpsDbHelper(this);

        MarkBusStop = (Button)findViewById(R.id.Mark_Bus_Stop);
        MarkBusStop.setOnClickListener(new Button.OnClickListener()
        {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                mark[0] = true;
                Toast.makeText(getApplicationContext(), "Mark this position as bus stop", Toast.LENGTH_SHORT).show();
            }

        });

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        /*used to keep the status recorded, otherwise everytime reopen the app, switch will be off*/
        editor = getSharedPreferences("com.llu17.youngq.sqlite_gps", MODE_PRIVATE).edit();
        prefs = getSharedPreferences("com.llu17.youngq.sqlite_gps", MODE_PRIVATE);

        Log.e("lalalala","i am here!!!");
        manualInputPart = (LinearLayout) findViewById(R.id.Manual_Part);
        if(!prefs.getBoolean("manualInput_status",false))
            manualInputPart.setVisibility(View.GONE);
        else
            manualInputPart.setVisibility(View.VISIBLE);

        DataCollectionSwitch = (Switch) findViewById(R.id.DataCollectionSwitch);
        AutoCheckUploadSwitch = (Switch) findViewById(R.id.AutoCheckUploadSwitch);



        //不能只使用一次监听注册,否则当重新进入main activity的时候,settings中值的改变不会影响main activity的布局
        //因此对应的layout改变不是当前main activity的了(对应的是第一次的)
//        flagOfSharedPreferences = prefs.getBoolean("SharedPreferencesStatus",false);
//        if(!flagOfSharedPreferences) {
        sharedPreferences.registerOnSharedPreferenceChangeListener(this);
//            editor.putBoolean("SharedPreferencesStatus", true);
//            editor.commit();
//        }



        DataCollectionSwitch.setChecked(prefs.getBoolean("DCS_status",false));
        AutoCheckUploadSwitch.setChecked(prefs.getBoolean("ACU_status",false));
        /*===check sqlite data using "chrome://inspect"===*/
        Stetho.initializeWithDefaults(this);
        new OkHttpClient.Builder()
                .addNetworkInterceptor(new StethoInterceptor())
                .build();
        //////////
        upload_state = (TextView)findViewById(R.id.Upload_State);
        upload_state.setVisibility(View.GONE);


        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION,
                    Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.INTERNET}, 10);

        }


//        int v = 0;
//        try {
//            v = getPackageManager().getPackageInfo("com.google.android.gms", 0 ).versionCode;
//        } catch (PackageManager.NameNotFoundException e) {
//            e.printStackTrace();
//        }
//        Log.e("version:-------",""+ v);

        //Can't use:
        //ACTION_REQUEST_IGNORE_BATTERY_OPTIMIZATIONS
        //android.content.ActivityNotFoundException: No Activity found to handle Intent { act=android.settings.REQUEST_IGNORE_BATTERY_OPTIMIZATIONS }
        //Can use:
        //ACTION_IGNORE_BATTERY_OPTIMIZATION_SETTINGS
        try {
            Intent intent = new Intent();
            String packageName = this.getPackageName();
            Log.e("pachageName: ", packageName);
            PowerManager pm = (PowerManager) this.getSystemService(Context.POWER_SERVICE);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (!pm.isIgnoringBatteryOptimizations(packageName)){

//                    Intent intent = new Intent(Settings.ACTION_IGNORE_BATTERY_OPTIMIZATION_SETTINGS);
                    intent.setAction(Settings.ACTION_REQUEST_IGNORE_BATTERY_OPTIMIZATIONS);
                    intent.setData(Uri.parse("package:" + packageName));
                    startActivity(intent);
                }
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }

        /*write file*/
        ActivityCompat.requestPermissions(this, new String[]{android
                .Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);

        DataCollectionSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean bChecked) {
                Log.e("DataCollectionSwitch","i am here !");
                editor.putBoolean("DCS_status", bChecked);
                editor.commit();
                if (bChecked) {
                    startService();
                } else {
                    stopService();
                }
            }
        });
        AutoCheckUploadSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean bChecked) {
                Log.e("AutoCheckUploadSwitch","i am here !");
                editor.putBoolean("ACU_status", bChecked);
                editor.commit();
                if (bChecked) {
                    uploadService();
                } else {
                    breakService();
                }
            }
        });

    }
    /*Sampling rate menu*/
    //Add the menu to the menu bar
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.sampling_rate_menu, menu);
        return true;
    }
    //When the "Settings" menu item is pressed, open SettingsActivity
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            Intent startSettingsActivity = new Intent(this, SettingsActivity.class);
            startActivity(startSettingsActivity);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        if (key.equals(getString(R.string.sr_key_all))) {
            sampling_rate = sharedPreferences.getString(key, "1000");
            Log.e("-----ALL SR2-----","changed: "+sampling_rate);

        } else if (key.equals("checkboxPref_MI")){
            manualInput = sharedPreferences.getBoolean(key, false);
            if(manualInput) {
                manualInputPart.setVisibility(View.VISIBLE);
                editor.putBoolean("manualInput_status", true);
                editor.commit();
            }
            else {

                manualInputPart.setVisibility(View.GONE);
                editor.putBoolean("manualInput_status", false);
                editor.commit();
            }
        }
    }
    public void startService() {        //startService(View view) 如果使用button.click去控制就要使用:startService(View v)
        PreferenceManager.getDefaultSharedPreferences(this)
                .unregisterOnSharedPreferenceChangeListener(this);
        Log.e("unregister","success");

        Toast.makeText(this, "Starting the service", Toast.LENGTH_SHORT).show();
        startService(new Intent(getBaseContext(), CollectorService.class));
        startService(new Intent(getBaseContext(), Activity_Tracker.class));
    }

    // Method to stop the service
    public void stopService() {         //stopService(View view)
        Toast.makeText(this, "Stopping the service", Toast.LENGTH_SHORT).show();
        stopService(new Intent(getBaseContext(), CollectorService.class));
        stopService(new Intent(getBaseContext(), Activity_Tracker.class));
        stopService(new Intent(getBaseContext(), HandleActivity.class));

    }

    public void uploadService(){        //uploadService(View view)
        Toast.makeText(this, "Begin to upload data automatically", Toast.LENGTH_SHORT).show();
        startService(new Intent(getBaseContext(), UploadService.class));
    }

    public void breakService(){        //breakService(View view)
        Toast.makeText(this, "Stop to upload data automatically", Toast.LENGTH_SHORT).show();
        stopService(new Intent(getBaseContext(), UploadService.class));
    }

    public void uploadServiceM(){      //uploadServiceM(View view)
        Toast.makeText(this, "Begin to upload data manually", Toast.LENGTH_SHORT).show();
        startService(new Intent(getBaseContext(), UploadServiceM.class));
    }

    public void breakServiceM(View view){       //breakServiceM(View view)
        Toast.makeText(this, "Stop to upload data manually", Toast.LENGTH_SHORT).show();
        stopService(new Intent(getBaseContext(), UploadServiceM.class));
    }

    public void parkingRecord(View view){
        final int[] flag = {0};   //default as 0  (-1 : none) (1 : 0~5) (2 : 5~10) (3 : 10+)
        AlertDialog.Builder dialog = new AlertDialog.Builder(MainActivity.this);
        dialog.setTitle("Parking Lots info");
        dialog.setSingleChoiceItems(R.array.parking_lots_left_array, -1, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                flag[0] = which;
            }
        });
        dialog.setPositiveButton(R.string.save, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
                long temp_time = System.currentTimeMillis();
                int tag = 0;
                String pl_id = getSerialNumber();
                mDb_pl = dbHelper_pl.getWritableDatabase();
                
                ContentValues cv_park = new ContentValues();
                cv_park.put(GpsContract.ParkingStateEntry.COLUMN_ID, pl_id);
                cv_park.put(GpsContract.ParkingStateEntry.COLUMN_TAG, tag);
                cv_park.put(GpsContract.ParkingStateEntry.COLUMN_TIMESTAMP, temp_time);
                cv_park.put(GpsContract.ParkingStateEntry.COLUMN_STATE, flag[0]);

                try {
                    mDb_pl.beginTransaction();
                    mDb_pl.insert(GpsContract.ParkingStateEntry.TABLE_NAME, null, cv_park);
                    mDb_pl.setTransactionSuccessful();
                } catch (SQLException e) {
                    //too bad :(
                } finally {
                    mDb_pl.endTransaction();
                }
                mDb_pl.close();

                Toast.makeText(MainActivity.this, "parking info: " + flag[0], Toast.LENGTH_SHORT).show();
            }
        })
        .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
                Toast.makeText(MainActivity.this, "Do nothing!", Toast.LENGTH_SHORT).show();
            }
        });
        dialog.show();
    }

    public void manualUpload(View view){
        AlertDialog.Builder dialog = new AlertDialog.Builder(MainActivity.this);
        dialog.setTitle("Manual Upload");
        dialog.setMessage("Would you like to upload data to Server? (make sure WiFi connected)");
        dialog.setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
                uploadServiceM();
            }
        })
                .setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        Toast.makeText(MainActivity.this, "Do nothing!", Toast.LENGTH_SHORT).show();
                    }
                });
        dialog.show();

    }


    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        sharedPreferences.registerOnSharedPreferenceChangeListener(this);
    }

    private static String getSerialNumber(){

        String serial = null;

        try {

            Class<?> c =Class.forName("android.os.SystemProperties");

            Method get =c.getMethod("get", String.class);

            serial = (String)get.invoke(c, "ro.serialno");

        } catch (Exception e) {

            e.printStackTrace();

        }

        return serial;

    }
//    @Override
//    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
//        switch (requestCode) {
//            case 1:
//                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//                    //创建文件夹
//                    if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
//                        File file = new File(Environment.getExternalStorageDirectory() + "/test/");
//                        if (!file.exists()) {
//                            Log.d("result", "create result:" + file.mkdirs());
//                        }
//                    }
//                    break;
//                }
//        }
//    }
}
