package sk.gymdb.thinis.fragment;

import android.app.Fragment;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
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

    private Gson gson;

    public GradesOverviewFragment() {
        gson = new Gson();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_grades_overview, container, false);

        Context context = getActivity().getApplicationContext();
        int color =Color.rgb(0, 65, 221);
        int color2=Color.rgb(0,121,217);
        rootView.setBackgroundColor(color);

        // gets grades from SharedPreferences and makes Userinfo from Json
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        String gradesJson = prefs.getString("grades", "");
        LinkedHashMap<String, ArrayList<String>> info = gson.fromJson(gradesJson, LinkedHashMap.class);
        ScrollView view= (ScrollView) rootView.findViewById(R.id.grades_scroll);
        TableLayout table = (TableLayout) rootView.findViewById(R.id.grades_table);
        table.setShrinkAllColumns(true);
        table.setVerticalGravity(LinearLayout.VERTICAL);
        if (info == null) {
            TextView noData = new TextView(context);
            ImageView smiley= new ImageView(context);
            Bitmap smileyBitmap= BitmapFactory.decodeResource(context.getResources(),R.drawable.sad);
            Display mDisplay = getActivity().getWindowManager().getDefaultDisplay();
            smileyBitmap=smileyBitmap.createScaledBitmap(smileyBitmap,mDisplay.getWidth()/2,mDisplay.getWidth()/2,false);
            smiley.setImageBitmap(smileyBitmap);
            noData.setGravity(Gravity.CENTER);
            noData.setText("Žiadne záznamy o známkach");
            table.addView(smiley);
            table.addView(noData);
        } else {
        TableRow header = new TableRow(context);
        header.setBackgroundColor(color);
        header.setGravity(Gravity.CENTER);
        TextView headerClazz = new TextView(context);
        TextView headerGrades = new TextView(context);
        headerClazz.setPadding(0, 15, 10, 15);
        headerGrades.setPadding(10, 15, 0, 15);
        headerClazz.setText("PREDMET");
        headerGrades.setText("ZNÁMKY");

        headerClazz.setTextSize(20);
        headerGrades.setTextSize(20);

        header.addView(headerClazz);
        header.addView(headerGrades);
        table.addView(header);

            int rowColor=color;
        // loops trough keys of HashMap, keys are subjcet names

            for (String key : info.keySet()) {
                // creates new table row and two textViews
                TableRow row = new TableRow(context);

                TextView subject = new TextView(context);
                TextView grades = new TextView(context);
                grades.setPadding(10, 15, 0, 15);
                subject.setPadding(0, 15, 10, 15);
                // makes TextViews adapt to the text (if it is longer than one row it puts text in it into another row)
                subject.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.MATCH_PARENT));
                grades.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.MATCH_PARENT));
                subject.setText(key);
                String gradesText = "";
                // loops trough ArrayList of grades and writes them to string, simple toString() will show it with []
                for (String s : info.get(key)) {
                    if (gradesText.equals("")) {
                        gradesText = s;
                    } else {
                        gradesText = gradesText + ", "+ s;
                    }
                }
                grades.setText(gradesText);
                row.setGravity(Gravity.CENTER);
                if(rowColor==color2) rowColor=color;
                else rowColor=color2;
                row.setBackgroundColor(rowColor);
                row.addView(subject);
                row.addView(grades);
                table.addView(row);
            }
        }
        return view;
    }

}
