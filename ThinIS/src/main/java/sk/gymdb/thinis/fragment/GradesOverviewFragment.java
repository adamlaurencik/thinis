package sk.gymdb.thinis.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import sk.gymdb.thinis.R;

/**
 * Created by matejkobza on 18.1.2014.
 */
public class GradesOverviewFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_day_overview, container, false);

        // todo tuto zobraz znamky, ale len tie, ktore mas uz ulozene v preferences, nenacitavaj ich pokazde
        // todo nacitavanie sa ma spravit pri logine a potom pri stlaceni tlacidla refresh v hornom actionbare

        return rootView;
    }

}
