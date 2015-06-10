package com.example.ming.techdemoas;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;

import com.example.ming.techdemoas.Fragments.IUserFragment;


public class MainActivity extends ActionBarActivity
        implements NavigationDrawerFragment.NavigationDrawerCallbacks {

    private NavigationDrawerFragment NavigationDrawerFragment;
    private int fragmentIndex;

    /**
     * Used to store the last screen title. For use in {@link # restoreActionBar()}.
     */
    private CharSequence mTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        if (savedInstanceState == null) {
            super.onCreate(null);

            FragmentLocator.setFragmentManager(getSupportFragmentManager());
            setContentView(R.layout.activity_main);

            NavigationDrawerFragment = (NavigationDrawerFragment)
                    getSupportFragmentManager().findFragmentById(R.id.navigation_drawer);
            mTitle = getTitle();

            // Set up the drawer.
            getNavigationDrawerFragment().setUp(
                    R.id.navigation_drawer,
                    (DrawerLayout) findViewById(R.id.drawer_layout));
        } else {
            super.onCreate(savedInstanceState);
        }
    }

    @Override
    public void onNavigationDrawerItemSelected(int position) {
        // update the main content by replacing fragments
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction trans = fragmentManager.beginTransaction();

        if (FragmentLocator.isAnimNeeded(position)) {
            trans.setCustomAnimations(R.anim.abc_slide_in_bottom, R.anim.abc_slide_out_top,
                    R.anim.abc_slide_in_top, R.anim.abc_slide_out_bottom)
                    .addToBackStack("S");
            NavigationDrawerFragment.setDrawerEnabled(false, fragmentIndex);
        }

        trans.hide(FragmentLocator.Get(fragmentIndex))
                .show(FragmentLocator.Get(position))
                .commit();

        fragmentIndex = position;

        IUserFragment frag = (IUserFragment) FragmentLocator.Get(position);
        if (frag != null) {
            mTitle = frag.GetTitle();
        }
        restoreActionBar();
    }

    public void restoreActionBar() {
        ActionBar actionBar = getSupportActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setTitle(mTitle);
    }

    /**
     * Fragment managing the behaviors, interactions and presentation of the navigation drawer.
     */
    public com.example.ming.techdemoas.NavigationDrawerFragment getNavigationDrawerFragment() {
        return NavigationDrawerFragment;
    }
}
