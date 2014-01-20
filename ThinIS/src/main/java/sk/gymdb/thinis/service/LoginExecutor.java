package sk.gymdb.thinis.service;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.os.AsyncTask;
import android.preference.PreferenceManager;

import com.google.gson.Gson;

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

import sk.gymdb.thinis.delegate.GradesDelegate;
import sk.gymdb.thinis.delegate.LoginDelegate;
import sk.gymdb.thinis.model.pojo.UserInfo;

/**
 * Created by Admin on 1/18/14.
 */
public class LoginExecutor extends AsyncTask<String, Void, Object> {

    private final Context context;
    private final Gson gson;
    private LoginDelegate loginDelegate;
    private GradesDelegate gradesDelegate;

    public LoginExecutor(Context context) {
        this.context = context;
        gson = new Gson();
    }

    @Override
    protected Object doInBackground(String... params) {
        HttpResponse response = null;
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        String username = prefs.getString("username", "");
        String password = prefs.getString("password", "");
        AssetManager assetManager = context.getAssets();
        HttpEntity entity = null;
        if (!(username.equals("") || password.equals(""))) {
            HttpClient client = new DefaultHttpClient();
            Properties props = new Properties();
            try {
                props.load(assetManager.open("application.properties"));
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
            try {
                response = client.execute(post);
            } catch (IOException e) {
                e.printStackTrace();
            }
            entity = response.getEntity();
        }
        return entity;
    }

    @Override
    protected void onPostExecute(Object o) {
        super.onPostExecute(o);
        UserInfo info = new UserInfo();
        if (!(o == null)) {
            try {
                info = gson.fromJson(EntityUtils.toString((HttpEntity) o), UserInfo.class);
            } catch (IOException e) {
                loginDelegate.loginUnsuccessful("Zadali ste zlé hodnoty");
                e.printStackTrace();
            }
            String name = info.getName();
            String grades = gson.toJson(info.getEvaluation());
            SharedPreferences prefs = context.getSharedPreferences("APPLICATION", Context.MODE_PRIVATE);
            SharedPreferences.Editor edit = prefs.edit();
            edit.putString("name", name);
            edit.putString("grades", grades);
            edit.commit();
            loginDelegate.loginSuccessful("yes");

        }
    }

    public void setLoginDelegate(LoginDelegate delegate) {
        this.loginDelegate = delegate;
    }

    public void setGradesDelegate(GradesDelegate delegate) {
        this.gradesDelegate = delegate;
    }
}