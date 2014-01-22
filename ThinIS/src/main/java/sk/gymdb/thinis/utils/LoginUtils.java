package sk.gymdb.thinis.utils;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

import sk.gymdb.thinis.LoginActivity;
import sk.gymdb.thinis.R;

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
        if (!prefs.getString("username", "").equals("") && !prefs.getString("password", "").equals("")) {
            Log.i(TAG, "None credentials found");
            return false;
        }
        return true;
    }

}
