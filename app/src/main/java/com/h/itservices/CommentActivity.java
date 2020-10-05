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

import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

public class CommentActivity extends AppCompatActivity {

    EditText addcomment;
    TextView post;

    String postid;
    String publisherid;
    FirebaseUser firebaseUser;
    private FirebaseAuth myAccount ;
    private String saveCurrentDate, saveCurrentTime  ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comment);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

// to undo to previose
        getSupportActionBar().setTitle("أضف إجابة ");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


        addcomment=findViewById(R.id.add_comment);
        post=findViewById(R.id.post);


        Intent intent= getIntent();
        //postid=intent.getStringExtra("postid");
        //publisherid=intent.getStringExtra("publisherid");

        post.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (addcomment.getText().toString().equals("")){
                    Toast.makeText(CommentActivity.this,"You can't send empty answer",Toast.LENGTH_SHORT).show();
                }else {
                    addComment();
                }
            }
        });


    }//onCreate

    private void addComment(){
        // هنا المفروض يصير عندي اجابات تحتها اسم البوست تحته answe, publisher
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Answers");
        //    DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Comment").child(postId);

        myAccount = FirebaseAuth.getInstance();

        Calendar calFordDate = Calendar.getInstance();
        SimpleDateFormat currentDate = new SimpleDateFormat("dd-MMMM-yyyy");
        saveCurrentDate = currentDate.format(calFordDate.getTime());

        Calendar calFordTime = Calendar.getInstance();
        SimpleDateFormat currentTime = new SimpleDateFormat("HH:mm");
        saveCurrentTime = currentTime.format(calFordDate.getTime());

        HashMap<String,Object> hashMap = new HashMap<>();
        hashMap.put("answer",addcomment.getText().toString());
        hashMap.put("publisher",myAccount.getUid());
        hashMap.put("Date",saveCurrentDate);
        hashMap.put("Time",saveCurrentTime);

        reference.push().setValue(hashMap);

        addcomment.setText("");
    }
}