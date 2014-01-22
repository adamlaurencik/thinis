package sk.gymdb.thinis.fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import sk.gymdb.thinis.R;

/**
 * @author Matej Kobza
 */
public class DayOverviewFragment extends Fragment {

    private long time;

    public DayOverviewFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_day_overview, container, false);

        return rootView;
    }
}