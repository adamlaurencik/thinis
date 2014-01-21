package sk.gymdb.thinis.service;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.AssetManager;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.util.Log;

import com.google.gson.Gson;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.BasicManagedEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import sk.gymdb.thinis.LoginActivity;
import sk.gymdb.thinis.R;
import sk.gymdb.thinis.delegate.GradesDelegate;
import sk.gymdb.thinis.delegate.LoginDelegate;
import sk.gymdb.thinis.model.pojo.UserInfo;

/**
 * Created by Admin on 1/18/14.
 */
public class LoginService extends AsyncTask<String, Void, Object> {

    private static final String TAG = "LoginService";

    private final Context context;
    private final Gson gson;
    private LoginDelegate loginDelegate;
    private GradesDelegate gradesDelegate;

    public LoginService(Context context) {
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
                Log.e(TAG, "Unable to load application.properties");
            }
            HttpPost post = new HttpPost(props.getProperty("server.url") + "/login");
            List<NameValuePair> pairList = new ArrayList<NameValuePair>();
            pairList.add(new BasicNameValuePair("u", username));
            pairList.add(new BasicNameValuePair("p", password));
            try {
                post.setEntity(new UrlEncodedFormEntity(pairList));
            } catch (UnsupportedEncodingException e) {
                Log.e(TAG, "Unsupported encoding for passed parameters");
            }
            try {
                response = client.execute(post);
            } catch (IOException e) {
                Log.e(TAG, "Unable to execute post request");
            }
            entity = response.getEntity();
        }
        return entity;
    }

    @Override
    protected void onCancelled(Object o) {
        super.onCancelled(o);
        Log.i(TAG, "Task cancelled");
        loginDelegate.loginCancelled(o.toString());
    }

    @Override
    protected void onPostExecute(Object o) {
        super.onPostExecute(o);
        UserInfo info = new UserInfo();
        if (!(o == null)) {
            try {
                String sourceString = new String(EntityUtils.toString((BasicManagedEntity) o));
                info = gson.fromJson(sourceString, UserInfo.class);
            } catch (IOException e) {
                loginDelegate.loginUnsuccessful(context.getString(R.string.wrong_login_credentials));
                Log.e(TAG, "Login attempt was not successful due to exception");
            }
            if (info != null) {
                String name = info.getName();
                String grades = gson.toJson(info.getEvaluation());
                SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
                SharedPreferences.Editor edit = prefs.edit();
                edit.putString("name", name);
                edit.putString("grades", grades);
                edit.commit();
                Log.i(TAG, "Successful login");
                loginDelegate.loginSuccessful(name);
            } else {
                loginDelegate.loginUnsuccessful(context.getString(R.string.wrong_login_credentials));
                Log.i(TAG, "Login attempt was not successful due to wrong credentials");
            }
        }
    }


    public void setLoginDelegate(LoginDelegate delegate) {
        this.loginDelegate = delegate;
    }

    public void setGradesDelegate(GradesDelegate delegate) {
        this.gradesDelegate = delegate;
    }
}