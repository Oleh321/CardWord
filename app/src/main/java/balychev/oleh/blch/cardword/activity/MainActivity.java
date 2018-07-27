package balychev.oleh.blch.cardword.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import balychev.oleh.blch.cardword.R;
import balychev.oleh.blch.cardword.fragment.AddCardFragment;
import balychev.oleh.blch.cardword.fragment.DictionaryPagerFragment;
import balychev.oleh.blch.cardword.fragment.TodayRateFragment;
import balychev.oleh.blch.cardword.fragment.WordListsFragment;
import balychev.oleh.blch.cardword.utils.SystemUtils;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private Fragment mCurrentFragment;
    public static final String FRAGMENT_TAG = "fragment_tag";
    public static final String TAG = "myLogs";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        mCurrentFragment = getSupportFragmentManager().findFragmentByTag(FRAGMENT_TAG);
        if (mCurrentFragment == null){
            mCurrentFragment = new TodayRateFragment();
        }

        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.content_main, mCurrentFragment, FRAGMENT_TAG);
        ft.commit();

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this,
                drawer,
                toolbar,
                R.string.navigation_drawer_open,
                R.string.navigation_drawer_close) {

            @Override
            public void onDrawerStateChanged(int newState) {
                super.onDrawerStateChanged(newState);
                SystemUtils.hideKeyBoard(MainActivity.this);
            }
        };

        drawer.addDrawerListener(toggle);

        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        navigationView.setCheckedItem(R.id.nav_daily_rate);


    }

    @Override
    public void onBackPressed() {
        SystemUtils.hideKeyBoard(this);
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }



    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();
        Fragment fragment = new Fragment();

        if (id == R.id.nav_daily_rate) {
           fragment = new TodayRateFragment();
        } else if (id == R.id.nav_wordlist) {
            fragment = new WordListsFragment();
        } else if (id == R.id.nav_add_card) {
            fragment = new AddCardFragment();
        } else if (id == R.id.nav_dictionary) {
            fragment = new DictionaryPagerFragment();
        } else if (id == R.id.nav_settings) {

        }

        if (!fragment.getClass().getName().equals(mCurrentFragment.getClass().getName())) {
            mCurrentFragment = fragment;
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.content_main, mCurrentFragment, FRAGMENT_TAG);
            ft.commit();

        }

        SystemUtils.hideKeyBoard(this);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }



}
