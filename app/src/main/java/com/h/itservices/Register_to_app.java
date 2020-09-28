package com.h.itservices;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatSpinner;
import androidx.appcompat.widget.Toolbar;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Register_to_app extends AppCompatActivity implements View.OnClickListener {

    public static String NAME,EMAIL, PASS;
    EditText Name;
    private RadioGroup radioGroupType;
    String typ="0";
    CheckBox prog1,prog2,prog3,prog4,prog5;
    EditText other;
    EditText password;
    EditText confirmPassword;
    EditText email;
    Button register;
    Module m;
    static String id;
    private ProgressDialog progressDialog ;
    private FirebaseAuth f1 ;

    //for move to login page
    private TextView LginTextView;


    private FirebaseDatabase database;
    private  DatabaseReference myRef ;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        f1 = FirebaseAuth.getInstance();
        progressDialog = new ProgressDialog(this);
        database = FirebaseDatabase.getInstance();

        myRef = database.getReference();


        //Toolbar toolbar = findViewById(R.id.toolbar2);
        //setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        //setTitle(R.string.createACC);

       // toolbar.setTitleTextColor(Color.WHITE);
        other = findViewById(R.id.other);
        prog1 = findViewById(R.id.prog1);
        prog2 = findViewById(R.id.prog2);
        prog3 = findViewById(R.id.prog3);
        prog4 = findViewById(R.id.prog4);
        prog5 = findViewById(R.id.prog5);

        //for move to login page
        LginTextView = findViewById(R.id.loginTextView);
        LginTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent loginIntent = new Intent(getApplicationContext(), LoginActivity.class);
                startActivity(loginIntent);
            }
        });

        Name = findViewById(R.id.editText2);
        password = findViewById(R.id.editText4);
        confirmPassword = findViewById(R.id.editText7);
        email = findViewById(R.id.editText5);
        radioGroupType = findViewById(R.id.radioGroupType);

        register = findViewById(R.id.button3);

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (checkDataEntered()){

                    id = f1.getUid();
                }}
        });

    }

    boolean isEmail(EditText text) {
        CharSequence email = text.getText().toString();
        return (!TextUtils.isEmpty(email) && Patterns.EMAIL_ADDRESS.matcher(email).matches());
    }

    boolean isEmpty(EditText text) {
        CharSequence str = text.getText().toString();
        return TextUtils.isEmpty(str);
    }

    public void onClickedUser(View view) {
        typ="user";
        prog1.setEnabled(false);
        prog2.setEnabled(false);
        prog3.setEnabled(false);
        prog4.setEnabled(false);
        prog5.setEnabled(false);
        other.setText("");
        other.setEnabled(false);


    }
    public void onClickedExp(View view) {
        typ="exp";
        prog1.setEnabled(true);
        prog2.setEnabled(true);
        prog3.setEnabled(true);
        prog4.setEnabled(true);
        prog5.setEnabled(true);

        other.setEnabled(true);

    }

    boolean checkDataEntered() {
        NAME  = Name.getText().toString().trim();
        EMAIL  = email.getText().toString().trim();
        PASS = password.getText().toString().trim();

        if (isEmpty((EditText)Name )&&(isEmpty(password)) &&(isEmpty(email))) {
            Name.setError("يجب إدخال الاسم!");
            password.setError("كلمة المرور مطلوبة!");
            email.setError("أدخل بريدًا إلكترونيًا صالحًا!");
            return false;}

        if (isEmpty(Name)) {
            Name.setError( "يجب عليك إدخال الاسم للتسجيل!");
            return false;
        }



        if (isEmpty(password)) {
            password.setError("كلمة المرور مطلوبة!");
            return false;
        }

        if( password.getText().toString().length()<=6){
            password.setError("من فضلك كلمة المرور الخاصة بك تحتاج إلى 6 أحرف أو أكثر ");
            return false;
        }



        if (isEmpty(email)) {
            email.setError("البريد الالكتروني مطلوب!");
            return false;
        }

        if (isEmpty(confirmPassword)) {
            confirmPassword.setError("تأكيد كلمة المرور مطلوب!");
            return false;
        }

        if (!isEmail(email)||(!email.getText().toString().substring(email.getText().toString().indexOf(".")+1).equals("com"))){
            email.setError("أدخل بريدًا إلكترونيًا صالحًا!");
            return false;
        }

        if (! password.getText().toString().matches(confirmPassword.getText().toString())) {
            confirmPassword.setError("كلمة المرور غير متطابقة ، يرجى المحاولة مرة أخرى");
            return false;
        }


        progressDialog.setMessage("الرجاء الانتظار");
        progressDialog.show();

        f1.createUserWithEmailAndPassword(EMAIL,PASS)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {

                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        progressDialog.dismiss();
                        if(task.isSuccessful()){

                            String uid = f1.getCurrentUser().getUid();
                            String id = myRef.push().getKey();

                            if (typ.equals("user")) {
                                m =new Module(id,Name.getText().toString(),password.getText().toString(),email.getText().toString(),typ," "," ");
                                myRef.child("allusers").child("User").child(uid).setValue(m);

                            }else {
                                String exx="";
                                if (prog1.isChecked()){exx=exx+""+prog1.getText().toString();}
                                if (prog2.isChecked()){exx=exx+","+prog2.getText().toString();}
                                if (prog3.isChecked()){exx=exx+","+prog3.getText().toString();}
                                if (prog4.isChecked()){exx=exx+","+prog4.getText().toString();}
                                if (prog5.isChecked()){exx=exx+","+prog5.getText().toString();}
                                m =new Module(id,Name.getText().toString(),password.getText().toString(),email.getText().toString(),typ,exx,other.getText().toString());
                                myRef.child("allusers").child("Expert").child(uid).setValue(m);
                            }


                            Toast.makeText(Register_to_app.this, "تم التسجيل بنجاح", Toast.LENGTH_SHORT).show();

                         Intent intent = new Intent(Register_to_app.this,MainActivity.class);
                         startActivity(intent);
                         finish();
                        }
                        else {
                            Toast.makeText(Register_to_app.this, "تعذر التسجيل ، يرجى المحاولة مرة أخرى", Toast.LENGTH_SHORT).show();

                        }
                    }
                });
        // if(ag)
        return true;
    }//data checked


    @Override
    public void onClick(View view) {

    }

    // public void onClick(View view) {

    //}


}