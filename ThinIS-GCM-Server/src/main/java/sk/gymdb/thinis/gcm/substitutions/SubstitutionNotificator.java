package sk.gymdb.thinis.gcm.substitutions;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

/**
 * Created by matejkobza on 21.12.2013.
 */
public class SubstitutionNotificator implements Runnable {

    private void checkURL() {
        URL url = null;
        try {
            url = new URL("http://gymdb.edupage.org/substitution/?");
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        URLConnection urlConnection = null;
        BufferedReader in = null;
        String inputLine;
        StringBuilder a = new StringBuilder();

        try {
            urlConnection = url.openConnection();
            in = new BufferedReader(new InputStreamReader(
                    urlConnection.getInputStream(), "UTF-8"));
            while ((inputLine = in.readLine()) != null)
                a.append(inputLine);
            in.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
        String source = a.toString();

        System.out.println("PRAVE SOM PRECITAL STRANKU");
    }


    @Override
    public void run() {
        System.out.println("Hello from a thread!");

        checkURL();
    }
}
