package com.example.OpenHackSample;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

/**
 * Created with IntelliJ IDEA.
 * User: mastakeu
 * Date: 2013/02/17
 * Time: 0:33
 * To change this template use File | Settings | File Templates.
 */
public class MyReceiver extends BroadcastReceiver {
    private MyObserver _observer;
    public static final String ACTION_RESPONSE = "com.example.OpenHackSample.android.intent.action.MESSAGE";

    public MyReceiver(MyObserver obs) {
        super();
        _observer = obs;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.i("OPEN_HACK", "onReceive");
        if (intent.hasExtra("api")) {
            String message = intent.getStringExtra("message");
            _observer.onUpdateApiStatus(message);
        } else {
            String message = intent.getStringExtra("message");
            String lightStatus = intent.getStringExtra("lightStatus");
            _observer.onUpdate(message, lightStatus);
        }
    }

    public interface MyObserver {
        public void onUpdate(String message, String lightStatus);
        public void onUpdateApiStatus(String message);
    }

}
