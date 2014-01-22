package sk.gymdb.thinis.service;

import android.content.Context;
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
    private final NetworkService networkService;
    private LoginDelegate loginDelegate;
    private GradesDelegate gradesDelegate;

    /**
     * this is private class just to let onPostExecute what might have happened during run of
     * background task
     */
    private enum LoginResult {
        UNEXPECTED_ERROR, UNSUPPORTED_SERVER_RELATION, SERVER_UNAVAILABLE, NO_NETWORK_CONNECTION;
    }

    public LoginService(Context context) {
        this.context = context;
        gson = new Gson();
        networkService = new NetworkService(context);
    }

    @Override
    protected Object doInBackground(String... params) {
        if (!networkService.isNetworkConnected()) {
            return LoginResult.NO_NETWORK_CONNECTION;
        }
        HttpResponse response;
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
                return LoginResult.UNEXPECTED_ERROR;
//                loginDelegate.loginUnsuccessful(context.getString(R.string.unexpected_error));
            }
            HttpPost post = new HttpPost(props.getProperty("server.url") + "/login");
            List<NameValuePair> pairList = new ArrayList<NameValuePair>();
            pairList.add(new BasicNameValuePair("u", username));
            pairList.add(new BasicNameValuePair("p", password));
            try {
                post.setEntity(new UrlEncodedFormEntity(pairList));
            } catch (UnsupportedEncodingException e) {
                Log.e(TAG, "Unsupported encoding for passed parameters");
                return LoginResult.UNSUPPORTED_SERVER_RELATION;
//                loginDelegate.loginUnsuccessful(context.getString(R.string.unsupported_server_relation));
            }
            try {
                response = client.execute(post);
            } catch (IOException e) {
                Log.e(TAG, "Unable to execute post request");
                return LoginResult.SERVER_UNAVAILABLE;
//                loginDelegate.loginUnsuccessful(context.getString(R.string.server_unavailable));
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
        if (o instanceof LoginResult) {
            LoginResult result = (LoginResult) o;
            if (result.equals(LoginResult.UNEXPECTED_ERROR)) {
                loginDelegate.loginUnsuccessful(context.getString(R.string.unexpected_error));
            } else if (result.equals(LoginResult.UNSUPPORTED_SERVER_RELATION)) {
                loginDelegate.loginUnsuccessful(context.getString(R.string.unsupported_server_relation));
            } else if (result.equals(LoginResult.NO_NETWORK_CONNECTION)) {
                loginDelegate.loginUnsuccessful(context.getString(R.string.network_unavailable));
            } else {
                loginDelegate.loginUnsuccessful(context.getString(R.string.server_unavailable));
            }
        } else {
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
                    // might be need to add also username and login
                    edit.commit();
                    Log.i(TAG, "Successful login");
                    loginDelegate.loginSuccessful(name);
                } else {
                    loginDelegate.loginUnsuccessful(context.getString(R.string.wrong_login_credentials));
                    Log.i(TAG, "Login attempt was not successful due to wrong credentials");
                }
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