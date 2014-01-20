package sk.gymdb.thinis.service;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

/**
 * Created by matejkobza on 20.1.2014.
 */
public class NetworkService {

    private static final String TAG = "NetworkService";

    private Context context;

    public NetworkService(Context context) {
        this.context = context;
    }

    public boolean isNetworkConnected() {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo ni = cm.getActiveNetworkInfo();
        if (ni != null) {
            Log.e(TAG, "No internet connection");
            return true;
        }
        return false;
    }

}
