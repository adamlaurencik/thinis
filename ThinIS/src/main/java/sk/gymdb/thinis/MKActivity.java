package sk.gymdb.thinis;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

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
import java.util.ArrayList;
import java.util.List;

/**
 * Created by matejkobza on 16.12.2013.
 */
public class MKActivity extends FragmentActivity {

    private static final String TAG = "MKActivity";
    private final static int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;
    private static final String PROPERTY_REG_ID = "REGID";
    private static final String PROPERTY_APP_VERSION = "2";
    private static final String SENDER_ID = "1045030114303";
    private static final String HTTP_SERVER_ADDRESS = "http://192.168.2.54:8084/ThinIS-GCM-Server/register";

    private Context context;
    private GoogleCloudMessaging gcm;
    private String regId;
    private String clas;

    public void onCreate(Bundle savedState) {
        super.onCreate(savedState);
        setContentView(R.layout.mk);


        context = getApplicationContext();

        if (checkPlayServices()) {
            gcm = GoogleCloudMessaging.getInstance(this);
            regId = getRegistrationId(context);
                registerInBackground();
        } else {
            Toast.makeText(context, R.string.play_services_not_available, Toast.LENGTH_SHORT).show();
        }

        Button button = (Button)findViewById(R.id.register);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registerInBackground();
            }

        });
        DialogFragment fragment= new PickClassDialogFragment();
        fragment.show(getSupportFragmentManager(), "classPicker");
    }

    @Override
    protected void onResume() {
        super.onResume();
        checkPlayServices();
    }

    private boolean checkPlayServices() {
        int resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);
        if (resultCode != ConnectionResult.SUCCESS) {
            if (GooglePlayServicesUtil.isUserRecoverableError(resultCode)) {
                GooglePlayServicesUtil.getErrorDialog(resultCode, this,
                        PLAY_SERVICES_RESOLUTION_REQUEST).show();
            } else {
                Log.i(TAG, "This device is not supported.");
                finish();
            }
            return false;
        }
        return true;
    }
    public class PickClassDialogFragment extends DialogFragment {
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            // Use the Builder class for convenient dialog construction
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setTitle("Ktorú triedu navštevuješ?")
            .setItems(R.array.class_arrays, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                            clas="";
                  switch (which){
                      case 0: clas="I.A";
                          break;
                      case 1: clas="I.B";
                          break;
                      case 2: clas="II.A";
                          break;
                      case 3: clas="II.B";
                          break;
                      case 4: clas="II.C";
                          break;
                      case 5: clas="III.A";
                          break;
                      case 6: clas="III.B";
                          break;
                      case 7: clas="III.C";
                          break;
                      case 8: clas="IV.A";
                          break;
                      case 9: clas="IV.B";
                          break;
                      case 10: clas="IV.C";
                          break;
                      case 11: clas="I.PRIMA";
                          break;
                      case 12: clas="II.SEKUNDA";
                          break;
                      case 13: clas="III.TERCIA";
                          break;
                      case 14: clas="IV.KVARTA";
                          break;
                      case 15: clas="VI.SEXTA";
                          break;
                      case 16: clas="VII.SEPTIMA";
                          break;
                      case 17: clas="VIII.OKTAVA";
                          break;
                  }
                    final SharedPreferences prefs = getPreferences();
                    SharedPreferences.Editor editor = prefs.edit();
                    editor.putString("CLASS", clas);
                    editor.commit();
                }
            });
            return builder.create();
        }
    }

    private String getRegistrationId(Context context) {
        final SharedPreferences prefs = getPreferences();
        String registrationId = prefs.getString(PROPERTY_REG_ID, "");
        if (registrationId.isEmpty()) {
            Log.i(TAG, "Registration not found.");
            return "";
        }
        // Check if app was updated; if so, it must clear the registration ID
        // since the existing regID is not guaranteed to work with the new
        // app version.
        int registeredVersion = prefs.getInt(PROPERTY_APP_VERSION, Integer.MIN_VALUE);
        int currentVersion = getAppVersion(context);
        if (registeredVersion != currentVersion) {
            Log.i(TAG, "App version changed.");
            return "";
        }
        return registrationId;
    }

    private SharedPreferences getPreferences() {
        // This sample app persists the registration ID in shared preferences, but
        // how you store the regID in your app is up to you.
        return getSharedPreferences(MKActivity.class.getSimpleName(),
                Context.MODE_PRIVATE);
    }

    private static int getAppVersion(Context context) {
        try {
            PackageInfo packageInfo;
            packageInfo = context.getPackageManager()
                    .getPackageInfo(context.getPackageName(), 0);
            return packageInfo.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            // should never happen
            throw new RuntimeException("Could not get package name: " + e);
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
    private void sendRegistrationIdToBackend() {
        HttpClient httpclient = new DefaultHttpClient();
        HttpPost httppost = new HttpPost(HTTP_SERVER_ADDRESS);

        try {
            List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(1);
            nameValuePairs.add(new BasicNameValuePair("regId", regId));
            httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
            httpclient.execute(httppost);
        } catch (Exception ex) {
//            Toast.makeText(context, R.string.regid_registration_unsuccessful, Toast.LENGTH_LONG).show();
            Log.e(TAG, ex.getLocalizedMessage());
        }
    }

    /**
     * Stores the registration ID and app versionCode in the application's
     * {@code SharedPreferences}.
     *
     * @param context application's context.
     * @param regId   registration ID
     */
    private void storeRegistrationId(Context context, String regId) {
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
            String msg;
            try {
                if (gcm == null) {
                    gcm = GoogleCloudMessaging.getInstance(context);
                }
                regId = gcm.register(SENDER_ID);
                msg = "Device registered, registration ID=" + regId;

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
            } catch (IOException ex) {
                msg = "Error :" + ex.getMessage();
                // If there is an error, don't just keep trying to register.
                // Require the user to click a button again, or perform
                // exponential back-off.
            }
            return msg;
        }

        @Override
        protected void onPostExecute(Object o) {
            Toast.makeText(context, o.toString(), Toast.LENGTH_SHORT).show();
//                mDisplay.append(msg + "\n");
            PendingIntent contentIntent = PendingIntent.getActivity(context, 0,
                    new Intent(context, MKActivity.class), 0);
            NotificationCompat.Builder mBuilder =
                    new NotificationCompat.Builder(context)
                            .setSmallIcon(0)
                            .setContentTitle("My notification")
                            .setContentText("Hello World!");
            mBuilder.setContentIntent(contentIntent);
            mBuilder.setDefaults(Notification.DEFAULT_SOUND);
            mBuilder.setAutoCancel(true);
            NotificationManager mNotificationManager =
                    (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
            mNotificationManager.notify(1, mBuilder.build());

        }
    }

}
