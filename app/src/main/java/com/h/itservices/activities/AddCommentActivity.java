package com.h.itservices.activities;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.h.itservices.R;
import com.h.itservices.adapter.AnswersAdapter;
import com.h.itservices.model.Answers;
import com.h.itservices.model.Likes;
import com.h.itservices.model.Matched;

import org.json.JSONObject;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import static android.content.ContentValues.TAG;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class AddCommentActivity extends AppCompatActivity {


    TextView qa_titel,qa_text;
    ProgressDialog progressDialog;

    ArrayList<Answers> answers;
    private FirebaseAuth mAuth;
    AnswersAdapter answersAdapter;
    RecyclerView recycle_view;
    TextView answwer_ampty;
    String question_id,qa_uid,publisher_name;


    public SharedPreferences prefs;


    public static String MY_PREFS_NAME = "itservice";
    public static SharedPreferences.Editor editor;


    private DatabaseReference myRef ;



    private FirebaseDatabase database;
    private Query rootRef;
    public static final String FCM_MESSAGE_URL = "https://fcm.googleapis.com/fcm/send";

    public static MediaType JSON= MediaType.parse("application/json; charset=utf-8");


    OkHttpClient mClient = new OkHttpClient();

    String backType="app_back";
    ArrayList<Likes> likes;
    ArrayList<Matched> experts;
    boolean flag;

    @SuppressLint("RestrictedApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //(1) compare the current user id with the id in the my experts to allow the expert to ansewr
        //(2) for loop to access the list of expert and to get user id
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comment);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        mAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        myRef = database.getReference();
        getSupportActionBar().setTitle("تفاصيل السؤال ");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        prefs = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
        editor = prefs.edit();

        likes=new ArrayList<>();

        progressDialog=new ProgressDialog(AddCommentActivity.this);
        progressDialog.setCancelable(true);
        progressDialog.setMessage("انتظر.....");

        qa_titel=findViewById(R.id.qa_titel);
        qa_text=findViewById(R.id.qa_text);
       answwer_ampty=findViewById(R.id.answwer_ampty);

        answers=new ArrayList<>();


        try {

            qa_titel.setText(getIntent().getStringExtra("qa_titel"));
            qa_text.setText(getIntent().getStringExtra("qa_text"));
            question_id=getIntent().getStringExtra("question_id");
            qa_uid=getIntent().getStringExtra("publisher_uid");
            publisher_name=getIntent().getStringExtra("publisher_name");
        }catch (Exception e){

        }
        try {
            if(getIntent().getStringExtra("type")!=null){
                backType=getIntent().getStringExtra("type");

            }
        }catch (Exception e){

        }

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(backType.equalsIgnoreCase("Question")){
                    startActivity(new Intent(AddCommentActivity.this,MainActivity.class));

                }else {
                    finish();

                }
            }
        });


        final FloatingActionButton fab = findViewById(R.id.fab);
        String currentuser= mAuth.getCurrentUser().getUid();

        rootRef= myRef.child("Question").child(question_id).child("experts");
        experts= new ArrayList<>();
        //to call expert method
        if (prefs.getString("user_type", "").equalsIgnoreCase("user")) {
            fab.setVisibility(View.GONE);
        }
        else {
            check_if_allowed(new FirebaseCallBack() {
                @Override
                public void onCallBack(boolean flag) {
                    if (flag)
                        fab.setVisibility(View.VISIBLE);
                    else
                        fab.setVisibility(View.GONE);

                }
            });
        }
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Dialog dialog = new Dialog(AddCommentActivity.this);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setCancelable(true);
                dialog.setContentView(R.layout.add_comment_dialog);
                final EditText comment_text=dialog.findViewById(R.id.comment);
                Button post=dialog.findViewById(R.id.post);



                post.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        progressDialog.show();

                        final Answers answers=new Answers("0",qa_uid,question_id,FirebaseAuth.getInstance().getCurrentUser().getUid(),prefs.getString("username",""),comment_text.getText().toString(),getDate(),likes);


                        myRef.child("Question").child(question_id).child("answers").push().setValue(answers).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(AddCommentActivity.this, "مشكلة في الأنترنت حاول مرة اخرى!", Toast.LENGTH_SHORT).show();
                                progressDialog.dismiss();
                            }
                        }).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                progressDialog.dismiss();
                                dialog.dismiss();

                                sendCommentNotification(prefs.getString("username",""),qa_titel.getText().toString(),qa_text.getText().toString(),question_id,publisher_name,qa_uid);

                                Toast.makeText(AddCommentActivity.this, "تم إرسال تعليقك بنجاح", Toast.LENGTH_SHORT).show();

                            }
                        });

                    }
                });
                dialog.show();

            }
        });
//fab this will be in check allowed


        recycle_view=(RecyclerView)findViewById(R.id.recycle_view);

        answersAdapter = new AnswersAdapter(AddCommentActivity.this, answers);
        LinearLayoutManager layoutManager = new LinearLayoutManager(AddCommentActivity.this, LinearLayoutManager.VERTICAL, false);
        recycle_view.setLayoutManager(layoutManager);
        recycle_view.setAdapter(answersAdapter);

        get_answers_list();

    }

    public void get_answers_list(){
        progressDialog.show();



        DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference().child("Question").child(question_id).child("answers");

        rootRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                answers.clear();

                progressDialog.dismiss();
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    ArrayList<Likes>likes=new ArrayList<Likes>();

                    String answers_id= ds.getKey();
                    String qa_id= (String) ds.child("qa_id").getValue();
                    String publisher_id= (String) ds.child("publisher_id").getValue();
                    String publisher_name= (String) ds.child("publisher_name").getValue();
                    String answers_text= (String) ds.child("answers_text").getValue();
                    String date_time= (String) ds.child("date_time").getValue();

                    for (DataSnapshot ds2 : ds.child("likes").getChildren()) {
                        Likes like= ds2.getValue(Likes.class);
                        like.setLike_id(ds2.getKey());
                        likes.add(like);
                    }

//                    Answers an = ds.getValue(Answers.class);
//                    an.setAnswers_id(ds.getKey());
                    answers.add(new Answers(answers_id,qa_id,question_id,publisher_id,publisher_name,answers_text,date_time,likes));
                }

                if(answers.size()==0){
                    answwer_ampty.setVisibility(View.VISIBLE);
                    Toast.makeText(AddCommentActivity.this, "لا يوجد اجوبة", Toast.LENGTH_SHORT).show();
                }else {
                    answwer_ampty.setVisibility(View.GONE);

                }

                answersAdapter.notifyDataSetChanged();



            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }


    public String getDate(){
        Calendar calFordDate = Calendar.getInstance();
        SimpleDateFormat currentDate = new SimpleDateFormat("dd-MMMM-yyyy");
        String saveCurrentDate = currentDate.format(calFordDate.getTime());

        return saveCurrentDate;

    }
    public void sendCommentNotification(final String username, final String qa_titel,final String qa_text,final String question_id,final String publisher_uid,final String publisher_name ) {

        new AsyncTask<String, String, String>() {
            @Override
            protected String doInBackground(String... params) {
                try {
                    JSONObject message_root = new JSONObject();
                    JSONObject notification = new JSONObject();
                    notification.put("title", username);

                    notification.put("type", "Question");
                    notification.put("decription", qa_titel);
                    notification.put("qa_titel", qa_titel);
                    notification.put("qa_text", qa_text);
                    notification.put("question_id", question_id);
                    notification.put("publisher_uid", publisher_uid);
                    notification.put("publisher_name", publisher_name);


                    message_root.put("data", notification);
                    message_root.put("to", "/topics/"+qa_uid);

                    String result = postToFCM(message_root.toString());
                    return result;
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
                return null;
            }

            @Override
            protected void onPostExecute(String result) {

            }
        }.execute();
    }
    String postToFCM(String bodyString) throws IOException {
        RequestBody body = RequestBody.create(JSON, bodyString);
        Request request = new Request.Builder()
                .url(FCM_MESSAGE_URL)
                .post(body)
                .addHeader("Authorization", "key=AAAAYZqPxo4:APA91bHsrUdGIUUBDhEx_zjHg_8OtYqAuXiWW_BZ7f6u2NMROUMRmj3RkUpjnd55_Aa3bw-KUJ0jp4cG_UiJmZnyFuYmi7BhHg-gb4arzp35L4Y-i3WyzvHEPtbeSiXA2URWmKHTVXuG" )
                .addHeader("Content-Type", "application/json" )
                .build();
        Response response = mClient.newCall(request).execute();
        return response.body().string();
    }
    @Override
    public void onBackPressed() {

        if(backType.equalsIgnoreCase("Question")){
            super.onBackPressed();

            startActivity(new Intent(AddCommentActivity.this,MainActivity.class));

        }else {
            super.onBackPressed();

            finish();

        }
    }

    private void check_if_allowed(final FirebaseCallBack firebaseCallBack){
        final String ui=mAuth.getCurrentUser().getUid();
        ValueEventListener listener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                boolean flag =false;
                experts =(ArrayList<Matched>) snapshot.getValue();
                double size = experts.size();
               for(int i=0 ;i<size;i++){
                    //here i can compare the uid
                   if(snapshot.child(String.valueOf(i)).child("expert_ID").getValue().equals(ui)) {
                       flag=true;
                       firebaseCallBack.onCallBack(flag);
                       return;
                   }
                   else {
                       firebaseCallBack.onCallBack(flag);
                   }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.d(TAG, error.getMessage()); //Don't ignore errors!
            }
        };//event listener
        rootRef.addValueEventListener(listener);
    }
    private interface FirebaseCallBack{
        void onCallBack(boolean flag);
    }}