package com.h.itservices;

import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

public class CustomerQuestionsActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    DrawerLayout drawerLayout;
    NavigationView navigationView;
    Toolbar toolbar;
    Menu menu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_questions);

        /*---------------------Hooks------------------------*/
        toolbar = findViewById(R.id.toolbar);
        navigationView = findViewById(R.id.nav_view );
        drawerLayout = findViewById(R.id.drawer_layout );
        setSupportActionBar(toolbar);

        /*---------------------Navigation------------------------*/
        navigationView.bringToFront();
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this,drawerLayout,toolbar,R.string.navigation_drawer_open,R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        // to change color when clicK on item
        navigationView.setNavigationItemSelectedListener(this);
        // send this page
        navigationView.setCheckedItem(R.id.nav_CustomerQuestions);


    }//onCreate

    /*---------------------to Open or close Navigation ------------------------*/
    public void onBackPressed(){
        if(drawerLayout.isDrawerOpen(GravityCompat.START)){
            drawerLayout.closeDrawer(GravityCompat.START);
        }
        else
        {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case R.id.nav_home:
                Intent home = new Intent(CustomerQuestionsActivity.this, MyQuestionsActivity.class);
                startActivity(home); break;
            case R.id.nav_profile:
                Intent profile = new Intent(CustomerQuestionsActivity.this, ProfileActivity.class);
                startActivity(profile); break;
            case R.id.nav_MyQuestion:
                Intent MyQuestion = new Intent(CustomerQuestionsActivity.this, MyQuestionsActivity.class);
                startActivity(MyQuestion); break;
            case R.id.nav_ExpAnswer:
                Intent Exp = new Intent(CustomerQuestionsActivity.this, ExpertAnswersActivity.class);
                startActivity(Exp); break;
            case R.id.nav_CustomerQuestions:break;
            case R.id.nav_out:
                Intent logout = new Intent(CustomerQuestionsActivity.this, Register_to_app.class);
                startActivity(logout); break;
        }
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }// End onNavigationItemSelected()
}