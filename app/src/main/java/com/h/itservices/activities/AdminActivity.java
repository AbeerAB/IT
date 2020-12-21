package com.h.itservices.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.google.firebase.auth.FirebaseAuth;
import com.h.itservices.R;

public class AdminActivity extends AppCompatActivity {

    CardView users,logout,questions;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);
        getSupportActionBar().setTitle("ادارة الاسئلة");

        users=findViewById(R.id.users);
        logout=findViewById(R.id.logout);
        questions=findViewById(R.id.questions);



        users.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(AdminActivity.this,UsersActivity.class));
            }
        });
        questions.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(AdminActivity.this,AdminQuestions.class));
            }
        });


        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth.getInstance().signOut();
                Intent logout = new Intent(AdminActivity.this, LoginActivity.class);
                startActivity(logout);

            }
        });
    }
}
