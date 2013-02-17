package com.example.OpenHackSample;

/**
 * Created with IntelliJ IDEA.
 * User: mastakeu
 * Date: 2013/02/17
 * Time: 10:49
 * To change this template use File | Settings | File Templates.
 */
public class ApiConfig {
    private static ApiConfig _apiConfig = null;

    public String hostname = "xxxxxxxxx";

    private ApiConfig() {
    }

    public static ApiConfig getInstance() {
        if (_apiConfig == null) {
            return new ApiConfig();
        }
        return _apiConfig;
    }

    public String getUrl(String sw) {
        if (sw.equals("on")) {
            return "http://" + hostname + "/ohd1/light.php?switch=on";
        }
        return "http://" + hostname + "/ohd1/light.php?switch=off";
    }

    public String getDemoUrl(String sw) {
        if (sw.equals("on")) {
            return "http://" + hostname + "/ohd1/light.php?switch=on&type=demo";
        }
        return "http://" + hostname + "/ohd1/light.php?switch=off&type=demo";
    }
}
