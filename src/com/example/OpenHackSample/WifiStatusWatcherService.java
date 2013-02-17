package com.example.OpenHackSample;

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;

/**
 * Created with IntelliJ IDEA.
 * User: mastakeu
 * Date: 2013/02/16
 * Time: 13:14
 * To change this template use File | Settings | File Templates.
 */
public class WifiStatusWatcherService extends IntentService {
    private Handler _handler;
    private final String MY_HOME_SSID = "XXXX";
    private static boolean _isValidStatus = false;

    public WifiStatusWatcherService(String name) {
        super(name);
    }

    public WifiStatusWatcherService() {
        super("WifiStatusWatcherService");
        _handler = new Handler();
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        Log.i("OPEN_HACK", "WifiStatusWatcherService.onHandleIntent");
        try {
            final Context context = this.getApplicationContext();
            ConnectivityManager cm = (ConnectivityManager) context.getSystemService(CONNECTIVITY_SERVICE);
            NetworkInfo wifiInfo = cm.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
            MyWifiStatus status = MyWifiStatus.getInstance();
            String lightStatus = "";
            if (wifiInfo.isConnected()) {
                Log.i("OPEN_HACK", "wifi connected");
                WifiManager manager = (WifiManager) context.getSystemService(WIFI_SERVICE);
                WifiInfo info = manager.getConnectionInfo();
                final String ssid = info.getSSID();
                Log.i("OPEN_HACK", "SSID=" + ssid);

                //登録されたSSIDであるか判定
                if (ssid.equals(MY_HOME_SSID)) {
                    final int duration = Toast.LENGTH_SHORT;

                    //初回のみ表示する
                    if (!_isValidStatus) {
                    /*
                    _handler.post(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(context, "(SSID: " + ssid + ") == MY_HOME_SSID", duration).show();
                        }
                    });
                    */
                        //通知バーに通知
                        _notify(context, "connected", "自宅のWiFiに接続されました");
                        ApiConfig apiConfig = ApiConfig.getInstance();
                        HttpTask task = new HttpTask();
                        task.execute(apiConfig.getUrl("on"));
                        lightStatus = "照明を点灯";
                    }
                    _isValidStatus = true;
                } else {
                    if (_isValidStatus) {
                        //true -> falseへの切り替わり
                        _notify(context, "disconnected", "自宅のWiFiから切断されました");
                        ApiConfig apiConfig = ApiConfig.getInstance();
                        HttpTask task = new HttpTask();
                        task.execute(apiConfig.getUrl("off"));
                        lightStatus = "照明を消灯";
                    }
                    _isValidStatus = false;
                }
                status.name = ssid;
            } else {
                if (_isValidStatus) {
                    //true -> falseへの切り替わり
                    _notify(context, "disconnected", "自宅のWiFiから切断されました");
                    ApiConfig apiConfig = ApiConfig.getInstance();
                    HttpTask task = new HttpTask();
                    task.execute(apiConfig.getUrl("off"));
                    lightStatus = "照明を消灯";
                }
                _isValidStatus = false;
                Log.i("OPEN_HACK", "wifi not connected");
                status.name = "disconnected";
            }

            //receiverに通知
            Log.i("OPEN_HACK", "sendBroadcast");
            Intent sendIntent = new Intent();
            sendIntent.setAction(MyReceiver.ACTION_RESPONSE);
            sendIntent.putExtra("message", status.name);
            sendIntent.putExtra("lightStatus", lightStatus);
            sendBroadcast(sendIntent);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void _notify(Context context, String text, String message) {
        Notification n = new Notification();
        n.icon = R.drawable.icon;
        n.tickerText = text;
        n.number = 1;
        n.when = System.currentTimeMillis();
        Intent i = new Intent(context, MyActivity.class);
        PendingIntent pi = PendingIntent.getActivity(this, 0, i, 0);
        n.setLatestEventInfo(context, "SmartMyHome", message, pi);

        NotificationManager nm = (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
        nm.notify(MyActivity.NOTIFICATION_ID,n);

        Log.i("OPEN_HACK", "Notified.");
    }

}
