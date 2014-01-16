package sk.gymdb.thinis.gcm.service;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.support.v4.app.FragmentActivity;
import android.util.Log;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.gcm.GoogleCloudMessaging;

import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import sk.gymdb.thinis.gcm.GcmServiceException;

/**
 * Created by matejkobza on 15.1.2014.
 */
public class GcmService {

    private final static int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;
    private static final String TAG = "GcmService";
    private static final String PROPERTY_REG_ID = "REGID";
    private static final String PROPERTY_APP_VERSION = "1";
//    private static final String SENDER_ID = "1045030114303";
//    private static final String HTTP_SERVER_ADDRESS = "http://192.168.2.54:8084/ThinIS-GCM-Server/register";

    private String senderId;
    private String serverUrl;
    private Context context;
    private GoogleCloudMessaging gcm;
    private String regId;


    public GcmService(FragmentActivity caller) throws GcmServiceException {
        context = caller.getApplicationContext();

        Properties properties = new Properties();
        try {
            // this is not working dont know why
            InputStream inputStream = context.getResources().getAssets().open("application.properties");
            properties.load(inputStream);
        } catch (IOException e) {
            throw new GcmServiceException(e);
        }

        this.senderId = properties.getProperty("sender.id");
        this.serverUrl = properties.getProperty("server.url");

        if (GcmService.checkPlayServices(caller)) {
            gcm = GoogleCloudMessaging.getInstance(caller);
            regId = getRegistrationId(context);

            if (regId == null) {
                registerInBackground();
            }
        } else {
            throw new GcmServiceException("Google play services unavailable.");
        }
    }

    /* GCM */
    public static boolean checkPlayServices(Activity activity) throws GcmServiceException {
        int resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(activity);
        if (resultCode != ConnectionResult.SUCCESS) {
            if (GooglePlayServicesUtil.isUserRecoverableError(resultCode)) {
                GooglePlayServicesUtil.getErrorDialog(resultCode, activity,
                        PLAY_SERVICES_RESOLUTION_REQUEST).show();
            } else {
                throw new GcmServiceException("This device is not supported");
            }
            return false;
        }
        return true;
    }

    private String getRegistrationId(Context context) throws GcmServiceException {
        final SharedPreferences prefs = getPreferences();
        String registrationId = prefs.getString(PROPERTY_REG_ID, "");

        if (registrationId.isEmpty()) {
            Log.i(TAG, "Registration not found.");
            return null;
        }
        // Check if app was updated; if so, it must clear the registration ID
        // since the existing regID is not guaranteed to work with the new
        // app version.
        int registeredVersion = prefs.getInt(PROPERTY_APP_VERSION, Integer.MIN_VALUE);
        int currentVersion = getAppVersion(context);
        if (registeredVersion != currentVersion) {
            throw new GcmServiceException("App version changed");
        }

        return registrationId;
    }

    private SharedPreferences getPreferences() {
        // This sample app persists the registration ID in shared preferences, but
        // how you store the regID in your app is up to you.
        return context.getSharedPreferences("APPLICATION",
                Context.MODE_PRIVATE);
    }

    private static int getAppVersion(Context context) throws GcmServiceException {
        try {
            PackageInfo packageInfo;
            packageInfo = context.getPackageManager()
                    .getPackageInfo(context.getPackageName(), 0);
            return packageInfo.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            // should never happen
            throw new GcmServiceException("Could not get package name: " + e);
        }
    }

    /**
     * Registers the application with GCM servers asynchronously.
     * <p/>
     * Stores the registration ID and app versionCode in the application's
     * shared preferences.
     */
    private void registerInBackground() {
        Registrator registrator = new Registrator();
        registrator.execute(null, null, null);
    }

    /**
     * Sends the registration ID to your server over HTTP, so it can use GCM/HTTP
     * or CCS to send messages to your app. Not needed for this demo since the
     * device sends upstream messages to a server that echoes back the message
     * using the 'from' address in the message.
     */
    private void sendRegistrationIdToBackend() throws GcmServiceException {
        HttpClient httpclient = new DefaultHttpClient();
        HttpPost httppost = new HttpPost(serverUrl + "register");

        try {
            List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(1);
            nameValuePairs.add(new BasicNameValuePair("regId", regId));
            httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
            httpclient.execute(httppost);
        } catch (Exception ex) {
            throw new GcmServiceException(ex);
        }
    }

    /**
     * Stores the registration ID and app versionCode in the application's
     * {@code SharedPreferences}.
     *
     * @param context application's context.
     * @param regId   registration ID
     */
    private void storeRegistrationId(Context context, String regId) throws GcmServiceException {
        final SharedPreferences prefs = getPreferences();
        int appVersion = getAppVersion(context);
        Log.i(TAG, "Saving regId on app version " + appVersion);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(PROPERTY_REG_ID, regId);
        editor.putInt(PROPERTY_APP_VERSION, appVersion);
        editor.commit();
    }


    private class Registrator extends AsyncTask<String, Void, Object> {

        @Override
        protected Object doInBackground(String... params) {
            String msg = null;
            try {
                if (gcm == null) {
                    gcm = GoogleCloudMessaging.getInstance(context);
                }
                regId = gcm.register(senderId);
//                msg = "Device registered, registration ID=" + regId;

                // You should send the registration ID to your server over HTTP,
                // so it can use GCM/HTTP or CCS to send messages to your app.
                // The request to your server should be authenticated if your app
                // is using accounts.
                sendRegistrationIdToBackend();

                // For this demo: we don't need to send it because the device
                // will send upstream messages to a server that echo back the
                // message using the 'from' address in the message.

                // Persist the regID - no need to register again.
                storeRegistrationId(context, regId);
            } catch (Exception ex) {
                msg = "Error :" + ex.getMessage();
                // If there is an error, don't just keep trying to register.
                // Require the user to click a button again, or perform
                // exponential back-off.
            }
            return msg;
        }

//@todo here should be some sort of notification to GUI about unsuccessful registration
//        @Override
//        protected void onPostExecute(Object o) {
//            if (o != null) {
//                throw new GcmServiceException(o.toString());
//            }
//        }
    }

}
