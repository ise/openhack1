package com.example.OpenHackSample;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class MyActivity extends Activity implements MyReceiver.MyObserver
{
    public static final int NOTIFICATION_ID = 1;
    MyReceiver _receiver;

    TextView logText;
    TextView logText2;
    Button alarmStartButton;
    Button alarmStopButton;
    Button onButton;
    Button offButton;
    Button demoOnButton;
    Button demoOffButton;
    EditText editHost;
    Button updateButton;
    TextView logText3;

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        //UIを設定する
        setContentView(R.layout.main);
        logText = (TextView)findViewById(R.id.log_text);
        logText2 = (TextView)findViewById(R.id.log_text2);
        alarmStartButton = (Button)findViewById(R.id.start_button);
        alarmStopButton = (Button)findViewById(R.id.stop_button);
        onButton = (Button)findViewById(R.id.on_button);
        offButton = (Button)findViewById(R.id.off_button);
        demoOnButton = (Button)findViewById(R.id.demo_on_button);
        demoOffButton = (Button)findViewById(R.id.demo_off_button);
        //logText3 = (TextView)findViewById(R.id.log_text3);
        alarmStartButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                _startAlarm();
            }
        });
        alarmStopButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                _stopAlarm();
            }
        });
        editHost = (EditText)findViewById(R.id.edit_host);
        updateButton = (Button)findViewById(R.id.update_button);
        final ApiConfig apiConfig = ApiConfig.getInstance();
        editHost.setText(apiConfig.hostname);
        //apiConfig.hostname = "";//ここでIPアドレス設定？ InputTextから？
        onButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                HttpTask task = new HttpTask();
                task.execute(apiConfig.getUrl("on"));
            }
        });
        offButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                HttpTask task = new HttpTask();
                task.execute(apiConfig.getUrl("off"));
            }
        });
        demoOnButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                HttpTask task = new HttpTask();
                task.execute(apiConfig.getDemoUrl("on"));
            }
        });
        demoOffButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                HttpTask task = new HttpTask();
                task.execute(apiConfig.getDemoUrl("off"));
            }
        });
        updateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String newHost = editHost.getText().toString();
                if (!newHost.equals("")) {
                    Log.i("OPEN_HACK", "Update host " + newHost);
                    apiConfig.hostname = newHost;
                }
            }
        });

        IntentFilter filter = new IntentFilter(MyReceiver.ACTION_RESPONSE);
        filter.addCategory(Intent.CATEGORY_DEFAULT);
        _receiver = new MyReceiver(this);
        registerReceiver(_receiver, filter);

        Log.i("OPEN_HACK", "MyActivity done.");
    }

    public void onResume() {
        super.onResume();
        NotificationManager nm = (NotificationManager)getSystemService(NOTIFICATION_SERVICE);
        nm.cancel(MyActivity.NOTIFICATION_ID);
    }

    public void onDestroy() {
        super.onDestroy();
        unregisterReceiver(_receiver);
    }

    private void _startAlarm() {
        Log.i("OPEN_HACK", "Start alarm ... ");
        //AlarmManager経由でServiceを呼び出す
        Context context = getBaseContext();
        Intent intent = new Intent(this, WifiStatusWatcherService.class);
        PendingIntent pendingIntent = PendingIntent.getService(context, -1, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(ALARM_SERVICE);
        alarmManager.setInexactRepeating(AlarmManager.RTC, System.currentTimeMillis(), 5000, pendingIntent);
    }

    private void _stopAlarm() {
        Log.i("OPEN_HACK", "Stop alarm ... ");
        //AlarmManagerを止める
        Context context = getBaseContext();
        Intent intent = new Intent(this, WifiStatusWatcherService.class);
        PendingIntent pendingIntent = PendingIntent.getService(context, -1, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        AlarmManager alarmManager = (AlarmManager)context.getSystemService(ALARM_SERVICE);
        alarmManager.cancel(pendingIntent);
    }

    private void _clearFlag() {
        Log.i("OPEN_HACK", "Clear flag ... ");
        //Context context = getBaseContext();
    }

    @Override
    public void onUpdate(String message, String lightStatus) {
        Log.i("OPEN_HACK", "onUpdate " + message + ", " + lightStatus);
        logText.setText(message);
        if (!lightStatus.equals("")) {
            logText2.setText(lightStatus);
        }
    }

    public void onUpdateApiStatus(String message) {
        Log.i("OPEN_HACK", "onUpdateApiStatus " + message);
        logText3.setText(message);
    }

}
