package sk.gymdb.thinis.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

/**
 * Created by matejkobza on 20.1.2014.
 */
public class LoginUtils {

    private static final String TAG = "LoginUtils";

    /**
     * check if the user has filled username and password, if not ask for username and password
     */
    public static boolean credentialsAvailable(Context context) {

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        if (prefs.getString("username", "").equals("") && prefs.getString("password", "").equals("")) {
            Log.i(TAG, "None credentials found");
            return false;
        }
        return true;
    }

}
