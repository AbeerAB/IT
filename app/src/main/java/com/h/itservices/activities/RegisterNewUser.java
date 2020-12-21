package com.h.itservices.activities;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.h.itservices.model.User;
import com.h.itservices.R;
import com.intentfilter.androidpermissions.PermissionManager;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import static java.util.Collections.singleton;

public class RegisterNewUser extends AppCompatActivity implements View.OnClickListener {

    public static String NAME,EMAIL, PASS;
    EditText Name;
    private RadioGroup radioGroupType;
    String typ="user";
    CheckBox prog11,prog12,prog13,prog14,prog15,prog16,prog17;
    CheckBox prog21,prog22,prog23,prog24,prog25;
    CheckBox prog1,prog2,prog3,prog4,prog5,prog6,prog7;
    EditText password;
    EditText confirmPassword;
    EditText email;
    Button register;
    RadioButton user,expuser;
    User m;
    static String id;
    private ProgressDialog progressDialog ;
    private FirebaseAuth f1 ;
    private StorageReference mStorage;
    //for move to login page
    private TextView LginTextView;
    TextView chose_imag;
    private Uri  image_uri;


    private FirebaseDatabase database;
    private  DatabaseReference myRef ;

    LinearLayout back;
    ImageView user_img;
    StorageReference filepath;

    ArrayList<String>exp;

    public static String MY_PREFS_NAME = "itservice";
    public static SharedPreferences.Editor editor;
    public SharedPreferences prefs;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        f1 = FirebaseAuth.getInstance();
        progressDialog = new ProgressDialog(this);
        database = FirebaseDatabase.getInstance();

        myRef = database.getReference();

        exp=new ArrayList<>();

        prefs = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
        editor = prefs.edit();

//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//        getSupportActionBar().setDisplayShowHomeEnabled(true);

        setTitle(R.string.title_activity_RegisterNewUser);

        user = findViewById(R.id.user);
        expuser = findViewById(R.id.expuser);
        chose_imag = findViewById(R.id.chose_imag);
        back = findViewById(R.id.back);

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

           Name = findViewById(R.id.user_name);
        password = findViewById(R.id.user_pass);
        confirmPassword = findViewById(R.id.conferm_password);
        email = findViewById(R.id.email);
        radioGroupType = findViewById(R.id.radioGroupType);
        user_img = findViewById(R.id.user_img);

        register = findViewById(R.id.button3);

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (checkDataEntered()){

                    id = f1.getUid();
                }}
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        user_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PermissionManager permissionManager = PermissionManager.getInstance(RegisterNewUser.this);
                permissionManager.checkPermissions(singleton(Manifest.permission.READ_EXTERNAL_STORAGE), new PermissionManager.PermissionRequestListener() {
                    @Override
                    public void onPermissionGranted() {

                        Intent gallery = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);
                        startActivityForResult(gallery, 300);
                    }

                    @Override
                    public void onPermissionDenied() {
//                        Toast.makeText(getActivity(), "P/ermissions Denied", Toast.LENGTH_SHORT).show();
                    }
                });

            }
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
        exp.clear();

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


        if(user.isChecked()==false && expuser.isChecked()==false){
            Toast.makeText(this, "يجب عليك إختيار نوع الحساب", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (isEmpty(password)) {
            password.setError("كلمة المرور مطلوبة!");
            return false;
        }

        if( password.getText().toString().length()<=6){
            password.setError("من فضلك كلمة المرور الخاصة بك تحتاج إلى 6 أحرف أو أكثر");
            return false;
        }
        //new
        if (password.length() >15 ){
            password.setError("من فضلك كلمة المرور الخاصة بك تحتاج إلى 15أحرف أو أقل");
            return false;
        }

        if (!password.getText().toString().matches(".*\\d.*")){
            password.setError("يجب أن تحتوي كلمة المرور الخاصة بك عل أرقام");
            return false;
        }

        if (!password.getText().toString().matches(".*[a-z].*")) {
            password.setError("يجب أن تحتوي كلمة المرور الخاصة بك على أحرف صغيرة ");
            return false;
        }
        if (!password.getText().toString().matches(".*[A-Z].*")) {
            password.setError("يجب أن تحتوي كلمة المرور الخاصة بك على أحرف كبيرة ");
            return false;
        }
        if (!password.getText().toString().matches(".*[!@#$%^&*+=?-].*")) {
            password.setError("يجب أن تحتوي كلمة المرور الخاصة بك على رموز");
            return false;
        }
        //end new


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


        progressDialog.setMessage("الرجاء الإنتظار");
        progressDialog.show();

        if (typ.equals("exp")) {
            exp.clear();

            if (prog1.isChecked()){
                exp.add(prog1.getText().toString());
            }
            if (prog2.isChecked()){
                exp.add(prog2.getText().toString());
            }
            if (prog3.isChecked()){
                exp.add(prog3.getText().toString());
            }
            if (prog4.isChecked()){
                exp.add(prog4.getText().toString());
            }

            if (prog5.isChecked()){
                exp.add(prog5.getText().toString());
            }
            if (prog6.isChecked()){
                exp.add(prog6.getText().toString());
            }

            if (prog7.isChecked()){
                exp.add(prog7.getText().toString());
            }

            if (prog11.isChecked()){
                exp.add(prog11.getText().toString());
            }
            if (prog12.isChecked()){
                exp.add(prog12.getText().toString());
            }
            if (prog13.isChecked()){
                exp.add(prog13.getText().toString());
            }
            if (prog14.isChecked()){
                exp.add(prog14.getText().toString());
            }

            if (prog15.isChecked()){
                exp.add(prog15.getText().toString());
            }

            if (prog16.isChecked()){
                exp.add(prog16.getText().toString());
            }
            if (prog17.isChecked()){
                exp.add(prog17.getText().toString());
            }
            if (prog21.isChecked()){
                exp.add(prog21.getText().toString());
            }
            if (prog22.isChecked()){
                exp.add(prog22.getText().toString());
            }

            if (prog23.isChecked()){
                exp.add(prog23.getText().toString());
            }
            if (prog24.isChecked()){
                exp.add(prog24.getText().toString());
            }
            if (prog25.isChecked()){
                exp.add(prog25.getText().toString());
            }


        }else {
            exp.clear();
//                                 exp=;

        }

        f1.createUserWithEmailAndPassword(EMAIL,PASS)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {


                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        progressDialog.dismiss();

                        if(task.isSuccessful()){

                            String uid = f1.getCurrentUser().getUid();
                            //String id = myRef.child("User").push().getKey();
                            String profile_image_name="";
//                            String exp="";


                            if(image_uri!=null){
                                profile_image_name = "profile_" + (int) (new Date().getTime() / 1000);
                                upload_profile_images(profile_image_name);

                            }else {
                                profile_image_name="";
                            }




                            if (exp.size()==0&& typ.equalsIgnoreCase("exp")) {
                                progressDialog.dismiss();
                                Toast.makeText(RegisterNewUser.this, "يجب اختيار مجال", Toast.LENGTH_SHORT).show();

                            }else {


                                m =new User(uid,Name.getText().toString(),password.getText().toString(),email.getText().toString(),typ,exp,profile_image_name);


                                myRef.child("User").child(uid).setValue(m);

                                editor.putString("username",Name.getText().toString());
                                editor.putString("user_type",typ);
                                editor.putString("profile_image_name",profile_image_name);
                                editor.commit();

                                if(typ.equalsIgnoreCase("exp")){

                                    Set<String> set = new HashSet<String>();
                                    set.addAll(exp);
                                    editor.putStringSet("user_exp", set);
                                    editor.commit();

                                }

                                Toast.makeText(RegisterNewUser.this, "تم التسجيل بنجاح", Toast.LENGTH_SHORT).show();

                                Intent intent = new Intent(RegisterNewUser.this, MainActivity.class);
                                startActivity(intent);
                                finish();
                            }


                        }
                        else {
                            Toast.makeText(RegisterNewUser.this, task.getException().getMessage()+"", Toast.LENGTH_SHORT).show();

                        }
                    }
                });
        // if(ag)
        return true;
    }//data checked

    @Override
    public void onClick(View view) {

    }
    public void upload_profile_images(String img_titel) {
        mStorage = FirebaseStorage.getInstance().getReference();
        if (chose_imag.getText().toString().equals("تم اختيار الصورة")) {


            filepath = mStorage.child("Profile_images/").child(img_titel);
            filepath.putFile(image_uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {



                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                }
            });
       /* */

        }
    }
//
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == 300 && data != null) {
            image_uri = data.getData();
            user_img.setImageURI(image_uri);
            chose_imag.setText("تم اختيار الصورة");
        }
    }


}