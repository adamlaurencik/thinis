package sk.gymdb.thinis.gcm.service;

import android.os.AsyncTask;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

/**
 * Created by Admin on 1/18/14.
 */
private class LoginExecutor extends AsyncTask<String, Void, Object> {

    @Override
    protected Object doInBackground(String... params) {
        String msg = null;
        HttpClient client = new DefaultHttpClient();
        Properties props = new Properties();
        try {
            props.load(getResources().getAssets().open("application.properties"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        HttpPost post = new HttpPost(props.getProperty("server.url") + "/login");
        List<NameValuePair> pairList = new ArrayList<NameValuePair>();
        pairList.add(new BasicNameValuePair("u", "AdamLaurencik"));
        pairList.add(new BasicNameValuePair("p", "970520/4960"));
        try {
            post.setEntity(new UrlEncodedFormEntity(pairList));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        HttpResponse response = null;
        try {
            response = client.execute(post);
        } catch (IOException e) {
            e.printStackTrace();
        }
        HttpEntity entity = response.getEntity();

        try {
            System.out.println(EntityUtils.toString(entity));
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println(EntityUtils.getContentCharSet(entity));
        System.out.println(response.getEntity());
        return response;
    }


}