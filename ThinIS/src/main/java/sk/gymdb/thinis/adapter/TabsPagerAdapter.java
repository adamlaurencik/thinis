package sk.gymdb.thinis.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.Calendar;

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
//            return Fragment.instantiate(context, )
        }
        return null;
}

    @Override
    public int getCount() {
        // get item count - equal to number of tabs
        return 3;
    }

}
