package sk.gymdb.thinis;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import sk.gymdb.thinis.delegate.GradesDelegate;
import sk.gymdb.thinis.fragment.DayOverviewFragment;
import sk.gymdb.thinis.fragment.GradesOverviewFragment;
import sk.gymdb.thinis.service.LoginService;

/**
 * Created with IntelliJ IDEA.
 * User: matejkobza
 * Date: 15.1.2014
 * Time: 16:59
 */
public class HomeActivity extends Activity implements GradesDelegate {


    private Menu actionBarMenu;
    private LoginService loginService;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        loginService = new LoginService(getApplicationContext());
        loginService.setGradesDelegate(this);

        Resources res = getResources();
        String[] tabs = res.getStringArray(R.array.tabs);

        // Initilization
        ActionBar actionBar = getActionBar();
        actionBar.setHomeButtonEnabled(false);
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

        // tabs
        actionBar.addTab(actionBar.newTab().setText(tabs[0])
                .setTabListener(new TabListener(this, tabs[0], DayOverviewFragment.class)));
        actionBar.addTab(actionBar.newTab().setText(tabs[1])
                .setTabListener(new TabListener(this, tabs[1], DayOverviewFragment.class)));
        actionBar.addTab(actionBar.newTab().setText(tabs[2])
                .setTabListener(new TabListener(this, tabs[2], GradesOverviewFragment.class)));
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt("tab", getActionBar().getSelectedNavigationIndex());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        this.actionBarMenu = menu;
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.action_bar, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent activityChangeIntent;
        switch (item.getItemId()) {
            case R.id.menu_item_refresh:
                showProgress(true);
                loginService.execute();
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

    public void showProgress(final boolean refreshing) {
        if (this.actionBarMenu != null) {
            final MenuItem refreshItem = actionBarMenu
                    .findItem(R.id.menu_item_refresh);
            if (refreshItem != null) {
                if (refreshing) {
                    refreshItem.setActionView(R.layout.layout_action_bar_progress);
                } else {
                    refreshItem.setActionView(null);
                }
            }
        }
    }

    @Override
    public void refreshSuccessful(String message) {
        showProgress(false);
        // todo refresh grades overview fragment
    }

    @Override
    public void refreshUnsuccessful(String message) {
        showProgress(false);
        AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setTitle(R.string.refresh);
        dialog.setMessage(message);
        dialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
//                goToMainActivity();
            }
        });
        dialog.show();
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
}