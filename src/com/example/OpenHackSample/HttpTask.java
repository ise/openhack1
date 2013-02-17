package com.example.OpenHackSample;

import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import java.net.HttpURLConnection;

/**
 * Created with IntelliJ IDEA.
 * User: mastakeu
 * Date: 2013/02/17
 * Time: 10:37
 * To change this template use File | Settings | File Templates.
 */
public class HttpTask extends AsyncTask<String, Void, String> {
    @Override
    protected String doInBackground(String... strings) {
        Log.i("OPEN_HACK", "HttpTask doInBackground");
        HttpClient c = new DefaultHttpClient();
        String url = strings[0];
        Log.i("OPEN_HACK", "GET URL=" + url);
        HttpGet g = new HttpGet(url);
        String result = null;
        int retry = 0;
        while (true) {
            if (retry > 100) {
                break;
            }
            try{
                HttpResponse response = c.execute(g);
                StatusLine statusLine = response.getStatusLine();
                if(statusLine.getStatusCode() != HttpURLConnection.HTTP_OK){
                    Log.i("OPEN_HACK", "ng");
                    Log.i("OPEN_HACK", "sleep and retry");
                    Thread.sleep(3000);
                } else{
                    Log.i("OPEN_HACK", "ok");
                    break;
                }
            }
            catch(Exception e){
                e.printStackTrace();
            }
            retry++;
        }
        return result;
    }

    protected void onPostExecute(String str) {
        //textView.setText("POST Data: " + str);
    }
}
