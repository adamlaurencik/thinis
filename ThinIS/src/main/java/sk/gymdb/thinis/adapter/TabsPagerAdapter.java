package sk.gymdb.thinis.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.Calendar;

import sk.gymdb.thinis.fragment.DayOverviewFragment;

/**
 * Created with IntelliJ IDEA.
 * User: matejkobza
 * Date: 15.1.2014
 * Time: 17:02
 */
public class TabsPagerAdapter extends FragmentPagerAdapter {

    public TabsPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int index) {
        Calendar cal;

        switch (index) {
            case 0:
                cal = Calendar.getInstance();
                return new DayOverviewFragment(cal.getTimeInMillis());
            case 1:
                cal = Calendar.getInstance();
                cal.add(Calendar.DAY_OF_YEAR, 1);
                return new DayOverviewFragment(cal.getTimeInMillis());
            case 2:
                cal = Calendar.getInstance();
                cal.add(Calendar.DAY_OF_YEAR, 2);
                return new DayOverviewFragment(cal.getTimeInMillis());
        }

        return null;
    }

    @Override
    public int getCount() {
        // get item count - equal to number of tabs
        return 3;
    }

}
