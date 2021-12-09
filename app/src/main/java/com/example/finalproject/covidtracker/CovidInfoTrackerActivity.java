package com.example.finalproject.covidtracker;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Menu;
import android.view.MenuItem;

import com.example.finalproject.R;
import com.google.android.material.navigation.NavigationView;

import java.util.Locale;

public class CovidInfoTrackerActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    DrawerLayout mDrawerLayout;
    Toolbar toolbar;
    NavigationView navigationView;

    /**
     *
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_covid_info_tracker);

        toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.project_covid19_tracker);
        setSupportActionBar(toolbar);

        mDrawerLayout = findViewById(R.id.covidDrawer);
        navigationView = findViewById(R.id.navigationView);
        ActionBarDrawerToggle actionBarDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, toolbar,
                R.string.drawer_open, R.string.drawer_close);
        mDrawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();

        navigationView.setNavigationItemSelectedListener(this);

        getSupportFragmentManager().beginTransaction().replace(R.id.frameLayout,
                new CovidInfoSearchFragment()).commit();
    }

    /**
     * @param item
     * @return
     * Using Navigation bar
     */
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.covidSearchInfo:
                getSupportFragmentManager().beginTransaction().replace(R.id.frameLayout,
                        new CovidInfoSearchFragment()).commit();
                break;
            case R.id.covidSavedInfo:
                        getSupportFragmentManager().beginTransaction().replace(R.id.frameLayout,
                                new CovidInfoSavedFragment()).commit();
                break;

        }
        mDrawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.covid_help_menu, menu);
        return true;
    }

    /**
     *
     * @param item
     * @return
     * Tis functions is used for selecting the options which we have created in the menu layout file
     */
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {

            case R.id.covidHelp: {
                //showing alert dialog for help
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle(getResources().getString(R.string.help));
                builder.setPositiveButton(getResources().getString(R.string.covid_okay), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                builder.setMessage(getResources().getString(R.string.covid_help_text));
                builder.show();
                return true;
            }
            case R.id.covidChangeLanguage:{
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle(getResources().getString(R.string.covid_change_language));
                builder.setPositiveButton("English", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Locale newLocale = new Locale("en");
                        DisplayMetrics dm = getResources().getDisplayMetrics();
                        Configuration conf = getResources().getConfiguration();
                        conf.locale = newLocale;
                        getResources().updateConfiguration(conf, dm);
                        startActivity(new Intent(CovidInfoTrackerActivity.this, CovidInfoTrackerActivity.class));

                        dialog.dismiss();
                        finish();
                    }
                });
                builder.setNegativeButton("Hindi", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Locale newLocale = new Locale("hi");
                        DisplayMetrics dm = getResources().getDisplayMetrics();
                        Configuration conf = getResources().getConfiguration();
                        conf.locale = newLocale;
                        getResources().updateConfiguration(conf, dm);
                        startActivity(new Intent(CovidInfoTrackerActivity.this, CovidInfoTrackerActivity.class));
                        dialog.dismiss();
                        finish();
                    }
                });
                builder.show();
            }
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onBackPressed() {
        //close the drawer before exit
        if (mDrawerLayout.isDrawerOpen(GravityCompat.START)) {
            mDrawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }
}