package sk.gymdb.thinis.fragment;

import android.app.Fragment;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.Calendar;

import sk.gymdb.thinis.R;
import sk.gymdb.thinis.model.pojo.Substitution;

/**
 * @author Matej Kobza
 */
public class TodayOverviewFragment extends Fragment {

    private long time;

    public TodayOverviewFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Context context = getActivity().getApplicationContext();
        int color = Color.rgb(166, 42, 42);
        int color2 = Color.rgb(255, 64, 64);
        View rootView = inflater.inflate(R.layout.fragment_grades_overview, container, false);
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        Gson gson = new Gson();
        ScrollView view = (ScrollView) rootView.findViewById(R.id.grades_scroll);
        TableLayout table = (TableLayout) rootView.findViewById(R.id.grades_table);
        rootView.setBackgroundColor(color);
        table.setShrinkAllColumns(true);
        ArrayList<Substitution> substitutions = gson.fromJson(prefs.getString("substitutions", ""), new TypeToken<ArrayList<Substitution>>() {
        }.getType());
        TableRow header = new TableRow(context);
        header.setBackgroundColor(color);
        // create all TextViews
        TextView headerHour = new TextView(context);
        TextView headerSubject = new TextView(context);
        TextView headerTeacher = new TextView(context);
        TextView headerClazz = new TextView(context);
        TextView headerComment = new TextView(context);
        // set Gravity
        headerHour.setGravity(Gravity.CENTER_HORIZONTAL);
        headerSubject.setGravity(Gravity.CENTER_HORIZONTAL);
        headerTeacher.setGravity(Gravity.CENTER_HORIZONTAL);
        headerClazz.setGravity(Gravity.CENTER_HORIZONTAL);
        headerComment.setGravity(Gravity.CENTER_HORIZONTAL);
        // set Padding
        headerHour.setText("KEDY?");
        headerSubject.setText("ČO?");
        headerTeacher.setText("KTO?");
        headerClazz.setText("KDE?");
        headerComment.setText("PS");

        headerHour.setText("KEDY?");
        headerSubject.setText("ČO?");
        headerTeacher.setText("KTO?");
        headerClazz.setText("KDE?");
        headerComment.setText("PS");

        headerHour.setPadding(0, 0, 6, 10);
        headerSubject.setPadding(6, 0, 6, 10);
        headerTeacher.setPadding(6, 0, 6, 10);
        headerClazz.setPadding(6, 0, 6, 10);
        headerComment.setPadding(6, 0, 0, 10);
        //add Textviews to row
        boolean atLeastOne = false;
        header.setBackgroundColor(color);
        table.addView(header);
        int rowColor = color;
        Calendar cal = Calendar.getInstance();
        if (cal.get(Calendar.DAY_OF_WEEK) == 7) { // friday we need 2 more days
            cal.add(Calendar.DAY_OF_YEAR, 1);
        } else if (cal.get(Calendar.DAY_OF_WEEK) == 1) { // saturday we still need one more
            cal.add(Calendar.DAY_OF_YEAR, 1);
        }

        if (!(substitutions == null)) {
            for (Substitution s : substitutions) {
                if (cal.get(Calendar.DAY_OF_MONTH) == (s.getDate().getDate())) {
                    atLeastOne = true;
                    TableRow row = new TableRow(context);
                    TextView hour = new TextView(context);
                    TextView subject = new TextView(context);
                    TextView teacher = new TextView(context);
                    TextView clazz = new TextView(context);
                    TextView comment = new TextView(context);
                    hour.setGravity(Gravity.CENTER_HORIZONTAL);
                    subject.setGravity(Gravity.CENTER_HORIZONTAL);
                    teacher.setGravity(Gravity.CENTER_HORIZONTAL);
                    clazz.setGravity(Gravity.CENTER_HORIZONTAL);
                    comment.setGravity(Gravity.CENTER_HORIZONTAL);

                    hour.setPadding(0, 0, 6, 10);
                    subject.setPadding(6, 0, 6, 10);
                    teacher.setPadding(6, 0, 6, 10);
                    clazz.setPadding(6, 0, 6, 10);
                    comment.setPadding(6, 0, 0, 10);
                    hour.setText(s.getHour());
                    subject.setText(s.getSubject());
                    teacher.setText(s.getTeacher());
                    clazz.setText(s.getClazz());
                    comment.setText(s.getComment());
                    if (s.isCanceled()) {
                        hour.setPaintFlags(hour.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                        subject.setPaintFlags(hour.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                        teacher.setPaintFlags(hour.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                        clazz.setPaintFlags(hour.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                    }
                    row.addView(hour);
                    row.addView(subject);
                    row.addView(teacher);
                    row.addView(clazz);
                    row.addView(comment);
                    if (rowColor == color2) rowColor = color;
                    else rowColor = color2;
                    row.setBackgroundColor(rowColor);
                    table.addView(row);
                } else if (cal.get(Calendar.DAY_OF_MONTH) - 2 > (s.getDate().getDate())) {
                    substitutions.remove(s);
                    SharedPreferences.Editor edit = prefs.edit();
                    edit.putString("substitutions", gson.toJson(substitutions));
                }
              }}

        if (atLeastOne) {
            //add Textviews to row
            header.addView(headerHour);
            header.addView(headerSubject);
            header.addView(headerTeacher);
            header.addView(headerClazz);
            header.addView(headerComment);

        } else {
            TextView noData = new TextView(context);
            ImageView smiley = new ImageView(context);
            Bitmap smileyBitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.sad);
            Display mDisplay = getActivity().getWindowManager().getDefaultDisplay();
            smileyBitmap = smileyBitmap.createScaledBitmap(smileyBitmap, mDisplay.getWidth() / 2, mDisplay.getWidth() / 2, false);
            table.setLayoutParams(new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT, Gravity.CENTER_VERTICAL));
            smiley.setImageBitmap(smileyBitmap);
            noData.setGravity(Gravity.CENTER);
            noData.setText("Nie sú žiadne údaje o suplovaní");
            table.addView(smiley);
            table.addView(noData);
        }


        return view;
    }
}