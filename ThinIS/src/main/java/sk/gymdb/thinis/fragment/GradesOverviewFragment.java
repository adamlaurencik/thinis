package sk.gymdb.thinis.fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.LinkedHashMap;

import sk.gymdb.thinis.R;

/**
 * Created by matejkobza on 18.1.2014.
 */
public class GradesOverviewFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_grades_overview, container, false);

        // todo tuto zobraz znamky, ale len tie, ktore mas uz ulozene v preferences, nenacitavaj ich pokazde
        // todo nacitavanie sa ma spravit pri logine a potom pri stlaceni tlacidla refresh v hornom actionbare
        Context context = getActivity().getApplicationContext();
        // gets grades from SharedPreferences and makes Userinfo from Json
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        Gson gson = new Gson();
        String gradesJson = prefs.getString("grades", "");
        LinkedHashMap<String, ArrayList<String>> info = new Gson().fromJson(gradesJson, LinkedHashMap.class);
        TableLayout table = (TableLayout) rootView.findViewById(R.id.table);
        table.setShrinkAllColumns(true);
        TableRow header = new TableRow(context);
        TextView headerClazz = new TextView(context);
        TextView headerGrades = new TextView(context);
        headerClazz.setText("Predmet");
        headerGrades.setText("Známky");
        headerClazz.setTextSize(20);
        headerGrades.setTextSize(20);
        header.addView(headerClazz);
        header.addView(headerGrades);
        table.addView(header);
        // loops trough keys of HashMap, keys are subjcet names
        for (String key : info.keySet()) {
            // creates new table row and two textViews
            TableRow row = new TableRow(context);
            TextView subject = new TextView(context);
            TextView grades = new TextView(context);
            grades.setPadding(3, 2, 2, 0);
            subject.setPadding(3, 2, 2, 0);
            // makes TextViews adapt to the text (if it is longer than one row it puts text in it into another row)
            subject.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.MATCH_PARENT));
            grades.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.MATCH_PARENT));
            subject.setText(key);
            String gradesText = "";
            // loops trough ArrayList of grades and writes them to string, simple toString() will show it with []
            for (String s : info.get(key)) {
                if (gradesText.equals("")) {
                    gradesText = s + ", ";
                } else {
                    gradesText = gradesText + s + ", ";
                }
                ;
            }
            grades.setText(gradesText);
            row.addView(subject);
            row.addView(grades);
            table.addView(row);
        }


        return table;
    }

}
