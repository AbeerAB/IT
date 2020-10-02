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
    CheckBox prog11,prog12,prog13,prog14,prog15,prog16,prog17;
    CheckBox prog21,prog22,prog23,prog24,prog25;
    CheckBox prog1,prog2,prog3,prog4,prog5,prog6,prog7;
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

        prog11 = findViewById(R.id.prog11);
        prog12 = findViewById(R.id.prog12);
        prog13 = findViewById(R.id.prog13);
        prog14 = findViewById(R.id.prog14);
        prog15 = findViewById(R.id.prog15);
        prog16 = findViewById(R.id.prog16);
        prog17 = findViewById(R.id.prog17);

        prog21 = findViewById(R.id.prog21);
        prog22 = findViewById(R.id.prog22);
        prog23 = findViewById(R.id.prog23);
        prog24 = findViewById(R.id.prog24);
        prog25 = findViewById(R.id.prog25);

        prog1 = findViewById(R.id.prog1);
        prog2 = findViewById(R.id.prog2);
        prog3 = findViewById(R.id.prog3);
        prog4 = findViewById(R.id.prog4);
        prog5 = findViewById(R.id.prog5);
        prog6 = findViewById(R.id.prog6);
        prog7 = findViewById(R.id.prog7);

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

        prog11.setEnabled(false);
        prog12.setEnabled(false);
        prog13.setEnabled(false);
        prog14.setEnabled(false);
        prog15.setEnabled(false);
        prog16.setEnabled(false);
        prog17.setEnabled(false);

        prog21.setEnabled(false);
        prog22.setEnabled(false);
        prog23.setEnabled(false);
        prog24.setEnabled(false);
        prog25.setEnabled(false);

        prog1.setEnabled(false);
        prog2.setEnabled(false);
        prog3.setEnabled(false);
        prog4.setEnabled(false);
        prog5.setEnabled(false);
        prog6.setEnabled(false);
        prog7.setEnabled(false);
        other.setText("");
        other.setEnabled(false);
    }
    public void onClickedExp(View view) {
        typ="exp";
        prog11.setEnabled(true);
        prog12.setEnabled(true);
        prog13.setEnabled(true);
        prog14.setEnabled(true);
        prog15.setEnabled(true);
        prog16.setEnabled(true);
        prog17.setEnabled(true);

        prog21.setEnabled(true);
        prog22.setEnabled(true);
        prog23.setEnabled(true);
        prog24.setEnabled(true);
        prog25.setEnabled(true);

        prog1.setEnabled(true);
        prog2.setEnabled(true);
        prog3.setEnabled(true);
        prog4.setEnabled(true);
        prog5.setEnabled(true);
        prog6.setEnabled(true);
        prog7.setEnabled(true);

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
                                m =new Module(id,Name.getText().toString(),password.getText().toString(),email.getText().toString(),typ,"","");
                                myRef.child("allusers").child("User").child(uid).setValue(m);

                            }else {
                                String exp="";
                                if (prog11.isChecked()){exp=exp+""+prog11.getText().toString();}
                                if (prog12.isChecked()){exp=exp+","+prog12.getText().toString();}
                                if (prog13.isChecked()){exp=exp+","+prog13.getText().toString();}
                                if (prog14.isChecked()){exp=exp+","+prog14.getText().toString();}
                                if (prog15.isChecked()){exp=exp+","+prog15.getText().toString();}
                                if (prog16.isChecked()){exp=exp+","+prog16.getText().toString();}
                                if (prog17.isChecked()){exp=exp+","+prog17.getText().toString();}

                                if (prog21.isChecked()){exp=exp+""+prog21.getText().toString();}
                                if (prog22.isChecked()){exp=exp+","+prog22.getText().toString();}
                                if (prog23.isChecked()){exp=exp+","+prog23.getText().toString();}
                                if (prog24.isChecked()){exp=exp+","+prog24.getText().toString();}
                                if (prog25.isChecked()){exp=exp+","+prog25.getText().toString();}

                                if (prog1.isChecked()){exp=exp+""+prog1.getText().toString();}
                                if (prog2.isChecked()){exp=exp+","+prog2.getText().toString();}
                                if (prog3.isChecked()){exp=exp+","+prog3.getText().toString();}
                                if (prog4.isChecked()){exp=exp+","+prog4.getText().toString();}
                                if (prog5.isChecked()){exp=exp+","+prog5.getText().toString();}
                                if (prog6.isChecked()){exp=exp+","+prog6.getText().toString();}
                                if (prog7.isChecked()){exp=exp+","+prog7.getText().toString();}
                                m =new Module(id,Name.getText().toString(),password.getText().toString(),email.getText().toString(),typ,exp,other.getText().toString());
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