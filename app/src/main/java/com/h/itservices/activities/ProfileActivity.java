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
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.h.itservices.R;
import com.h.itservices.model.User;
import com.intentfilter.androidpermissions.PermissionManager;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import static java.util.Collections.singleton;

public class ProfileActivity extends AppCompatActivity implements View.OnClickListener {

    public static String NAME,EMAIL, PASS;
    EditText Name;
    private RadioGroup radioGroupType;
    public static String type="user";
    CheckBox prog11,prog12,prog13,prog14,prog15,prog16,prog17;
    CheckBox prog21,prog22,prog23,prog24,prog25;
    CheckBox prog1,prog2,prog3,prog4,prog5,prog6,prog7;
    EditText password;
    EditText confirmPassword;
    EditText email;
    Button register;
    RadioButton user_ridio_button,expuser_ridio_button;
    User m;
    static String id;
    private ProgressDialog progressDialog ;
    private FirebaseAuth f1 ;
    private StorageReference mStorage;
    //for move to login page
    private TextView LginTextView;
    TextView chose_imag;
    private Uri  image_uri;


    public static String old_password;
    private FirebaseDatabase database;
    private  DatabaseReference myRef ;

    LinearLayout back;
    ImageView user_img;
    StorageReference filepath;

    ArrayList<String>exp;

    ProgressBar progressBar;

    ScrollView   scrol_data;

    LinearLayout linear_exp;
    String profile_image_name="";
    public static String MY_PREFS_NAME = "itservice";
    public static SharedPreferences.Editor editor;
    public SharedPreferences prefs;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
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

        user_ridio_button = findViewById(R.id.user);
        expuser_ridio_button = findViewById(R.id.expuser);
        chose_imag = findViewById(R.id.chose_imag);
        back = findViewById(R.id.back);
        progressBar = findViewById(R.id.progressBar);
        scrol_data = findViewById(R.id.scrol_data);
        linear_exp = findViewById(R.id.linear_exp);

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
                PermissionManager permissionManager = PermissionManager.getInstance(ProfileActivity.this);
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
        getUserData();

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
        type="user";

        exp.clear();
        linear_exp.setVisibility(View.GONE);
    }
    public void onClickedExp(View view) {
        type="exp";
        linear_exp.setVisibility(View.VISIBLE);
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


        if(user_ridio_button.isChecked()==false && expuser_ridio_button.isChecked()==false){
            Toast.makeText(this, "يجب عليك إختيار نوع الحساب", Toast.LENGTH_SHORT).show();
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


        progressDialog.setMessage("الرجاء الانتظار");
        progressDialog.show();


            String uid = f1.getCurrentUser().getUid();
            String id = myRef.push().getKey();
//                            String exp="";

            if (type.equals("exp")) {
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

        }

        if (exp.size()==0&& type.equalsIgnoreCase("exp")) {
            progressDialog.dismiss();
            Toast.makeText(this, "يجب اختيار مجال", Toast.LENGTH_SHORT).show();

            return false;
        }

        if(image_uri!=null){

            profile_image_name = "profile_" + (int) (new Date().getTime() / 1000);
            upload_profile_images(profile_image_name);

        }else {
//            profile_image_name="";
        }

        m =new User(id,Name.getText().toString(),password.getText().toString(),
                email.getText().toString(),type,exp,profile_image_name);
        final AuthCredential credential = EmailAuthProvider
                .getCredential(email.getText().toString(),old_password);

        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        myRef.child("User").child(uid).setValue(m).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {

                progressDialog.dismiss();
                editor.putString("username",Name.getText().toString());
                editor.putString("user_type",type);
                editor.putString("profile_image_name",profile_image_name);
                Set<String> set = new HashSet<String>();
                set.addAll(exp);
                editor.putStringSet("user_exp", set);
                editor.commit();
                Toast.makeText(ProfileActivity.this, "تم التعديل بنجاح", Toast.LENGTH_SHORT).show();



                user.reauthenticate(credential)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    user.updatePassword(password.getText().toString()).addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()) {
                                            } else {
                                            }
                                        }
                                    });
                                } else {
                                }
                            }
                        });
                Intent intent = new Intent(ProfileActivity.this, MainActivity.class);
                startActivity(intent);
                finish();

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                progressDialog.dismiss();

                Toast.makeText(ProfileActivity.this, e.getMessage()+"", Toast.LENGTH_SHORT).show();

            }
        });


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

    public void getUserData(){
        progressBar.setVisibility(View.VISIBLE);
        scrol_data.setVisibility(View.GONE);

        DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference();
        DatabaseReference usersRef = rootRef.child("User").child(FirebaseAuth.getInstance().getCurrentUser().getUid());


        ValueEventListener valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
//                User user = dataSnapshot.getValue(User.class);
                String user_name = (String) dataSnapshot.child("user_name").getValue();
                String user_email = (String) dataSnapshot.child("email").getValue();
                String user_type = (String) dataSnapshot.child("type").getValue();
                String user_id = (String) dataSnapshot.child("user_id").getValue();
                String profile_image_name = (String) dataSnapshot.child("profile_image_name").getValue();
                String user_password = (String) dataSnapshot.child("password").getValue();
                type=user_type;
                progressBar.setVisibility(View.GONE);
                scrol_data.setVisibility(View.VISIBLE);
                Name.setText(user_name);
                email.setText(user_email);
                password.setText(user_password);
                confirmPassword.setText(user_password);
                old_password=user_password;
                profile_image_name=profile_image_name;
                ArrayList<String>exp=new ArrayList<>();


                if(user_type.equalsIgnoreCase("exp")){


                    exp = (ArrayList<String>) dataSnapshot.child("exp").getValue();

                    type="exp";

                    linear_exp.setVisibility(View.VISIBLE);
                    expuser_ridio_button.setChecked(true);

                    user_ridio_button.setChecked(false);


                    for(int i=0;i<exp.size();i++){

                        if(exp.get(i).equalsIgnoreCase("Matlab")){
                            prog11.setChecked(true);
                        }if(exp.get(i).equalsIgnoreCase("SPSS")){
                            prog12.setChecked(true);
                        }if(exp.get(i).equalsIgnoreCase("EndNote")){
                            prog13.setChecked(true);
                        }if(exp.get(i).equalsIgnoreCase("Oracle")){
                            prog14.setChecked(true);
                        }if(exp.get(i).equalsIgnoreCase("Latex")){
                            prog15.setChecked(true);
                        }if(exp.get(i).equalsIgnoreCase("Microsoft Office")){
                            prog16.setChecked(true);
                        }if(exp.get(i).equalsIgnoreCase("Photoshop and illustrator")){
                            prog17.setChecked(true);
                        }if(exp.get(i).equalsIgnoreCase("Windows")){
                            prog21.setChecked(true);
                        }if(exp.get(i).equalsIgnoreCase("Linux")){
                            prog22.setChecked(true);
                        }if(exp.get(i).equalsIgnoreCase("MAC")){
                            prog23.setChecked(true);
                        }if(exp.get(i).equalsIgnoreCase("IOS")){
                            prog24.setChecked(true);
                        }if(exp.get(i).equalsIgnoreCase("Android")){
                            prog25.setChecked(true);
                        }if(exp.get(i).equalsIgnoreCase("Java")){
                            prog1.setChecked(true);
                        }if(exp.get(i).equalsIgnoreCase("C")){
                            prog2.setChecked(true);
                        }if(exp.get(i).equalsIgnoreCase("C+")){
                            prog3.setChecked(true);
                        }if(exp.get(i).equalsIgnoreCase("C++")){
                            prog4.setChecked(true);
                        }if(exp.get(i).equalsIgnoreCase("Script Python")){
                            prog5.setChecked(true);
                        }if(exp.get(i).equalsIgnoreCase("Visual Basic.Net Java")){
                            prog6.setChecked(true);
                        }if(exp.get(i).equalsIgnoreCase("R Swift SQL")){
                            prog7.setChecked(true);
                        }


                    }

                }else {
                    type="user";
                    user_ridio_button.setChecked(true);

                    expuser_ridio_button.setChecked(false);

                    linear_exp.setVisibility(View.GONE);

                }


                getImage(profile_image_name);




                //Do what you need to do with your list
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        };
        usersRef.addListenerForSingleValueEvent(valueEventListener);
    }

    public void getImage(final String image_name){
        StorageReference mStorage = FirebaseStorage.getInstance().getReference();


        mStorage.child("Profile_images/" + image_name)
                .getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Picasso.get()
                        .load(uri)
                        .placeholder(R.drawable.user_empty)
                        .error(R.drawable.user_empty)
                        .into(user_img);



            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {

            }
        });
    }

}