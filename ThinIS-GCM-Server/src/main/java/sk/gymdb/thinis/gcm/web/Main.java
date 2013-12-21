/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package sk.gymdb.thinis.gcm.web;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author Admin
 */
public class Main implements Runnable {

    public void run() {
        System.out.println("Hello from a thread!");

        checkURL();
    }

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

    public static void main(String args[]) {
        Thread t = new Thread(new Main());

        while (true) {
            Calendar cal = Calendar.getInstance();
            cal.getTime();
            SimpleDateFormat sdf = new SimpleDateFormat("mm");
            if (sdf.format(cal.getTime()).equals("00")) {
                if (!t.isAlive()) {
                    t.interrupt();
                }
                t.start();
            }
        }
    }

}
