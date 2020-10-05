package com.h.itservices;

import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

public class Add_Question_Activity extends AppCompatActivity {

    EditText question_title,question_text;
    Button post_btn,cancel_btn;

    String postid;
    String publisherid;
    FirebaseUser firebaseUser;
    private FirebaseAuth myAccount ;
    private String saveCurrentDate, saveCurrentTime  ;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add__question_);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


// to undo to previose
        getSupportActionBar().setTitle("إسأل");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


        question_title=findViewById(R.id.question_title);
        question_text=findViewById(R.id.question_text);
        post_btn=findViewById(R.id.post_btn);
        cancel_btn=findViewById(R.id.cancel_btn);

        Intent intent= getIntent();
        //postid=intent.getStringExtra("postid");
        //publisherid=intent.getStringExtra("publisherid");




    /*    post_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (question_title.getText().toString().equals("") || !question_title.getText().toString().trim().isEmpty() ){
                    Toast.makeText(Add_Question_Activity.this,"أدخل عنوان للسؤال",Toast.LENGTH_SHORT).show();


                }else  if (question_text.getText().toString().equals("") || !question_text.getText().toString().trim().isEmpty() ){
                    Toast.makeText(Add_Question_Activity.this,"أدخل محتوى السؤال",Toast.LENGTH_SHORT).show();

                }else{  startPosting();}
        }
        });
        */

        post_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startPosting();}

        });


        cancel_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }// on Create







        private void startPosting(){


        final String title_val = question_title.getText().toString().trim();
        final String description_val = question_text.getText().toString().trim();




            if(!TextUtils.isEmpty(title_val) && !TextUtils.isEmpty(description_val) ){

                // هنا المفروض يصير عندي أسئلة تحتها  answe, publisher
                DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Questions");
                //    DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Comment").child(postId);

                myAccount = FirebaseAuth.getInstance();

                Calendar calFordDate = Calendar.getInstance();
                SimpleDateFormat currentDate = new SimpleDateFormat("dd-MMMM-yyyy");
                saveCurrentDate = currentDate.format(calFordDate.getTime());

                Calendar calFordTime = Calendar.getInstance();
                SimpleDateFormat currentTime = new SimpleDateFormat("HH:mm");
                saveCurrentTime = currentTime.format(calFordDate.getTime());

                String postKey= saveCurrentDate+saveCurrentTime;

                HashMap<String,Object> hashMap = new HashMap<>();
                hashMap.put("question title",question_title.getText().toString());
                hashMap.put("question text",question_text.getText().toString());
                hashMap.put("Date",saveCurrentDate);
                hashMap.put("Time",saveCurrentTime);
                hashMap.put("publisher",myAccount.getUid());
                hashMap.put("postKey",postKey.toString());
                reference.push().setValue(hashMap);

                question_title.setText("");
                question_text.setText("");



                startActivity(new Intent(Add_Question_Activity.this, MainActivity.class));


                    }







    }



    private void checkEnteredData() {

        if (isEmpty(question_title)) {
            question_title.setError("عنوان الكتاب مطلوب!");
        }

        if (isEmpty(question_text)) {
            question_text.setError("محتوى السؤال مطلوب!");
        }

    }


    boolean isEmpty(EditText text) {
        CharSequence str = text.getText().toString();
        return TextUtils.isEmpty(str);
    }


}