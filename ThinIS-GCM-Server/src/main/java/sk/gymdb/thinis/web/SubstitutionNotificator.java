package sk.gymdb.thinis.web;

import sk.gymdb.thinis.service.SubstitutionService;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashMap;
import java.util.List;

/**
 * Created by matejkobza on 21.12.2013.
 */
public class SubstitutionNotificator implements Runnable {

    private SubstitutionService service;

    public SubstitutionNotificator() {
        service = new SubstitutionService();
    }

    @Override
    public void run() {
        System.out.println("Hello from a thread!");
        // hashmap key is class, value is message
        HashMap<String, String> substitutions = null;
        try {
            substitutions = service.findSubstitutions();
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (!substitutions.isEmpty()) {
            for (String clazz : substitutions.keySet()) {
                // send substitution
                System.out.println(clazz + ", " + substitutions.get(clazz));
            }
        }
    }
}
