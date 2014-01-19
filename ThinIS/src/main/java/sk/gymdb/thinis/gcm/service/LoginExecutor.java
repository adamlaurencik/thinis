package sk.gymdb.thinis.gcm.service;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
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
public class LoginExecutor extends AsyncTask<String, Void, Object> {

    private final Context context;

    public LoginExecutor(Context context) {
        this.context = context;
    }

    @Override
    protected Object doInBackground(String... params) {
        String msg = null;
        Resources res=Resources.getSystem();
        SharedPreferences prefs = context.getSharedPreferences("APPLICATION", Context.MODE_PRIVATE);
        String username=params[0];
        String password=params[1];
        HttpClient client = new DefaultHttpClient();
        Properties props = new Properties();
        try {
            props.load(res.getAssets().open("application.properties"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        HttpPost post = new HttpPost(props.getProperty("server.url") + "/login");
        List<NameValuePair> pairList = new ArrayList<NameValuePair>();
        pairList.add(new BasicNameValuePair("u", username));
        pairList.add(new BasicNameValuePair("p", password));
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