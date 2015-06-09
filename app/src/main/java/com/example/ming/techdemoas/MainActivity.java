package com.example.ming.techdemoas;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
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
            FragmentLocator.setFragmentManager(getSupportFragmentManager());

            super.onCreate(null);
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

    public void setCurrentPosition(int currPos) {
        fragmentIndex = currPos;
    }

    @Override
    public void onNavigationDrawerItemSelected(int position) {
        // update the main content by replacing fragments
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .hide(FragmentLocator.Get(fragmentIndex))
                .show(FragmentLocator.Get(position))
                .commit();

        setCurrentPosition(position);

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

    @Override
    protected void onStop() {
        super.onStop();
        FragmentLocator.InvalidateAll();
    }
}
