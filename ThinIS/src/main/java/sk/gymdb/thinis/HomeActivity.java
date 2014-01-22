package sk.gymdb.thinis;

import android.app.ActionBar;
import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import sk.gymdb.thinis.fragment.DayOverviewFragment;
import sk.gymdb.thinis.fragment.GradesOverviewFragment;

/**
 * Created with IntelliJ IDEA.
 * User: matejkobza
 * Date: 15.1.2014
 * Time: 16:59
 */
public class HomeActivity extends Activity {


    private ActionBar actionBar;
    private Resources res;
    private String[] tabs;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        res = getResources();
        tabs = res.getStringArray(R.array.tabs);

        // Initilization
        actionBar = getActionBar();
        actionBar.setHomeButtonEnabled(false);
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

        // tabs
        actionBar.addTab(actionBar.newTab().setText(tabs[0])
                .setTabListener(new TabListener(this, tabs[0], DayOverviewFragment.class)));
        actionBar.addTab(actionBar.newTab().setText(tabs[1])
                .setTabListener(new TabListener(this, tabs[1], DayOverviewFragment.class)));
        actionBar.addTab(actionBar.newTab().setText(tabs[2])
                .setTabListener(new TabListener(this, tabs[2], GradesOverviewFragment.class)));


        /**
         * on swiping the viewpager make respective tab selected
         * */
//        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
//
//            @Override
//            public void onPageSelected(int position) {
//                on changing the page
//                make respected tab selected
//                actionBar.setSelectedNavigationItem(position);
//            }
//
//            @Override
//            public void onPageScrolled(int arg0, float arg1, int arg2) {
//            }
//
//            @Override
//            public void onPageScrollStateChanged(int arg0) {
//            }
//        });
//
//        if (savedInstanceState != null) {
//            actionBar.setSelectedNavigationItem(savedInstanceState.getInt("tab", 0));
//        }
//
    }


    public static class TabListener<T extends Fragment> implements ActionBar.TabListener {
        private final Activity mActivity;
        private final String mTag;
        private final Class<T> mClass;
        private final Bundle mArgs;
        private Fragment mFragment;

        public TabListener(Activity activity, String tag, Class<T> clz) {
            this(activity, tag, clz, null);
        }

        public TabListener(Activity activity, String tag, Class<T> clz, Bundle args) {
            mActivity = activity;
            mTag = tag;
            mClass = clz;
            mArgs = args;

            // Check to see if we already have a fragment for this tab, probably
            // from a previously saved state.  If so, deactivate it, because our
            // initial state is that a tab isn't shown.
            mFragment = mActivity.getFragmentManager().findFragmentByTag(mTag);
            if (mFragment != null && !mFragment.isDetached()) {
                FragmentTransaction ft = mActivity.getFragmentManager().beginTransaction();
                ft.detach(mFragment);
                ft.commit();
            }
        }

        public void onTabSelected(ActionBar.Tab tab, FragmentTransaction ft) {
            if (mFragment == null) {
                mFragment = Fragment.instantiate(mActivity, mClass.getName(), mArgs);
                ft.add(android.R.id.content, mFragment, mTag);
            } else {
                ft.attach(mFragment);
            }
        }

        public void onTabUnselected(ActionBar.Tab tab, FragmentTransaction ft) {
            if (mFragment != null) {
                ft.detach(mFragment);
            }
        }

        public void onTabReselected(ActionBar.Tab tab, FragmentTransaction ft) {
        }
    }


// this is later

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt("tab", getActionBar().getSelectedNavigationIndex());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.action_bar, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent activityChangeIntent;
        switch (item.getItemId()) {
            case R.id.menu_item_refresh:
                // todo implement refresh
                return true;
            case R.id.menu_item_login:
                activityChangeIntent = new Intent(HomeActivity.this, LoginActivity.class);
                HomeActivity.this.startActivity(activityChangeIntent);
                return true;
            case R.id.menu_item_settings:
                activityChangeIntent = new Intent(HomeActivity.this, SettingsActivity.class);
                HomeActivity.this.startActivity(activityChangeIntent);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

//    public void setRefreshActionButtonState(final boolean refreshing) {
//        if (this.actionBarMenu != null) {
//            final MenuItem refreshItem = actionBarMenu
//                    .findItem(R.id.airport_menuRefresh);
//            if (refreshItem != null) {
//                if (refreshing) {
//                    refreshItem.setActionView(R.layout.layout_action_bar_progress);
//                } else {
//                    refreshItem.setActionView(null);
//                }
//            }
//        }
//    }
}