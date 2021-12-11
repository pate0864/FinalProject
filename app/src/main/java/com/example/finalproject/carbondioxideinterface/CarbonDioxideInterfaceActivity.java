package com.example.finalproject.carbondioxideinterface;

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

public class CarbonDioxideInterfaceActivity extends AppCompatActivity {

    DrawerLayout mDrawerLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_carbon_dioxide_interface);
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.project_carbon_dioxide);
        setSupportActionBar(toolbar);

        mDrawerLayout = findViewById(R.id.drawerLayoutCarbonDioxide);
        ActionBarDrawerToggle actionBarDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, toolbar,
                R.string.drawer_open, R.string.drawer_close);
        actionBarDrawerToggle.syncState();
        NavigationView navigationView = findViewById(R.id.navigationView);


        getSupportFragmentManager().beginTransaction().replace(R.id.frameLayout,
                new CarbonDioxideSearchFragment()).commit();

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.carbonDioxideSearch:
                        getSupportFragmentManager().beginTransaction().replace(R.id.frameLayout,
                                new CarbonDioxideSearchFragment()).commit();
                        break;
                    case R.id.carbonDioxideSaved:
                        getSupportFragmentManager().beginTransaction().replace(R.id.frameLayout,
                                new CarbonDioxideSavedFragment()).commit();
                        break;

                }
                mDrawerLayout.closeDrawer(GravityCompat.START);
                return true;
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar_menu_carbon_dioxide, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {

            case R.id.menuHelp: {
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle(getResources().getString(R.string.help));
                builder.setPositiveButton(getResources().getString(R.string.carbon_dioxide_okay), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                builder.setMessage(getResources().getString(R.string.carbon_dioxide_help));
                builder.show();
                return true;
            }
            case R.id.changeLanguage:{
                Locale newLocale;
                if (getResources().getConfiguration().locale.getLanguage().equals(new Locale("hi").getLanguage())) {
                    newLocale = new Locale("en");
                } else {
                    newLocale = new Locale("hi");
                }

                DisplayMetrics dm = getResources().getDisplayMetrics();
                Configuration conf = getResources().getConfiguration();
                conf.locale = newLocale;
                getResources().updateConfiguration(conf, dm);
                startActivity(new Intent(this, CarbonDioxideInterfaceActivity.class));
                finish();
            }
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onBackPressed() {
        if (mDrawerLayout.isDrawerOpen(GravityCompat.START)) {
            mDrawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }
}