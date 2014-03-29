package sk.gymdb.thinis.gcm.service;

import android.app.IntentService;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.google.gson.Gson;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;

import sk.gymdb.thinis.HomeActivity;
import sk.gymdb.thinis.R;
import sk.gymdb.thinis.gcm.receivers.GcmBroadcastReceiver;
import sk.gymdb.thinis.model.pojo.Substitution;


/**
 * Created by Admin on 11/25/13.
 */
public class GcmIntentService extends IntentService {

    private static final String TAG = "GcmIntentService";
    public static int notificationID;
    private NotificationManager mNotificationManager;
    private Context context=this;

    public GcmIntentService() {
        super("GcmIntentService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        Bundle extras = intent.getExtras();
        GoogleCloudMessaging gcm = GoogleCloudMessaging.getInstance(this);
        // The getMessageType() intent parameter must be the intent you received
        // in your BroadcastReceiver.
        String messageType = gcm.getMessageType(intent);

        if (!extras.isEmpty()) {  // has effect of unparcelling Bundle
            /*
             * Filter messages based on message type. Since it is likely that GCM
             * will be extended in the future with new message types, just ignore
             * any message types you're not interested in, or that you don't
             * recognize.
             */
            if (GoogleCloudMessaging.
                    MESSAGE_TYPE_SEND_ERROR.equals(messageType)) {
                sendNotification("Send error: " + extras.toString());
            } else if (GoogleCloudMessaging.
                    MESSAGE_TYPE_DELETED.equals(messageType)) {
                sendNotification("Deleted messages on server: " +
                        extras.toString());
                // If it's a regular GCM message, do some work.
            } else if (GoogleCloudMessaging.
                    MESSAGE_TYPE_MESSAGE.equals(messageType)) {
                Gson gson= new Gson();
                try {
                    String jsonString= URLDecoder.decode(extras.getString("SPRAVA"), "UTF-8");
                Substitution s= gson.fromJson(jsonString, Substitution.class);

                String msg=s.toString();
                ArrayList<Substitution> newSubstitutions;
                String jsonSubstitutions;
                SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
                if (prefs.getString("substitutions", "").equals("")) {
                    newSubstitutions= new ArrayList<Substitution>();
                }else{
                    newSubstitutions= gson.fromJson(prefs.getString("substitutions", ""),ArrayList.class);
                }
                newSubstitutions.add(s);
                SharedPreferences.Editor edit = prefs.edit();
                edit.putString("substitutions",gson.toJson(newSubstitutions));
                edit.commit();
                notificationID=s.getID();
                sendNotification(msg);

                Log.i(TAG, "Received: " + extras.getString("SPRAVA"));
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }


            }
        }
        // Release the wake lock provided by the WakefulBroadcastReceiver.
        GcmBroadcastReceiver.completeWakefulIntent(intent);
    }

    // Put the message into a notification and post it.
    // This is just one simple example of what you might choose to do with
    // a GCM message.
    private void sendNotification(String msg) {
        mNotificationManager = (NotificationManager)
                this.getSystemService(Context.NOTIFICATION_SERVICE);

        PendingIntent contentIntent = PendingIntent.getActivity(this, 0,
                new Intent(this, HomeActivity.class), 0);
        Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        Bitmap icon= BitmapFactory.decodeResource(context.getResources(), R.drawable.notification2);
        long [] pattern={250,250,250,250};
        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(this)
                      .setSmallIcon(R.drawable.notification)
                      .setLargeIcon(icon)
                        .setContentTitle("Suplovanie")
                        .setStyle(new NotificationCompat.BigTextStyle()
                                .bigText(msg))
                        .setContentText(msg)
                        .setSound(alarmSound)
                        .setVibrate(pattern)
                        .setLights(Color.BLUE, 500, 500);
        mBuilder.setContentIntent(contentIntent);
        mNotificationManager.notify(notificationID, mBuilder.build());

    }
}
