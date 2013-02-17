package com.example.OpenHackSample;

/**
 * Created with IntelliJ IDEA.
 * User: mastakeu
 * Date: 2013/02/16
 * Time: 19:53
 * To change this template use File | Settings | File Templates.
 */
public class MyWifiStatus {
    private static MyWifiStatus _wifiStatus = null;

    public String name = null;

    private MyWifiStatus() {
    }

    public static MyWifiStatus getInstance() {
        if (_wifiStatus == null) {
            return new MyWifiStatus();
        }
        return _wifiStatus;
    }

}
