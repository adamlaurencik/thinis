package sk.gymdb.thinis.gcm;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.support.v4.content.WakefulBroadcastReceiver;

/**
 * Created by Admin on 11/25/13.
 */
public class GcmBroadcastReceiver extends WakefulBroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        ComponentName comp= new ComponentName(context.getPackageName(),GcmBroadcastReceiver.class.getName());
        startWakefulService(context,intent.setComponent(comp));
        setResultCode(Activity.RESULT_OK);
    }
}
