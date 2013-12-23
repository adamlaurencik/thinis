/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package sk.gymdb.thinis.gcm;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import sk.gymdb.thinis.gcm.substitutions.SubstitutionNotificator;
import sk.gymdb.thinis.gcm.substitutions.SubstitutionParser;

/**
 * @author Admin
 * @modified matejkobza on 21.12.2013.
 */
public class Main {

    public static void main(String args[]) {
        Thread t = new Thread(new SubstitutionNotificator());

        /**while (true) {
            Calendar cal = Calendar.getInstance();
            cal.getTime();
            SimpleDateFormat sdf = new SimpleDateFormat("mm");
            if (sdf.format(cal.getTime()).equals("00")) {
                if (t.isAlive()) {
                    t.interrupt();
                }
                t.start();
            }**/
         SubstitutionParser parser= new SubstitutionParser();
         parser.GetData();
        }
    }


