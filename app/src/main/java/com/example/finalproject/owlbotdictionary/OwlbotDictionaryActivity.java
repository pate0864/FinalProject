package com.example.finalproject.owlbotdictionary;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.example.finalproject.R;
import com.google.android.material.navigation.NavigationView;

public class OwlbotDictionaryActivity extends AppCompatActivity {

    DrawerLayout mDrawerLayout;
    Toolbar toolbar;
    NavigationView navigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_owlbot_dictionary);

        toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.project_owlbot_dictionary);
        setSupportActionBar(toolbar);

        mDrawerLayout = findViewById(R.id.owlbotDrawer);
        navigationView = findViewById(R.id.navigationView);
        setupNavigationDrawer();

        getSupportFragmentManager().beginTransaction().replace(R.id.frameLayout,
                new OwlbotDictionarySearchFragment()).commit();

    }

    private void setupNavigationDrawer(){
        ActionBarDrawerToggle actionBarDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, toolbar,
                R.string.drawer_open, R.string.drawer_close);
        mDrawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.owlbotSearch:
                        getSupportFragmentManager().beginTransaction().replace(R.id.frameLayout,
                                new OwlbotDictionarySearchFragment()).commit();
                        break;
                    case R.id.owlbotSaved:
                        getSupportFragmentManager().beginTransaction().replace(R.id.frameLayout,
                                new OwlbotDictionarySavedDefinitionFragment()).commit();
                        break;

                }
                mDrawerLayout.closeDrawer(GravityCompat.START);
                return true;
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.owlbot_context_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {

            case R.id.owlbotHelp:
                //showing alert dialog for help
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle(getResources().getString(R.string.help));
                builder.setPositiveButton(getResources().getString(R.string.owlbot_okay), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                builder.setMessage(getResources().getString(R.string.owlbot_help));
                builder.show();
                return true;
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