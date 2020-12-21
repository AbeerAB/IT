package com.h.itservices.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.h.itservices.R;
import com.h.itservices.model.User;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class SplashActivity extends AppCompatActivity {


    public static String MY_PREFS_NAME = "itservice";
    public static SharedPreferences.Editor editor;

    private FirebaseAuth mAuth;
    public SharedPreferences prefs;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        prefs = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
        editor = prefs.edit();
        mAuth = FirebaseAuth.getInstance();


        if (mAuth.getCurrentUser() ==null) {
            Intent profileIntent = new Intent(getApplicationContext(), LoginActivity.class);
            startActivity(profileIntent);
            finish();


        }else {

            getUserData();
        }


    }
    public void getUserData(){

        DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference();
        DatabaseReference usersRef = rootRef.child("User").child(FirebaseAuth.getInstance().getCurrentUser().getUid());


        ValueEventListener valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                String username= (String) dataSnapshot.child("user_name").getValue();
                String user_type= (String) dataSnapshot.child("type").getValue();
                Log.d("user_type",username);
                Log.d("user_type",user_type);
                editor.putString("username",username);
                editor.putString("user_type",user_type);

                if(user_type.equalsIgnoreCase("exp")){
                    ArrayList<String> exp= ( ArrayList<String> ) dataSnapshot.child("exp").getValue();

                    Set<String> set = new HashSet<String>();
                    set.addAll(exp);
                    editor.putStringSet("user_exp", set);
                }

                editor.commit();

                Intent listQuestions = new Intent(SplashActivity.this, MainActivity.class);
                startActivity(listQuestions);
                finish();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(SplashActivity.this, "مشكلة في الانترنت حاول مرة اخرى", Toast.LENGTH_SHORT).show();
            }
        };
        usersRef.addListenerForSingleValueEvent(valueEventListener);
    }



}