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
        Context context= getActivity().getApplicationContext();
        // gets grades from SharedPreferences and makes Userinfo from Json
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        Gson gson=new Gson();
        String name = prefs.getString("name", "");
        String gradesJson = prefs.getString("grades", "");
        LinkedHashMap<String, ArrayList<String>> info = new Gson().fromJson(gradesJson, LinkedHashMap.class);
        TableLayout table= (TableLayout) rootView.findViewById(R.id.table);
        TextView textView= (TextView) rootView.findViewById(R.id.GradesText);
        textView.setText(name);
        for (String key : info.keySet()) {
            TableRow row = new TableRow(context);
            TextView subject= new TextView(context);
            TextView grades= new TextView(context);
            subject.setText(key);
            grades.setText(info.get(key).toString());
            row.addView(subject);
            row.addView(grades);
            table.addView(row);
        }



        return table;
    }

}
