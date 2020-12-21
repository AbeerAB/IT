package com.h.itservices.activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.messaging.FirebaseMessaging;
import com.h.itservices.R;
import com.h.itservices.fragments.All_Home;
import com.h.itservices.fragments.SeekerQuestions;
import com.h.itservices.fragments.MyQuestions;

public class MainActivity extends AppCompatActivity  {


    public static Context mContext;

    private FirebaseAuth mAuth;
    public SharedPreferences prefs;
    public static String MY_PREFS_NAME = "itservice";
    public static SharedPreferences.Editor editor;
    private ActionBarDrawerToggle drawerToggle;
    private  Toolbar toolbar;
    private NavigationView drawer;
    private DrawerLayout drawerLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mContext=this;

        prefs = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
        editor = prefs.edit();
        mAuth = FirebaseAuth.getInstance();
        FirebaseMessaging.getInstance().subscribeToTopic(FirebaseAuth.getInstance().getCurrentUser().getUid());

        toolbar = findViewById(R.id.main_toolbar);

        drawerLayout = findViewById(R.id.drawer_layout);
        drawer = findViewById(R.id.drawer);
        if(prefs.getString("user_type","").equalsIgnoreCase("admin")){

            startActivity(new Intent(MainActivity.this,AdminActivity.class));

        }

        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("الصفحة الرئيسية ");
        //getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        setupDrawerToggle();
//        viewPager.setOffscreenPageLimit(1);
        drawer.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();

                handlerDrawerClick(id);

                return true;
            }
        });

        drawerLayout.closeDrawers();

        All_Home home = new All_Home();
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.container, home)
                .commit();






    }


    private void handlerDrawerClick(int id){
        switch (id){
            case R.id.nav_home:
                drawerLayout.closeDrawers();

                All_Home home = new All_Home();
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.container, home).addToBackStack("")
                        .commit();

                break;
            case R.id.nav_MyQuestion:
                drawerLayout.closeDrawers();
                MyQuestions myQuestions = new MyQuestions();
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.container, myQuestions).addToBackStack("")
                        .commit();

                break;

                case R.id.nav_SeekerQuestions:
                drawerLayout.closeDrawers();
                    SeekerQuestions seekerQuestions = new SeekerQuestions();
                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.container, seekerQuestions).addToBackStack("")
                            .commit();

                break;
            case R.id.nav_out:
                FirebaseAuth.getInstance().signOut();
                Intent logout = new Intent(MainActivity.this, LoginActivity.class);
                startActivity(logout);
                break;
            case R.id.nav_profile:
                drawerLayout.closeDrawers();

                Intent profile = new Intent(MainActivity.this, ProfileActivity.class);
                startActivity(profile); break;
            default:
                break;


        }
    }


    private void setupDrawerToggle(){
        drawerToggle = new ActionBarDrawerToggle(MainActivity.this, drawerLayout, toolbar, R.string.app_name, R.string.app_name);
        drawerLayout.addDrawerListener(drawerToggle);

        drawerToggle.setDrawerIndicatorEnabled(true);
        drawerToggle.syncState();
    }

}