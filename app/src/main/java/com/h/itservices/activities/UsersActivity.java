package com.h.itservices.activities;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.h.itservices.R;
import com.h.itservices.adapter.UsersAdapter;
import com.h.itservices.model.User;


import java.util.ArrayList;

public class UsersActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    ProgressDialog progressDialog;

    ArrayList<User> users;
    UsersAdapter usersAdapter;
    TextView item_ampty;
    RecyclerView recyclerView;
    private DatabaseReference myRef ;
    private FirebaseDatabase database;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.users_activity);


        getSupportActionBar().setTitle("ادارة المستخدمين");
        mAuth = FirebaseAuth.getInstance();
        progressDialog=new ProgressDialog(this);
        progressDialog.setCancelable(false);
        progressDialog.setMessage(getResources().getString(R.string.msg_login));

        users=new ArrayList<>();
        database = FirebaseDatabase.getInstance();
        myRef = database.getReference();

        recyclerView=(RecyclerView)findViewById(R.id.recyclerView);
        item_ampty=(TextView)findViewById(R.id.item_ampty);

        usersAdapter = new UsersAdapter(this, users);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(usersAdapter);

        get_users();
    }
    public void get_users(){
        progressDialog.show();
        DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference().child("User");

        rootRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                users.clear();

                progressDialog.dismiss();

                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    User user=ds.getValue(User.class);
                    user.setUser_id(ds.getKey());
                    if(!user.getEmail().equalsIgnoreCase("admin@gmail.com"))
                    users.add(user);
                }


                if(users.size()==0){
                    item_ampty.setVisibility(View.VISIBLE);

                    Toast.makeText(UsersActivity.this,"لا يوجد مستخدمين", Toast.LENGTH_SHORT).show();
                }else {
                    item_ampty.setVisibility(View.GONE);

                }
                usersAdapter.notifyDataSetChanged();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

}
