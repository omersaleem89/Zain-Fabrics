package com.zainfabrics.pk;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.zainfabrics.pk.fragments.CartFragment;
import com.zainfabrics.pk.fragments.HomeFragment;
import com.zainfabrics.pk.fragments.SearchFragment;
import com.zainfabrics.pk.fragments.models.ImageListModel;

import java.util.ArrayList;

import hotchemi.android.rate.AppRate;
import hotchemi.android.rate.OnClickButtonListener;


/**
 * Created by M Umer Saleem on 19-Nov-17.
 */

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    NavigationView navigationView;
    PrefManager prefManager;
    ArrayList<ImageListModel> categoryModelArrayList =new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        AppRate.with(this)
                .setInstallDays(0) // default 10, 0 means install day.
                .setLaunchTimes(5) // default 10
                .setRemindInterval(10) // default 1
                .setShowLaterButton(true) // default true
                .setDebug(false) // default false
                .setOnClickButtonListener(new OnClickButtonListener() { // callback listener.
                    @Override
                    public void onClickButton(int which) {
                        Log.d(MainActivity.class.getName(), Integer.toString(which));
                    }
                })
                .monitor();

        // Show a dialog if meets conditions
        AppRate.showRateDialogIfMeetsConditions(this);


        prefManager=new PrefManager(this);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        navigationView=(NavigationView)findViewById(R.id.nav_view) ;
        navigationView.setNavigationItemSelectedListener(this);

        Toolbar appbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(appbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);






        categoryModelArrayList=(ArrayList<ImageListModel>) getIntent().getSerializableExtra("categoryModelArrayList");
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();


        navigationView.getMenu().getItem(0).setChecked(true);
        navigationView.setCheckedItem(R.id.menu_home);
            Bundle bundle=new Bundle();
            Fragment fragment = new HomeFragment();
            bundle.putSerializable("categoryModelArrayList",categoryModelArrayList);
            FragmentManager fm = getSupportFragmentManager();
            fragment.setArguments(bundle);
            fm.beginTransaction().replace(R.id.frame_layout,fragment).commit();

    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        Fragment fragment=null;
        if(id==R.id.menu_shopping_cart){
            fragment=new CartFragment();
        }
        if(id==R.id.menu_search){
            fragment=new SearchFragment();
        }
        FragmentManager fm = getSupportFragmentManager();
        fm.beginTransaction().replace(R.id.frame_layout,fragment).commit();

        //noinspection SimplifiableIfStatement


        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        Fragment fragment = null;
        Bundle bundle=new Bundle();
        if(item.getItemId() == R.id.menu_home){
            fragment = new HomeFragment();
            bundle.putSerializable("categoryModelArrayList",categoryModelArrayList);
            fragment.setArguments(bundle);
            FragmentManager fm = getSupportFragmentManager();
            fm.beginTransaction().replace(R.id.frame_layout,fragment).addToBackStack(null).commit();
        }
        if(item.getItemId() == R.id.menu_share)
        {
            Intent sendIntent = new Intent();
            sendIntent.setAction(Intent.ACTION_SEND);
            sendIntent.putExtra(Intent.EXTRA_TEXT, "https://www.zainfabricspk.com");
            sendIntent.setType("text/plain");
            startActivity(sendIntent);
        }
        if(item.getItemId() == R.id.menu_change_country){
            prefManager.setFirstTimeLaunch(true);
            startActivity(new Intent(MainActivity.this,WelcomeActivity.class));
            finish();
        }
        if (item.getItemId() == R.id.menu_rate){
            AppRate.with(this).showRateDialog(this);
        }
//        else {
//            fragment=new SubCatFragment();
//            String title = item.getTitle().toString();
//            bundle.putString("categoryName",title);
//            fragment.setArguments(bundle);
//        }


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    }

