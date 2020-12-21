package com.h.itservices.activities;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
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

public class LoginActivity extends AppCompatActivity  {

    private TextView registerTextView, forgetPassTextView;
    private EditText emailEditText, passwordEditText;
    private ImageView logoImageView;
    private Button loginButton;
    private String email, password;
    private static final String TAG = "LoginActivity";
    private FirebaseAuth mAuth;

    ProgressDialog progressDialog;

    public static String MY_PREFS_NAME = "itservice";
    public static SharedPreferences.Editor editor;
    public SharedPreferences prefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mAuth = FirebaseAuth.getInstance();

        prefs = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
        editor = prefs.edit();


        registerTextView = findViewById(R.id.register_textview);
        forgetPassTextView = findViewById(R.id.forget_password_textview);
        emailEditText = findViewById(R.id.emailogin_edittext);
        passwordEditText = findViewById(R.id.password_edittext);
        logoImageView = findViewById(R.id.imageView);
        loginButton = findViewById(R.id.button);


        progressDialog=new ProgressDialog(this);
        progressDialog.setMessage(getResources().getString(R.string.msg_login));
        progressDialog.setCancelable(false);


        //checking if user is logged in








        //move to Register page
        registerTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent registerIntent = new Intent(getApplicationContext(), RegisterNewUser.class);
                startActivity(registerIntent);
            }
        });

        //if one of field is empty
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                checkInputIsEmpty(emailEditText,passwordEditText);
                email = emailEditText.getText().toString();
                password = passwordEditText.getText().toString();



                if(!TextUtils.isEmpty(email)&&!TextUtils.isEmpty(password)){
                    progressDialog.show();

                    mAuth.signInWithEmailAndPassword(email, password)
                            .addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {

                                    if (task.isSuccessful()) {

                                        FirebaseUser user = mAuth.getCurrentUser();

                                        getUserData();
                                    } else {
                                        progressDialog.dismiss();

                                        Toast.makeText(getApplicationContext(), getResources().getString(R.string.msg_valid_email_and_password),
                                                Toast.LENGTH_LONG).show();
                                    }

                                    // ...
                                }
                            });

                }



            }
        });



        //move to register page
        registerTextView.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent registerIntent = new Intent(getApplicationContext(), RegisterNewUser.class);
                startActivity(registerIntent);
            }
        });




        //start forget password
        forgetPassTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final EditText resetMail = new EditText(v.getContext());
                final AlertDialog.Builder passwordResetDialog = new AlertDialog.Builder(v.getContext());
                passwordResetDialog.setTitle("Reset Password ?");
                passwordResetDialog.setMessage("Enter Your Email To Received Reset Link.");
                passwordResetDialog.setView(resetMail);

                passwordResetDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // extract the email and send reset link
                        String mail = resetMail.getText().toString();
                        mAuth.sendPasswordResetEmail(mail).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Toast.makeText(LoginActivity.this, "Reset Link Sent To Your Email.", Toast.LENGTH_SHORT).show();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(LoginActivity.this, "Error ! Reset Link is Not Sent" + e.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });

                    }
                });

                passwordResetDialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // close the dialog
                    }
                });

                passwordResetDialog.create().show();

            }
        });
// end forget password




    }// on create method


    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            updateUI();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            updateUI();
        }
    }

    // move to main page
    public void updateUI() {

        Intent profileIntent = new Intent(getApplicationContext(), MainActivity.class);
//        profileIntent.putExtra("email", currentUser.getEmail());
//        Log.v("DATA", currentUser.getUid());
        startActivity(profileIntent);
    }

public void checkInputIsEmpty(EditText email,EditText password){

    if(TextUtils.isEmpty(email.getText().toString())){
        email.setError(getResources().getString(R.string.msg_email_is_empty));
    }

    if(TextUtils.isEmpty(password.getText().toString())){
        password.setError(getResources().getString(R.string.msg_password_is_empty));

    }
//    Toast.makeText(getApplicationContext(),"Enter email and password", Toast.LENGTH_LONG).show();




    }

    public void getUserData(){

        DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference();
        DatabaseReference usersRef = rootRef.child("User").child(FirebaseAuth.getInstance().getCurrentUser().getUid());


        ValueEventListener valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                String username= (String) dataSnapshot.child("user_name").getValue();
                String user_type= (String) dataSnapshot.child("type").getValue();
                String profile_image_name= (String) dataSnapshot.child("profile_image_name").getValue();
                editor.putString("username",username);
                editor.putString("user_type",user_type);
                editor.putString("profile_image_name",profile_image_name);
                editor.commit();

                if(user_type.equalsIgnoreCase("exp")){
                    ArrayList<String> exp= ( ArrayList<String> ) dataSnapshot.child("exp").getValue();
                    Set<String> set = new HashSet<String>();
                    set.addAll(exp);
                    editor.putStringSet("user_exp", set);
                }

                editor.commit();

                Intent listQuestions = new Intent(LoginActivity.this, MainActivity.class);
                startActivity(listQuestions);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(LoginActivity.this, "مشكلة في الانترنت حاول مرة اخرى", Toast.LENGTH_SHORT).show();
            }
        };
        usersRef.addListenerForSingleValueEvent(valueEventListener);
    }


}