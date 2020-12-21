package com.h.itservices.activities;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.h.itservices.R;
import com.h.itservices.model.Answers;
import com.h.itservices.model.Likes;
import com.h.itservices.model.Matched;
import com.h.itservices.model.Questions;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import static android.content.ContentValues.TAG;
import static android.util.Log.i;

public class AddQuestionActivity extends AppCompatActivity {

    EditText question_title,question_text;
    Button post_btn,cancel_btn;

    String postid;
    String publisherid;
    FirebaseUser firebaseUser;
    private FirebaseAuth myAccount ;
    private String saveCurrentDate, saveCurrentTime  ;
    CheckBox prog11,prog12,prog13,prog14,prog15,prog16,prog17;
    CheckBox prog21,prog22,prog23,prog24,prog25;
    CheckBox prog1,prog2,prog3,prog4,prog5,prog6,prog7;
    ArrayList<String> exp;

    Button choose_exp;
    ProgressDialog progressDialog;
    private DatabaseReference myRef ;
    private FirebaseDatabase database;

    TextView text_exp;
    ArrayList<Matched> experts;
    ArrayList<Answers> answers;
    ArrayList<Likes> likes;
    public SharedPreferences prefs;
    public static String MY_PREFS_NAME = "itservice";
    public static SharedPreferences.Editor editor;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add__question_);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        database = FirebaseDatabase.getInstance();
        prefs = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);

        myRef = database.getReference();
        experts =new ArrayList<>();
        answers=new ArrayList<>();
        likes=new ArrayList<>();
// to undo to previose
        getSupportActionBar().setTitle("إسأل");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        exp=new ArrayList<>();

        question_title=findViewById(R.id.question_title);
        question_text=findViewById(R.id.question_text);
        post_btn=findViewById(R.id.post_btn);
        cancel_btn=findViewById(R.id.cancel_btn);
        choose_exp=findViewById(R.id.choose_exp);
        text_exp=findViewById(R.id.text_exp);

        Intent intent= getIntent();
        //postid=intent.getStringExtra("postid");
        //publisherid=intent.getStringExtra("publisherid");







        post_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (question_title.getText().toString().equals("")  ){
             Toast.makeText(AddQuestionActivity.this,"أدخل عنوان للسؤال",Toast.LENGTH_SHORT).show();
                    question_title.setError("أدخل عنوان للسؤال"); }
                if (question_text.getText().toString().equals("")){
                    question_text.setError("أدخل محتوى السؤال");
              Toast.makeText(AddQuestionActivity.this,"أدخل محتوى السؤال",Toast.LENGTH_SHORT).show(); }
              if (exp.size()==0){
             Toast.makeText(AddQuestionActivity.this, "يجب إختيار مجال", Toast.LENGTH_SHORT).show(); }
              if(!question_text.getText().toString().equalsIgnoreCase("")&&! question_title.getText().toString().equalsIgnoreCase("")
                && exp.size()!=0) {
                       startPosting(); }

            }});

        cancel_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        choose_exp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Dialog dialog = new Dialog(AddQuestionActivity.this);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setCancelable(true);
                dialog.setContentView(R.layout.dialog_exp);

                Button add=(Button)dialog.findViewById(R.id.choose);
                prog11 =dialog. findViewById(R.id.prog11);
                prog12 = dialog.findViewById(R.id.prog12);
                prog13 =dialog. findViewById(R.id.prog13);
                prog14 =dialog. findViewById(R.id.prog14);
                prog15 =dialog. findViewById(R.id.prog15);
                prog16 =dialog. findViewById(R.id.prog16);
                prog17 =dialog. findViewById(R.id.prog17);

                prog21 =dialog. findViewById(R.id.prog21);
                prog22 =dialog. findViewById(R.id.prog22);
                prog23 =dialog. findViewById(R.id.prog23);
                prog24 =dialog. findViewById(R.id.prog24);
                prog25 =dialog. findViewById(R.id.prog25);

                prog1 = dialog.findViewById(R.id.prog1);
                prog2 =dialog. findViewById(R.id.prog2);
                prog3 = dialog.findViewById(R.id.prog3);
                prog4 =dialog. findViewById(R.id.prog4);
                prog5 = dialog.findViewById(R.id.prog5);
                prog6 = dialog.findViewById(R.id.prog6);
                prog7 = dialog.findViewById(R.id.prog7);
                add.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

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

                        if (exp.size()==0){
                            Toast.makeText(AddQuestionActivity.this, "يجب إختيار مجال", Toast.LENGTH_SHORT).show();
                        }else {

                            String result="";
                            for(int i=0;i<exp.size();i++){

                                result=result+exp.get(i)+",";

                            }
                            text_exp.setText(result);

                            dialog.dismiss();

                        }



                    }
                });

                dialog.show();

            }
        });

    }// on Create



        private void startPosting(){

            progressDialog=new ProgressDialog(AddQuestionActivity.this);
            progressDialog.setMessage("إنتظر ...");
            progressDialog.setCancelable(false);
            progressDialog.show();
            Calendar calFordDate = Calendar.getInstance();
            SimpleDateFormat currentDate = new SimpleDateFormat("dd-MMMM-yyyy");
            saveCurrentDate = currentDate.format(calFordDate.getTime());
            String questionId=myRef.child("Question").push().getKey();
            final Questions questions =new Questions(questionId,
                    FirebaseAuth.getInstance().getCurrentUser().getUid(),
                    prefs.getString("username",""),
                    prefs.getString("profile_image_name",""),
                    question_title.getText().toString(),
                    question_text.getText().toString(),
                    saveCurrentDate,prefs.getString("user_type",""),exp ,answers,likes,experts);

            myRef.child("Question").child(questionId).setValue(questions).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(AddQuestionActivity.this, "مشكلة في الأنترنت حاول مرة اخرى!", Toast.LENGTH_SHORT).show();
                    progressDialog.dismiss();
                }
            }).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {

                    Log.i(TAG, " the new question exp above is "+questions.getExp() );
                    Log.i(TAG, " the new question push key above is "+questions.getQuestion_id() );

                    progressDialog.dismiss();
                    question_title.setText("");
                    question_text.setText("");
                    exp.clear();
                    text_exp.setText("");
                    matchQuestionToExpert(questions);

                }
            });

    }


    boolean isEmpty(EditText text) {
        CharSequence str = text.getText().toString();
        return TextUtils.isEmpty(str);
    }

public void matchQuestionToExpert (final Questions currentQ) {
    // ______first data snapshot for question root second data snapshot for users root________
    // _________single value event listener to trigger once the current question_______
    //_________value event listeners for the users and for the user exp_______

    Log.i(TAG, " the new question id is "+currentQ.getQuestion_id() );
    Log.i(TAG, " the new question exp is empty "+currentQ.getExp() );
    //it works for now let check later

    final DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference();
      final String cQID=currentQ.getQuestion_id();
     DatabaseReference quesRef = rootRef.child("Question").child(currentQ.getQuestion_id());
     //Start of event listener
    ValueEventListener firstListener = new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot snapshot) {
            final String CurrentUID= (String) snapshot.child("publisher_id").getValue();
           final ArrayList<String>questionExp= (ArrayList<String>) snapshot.child("exp").getValue();
            final int QESize =questionExp.size();

                Query queryExp = rootRef.child("User").orderByChild("type").equalTo("exp");
                ValueEventListener secondListener =new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {

                        final ArrayList<Matched> MatchedExp = new ArrayList<>();
                        //here all the users

                        //here is the single expert
                        for (DataSnapshot ds : snapshot.getChildren()){
                            Log.i(TAG, " what is dsss "+ds);//ok full
                            String singleUI = (String) ds.child("user_id").getValue();
                            String usernameEx=(String)ds.child("user_name").getValue();
                            if(!(singleUI.equals(CurrentUID))) {
                                double Score=0;
                                double sizetemp = QESize;
                                // user Variables

                                ArrayList<String> UserExp = new ArrayList<>();
                                UserExp = (ArrayList<String>) ds.child("exp").getValue();

                                for ( int i=0 ; i<QESize;i++) {
                                    if (UserExp.contains(questionExp.get(i))){
                                      Score++;}
                                }
                                //here before i end with the current user
                                Score= (Score/sizetemp )*100;
                                //generate the variable to be add to the list
                                //push key
                                if (Score>=65) {
                                    String pushkey = myRef.child("Question").child(cQID).child("experts").push().getKey();
                                    //assign the object to database
                                    Matched match = new Matched(pushkey, cQID, singleUI, usernameEx,Score);
                                    //array alwayes updates
                                    MatchedExp.add(match);
                                }//if the score greater than 50
                            }//if the user ! current user
                        }//ds
                        myRef.child("Question").child(cQID).child("experts").setValue(MatchedExp).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(AddQuestionActivity.this, "مشكلة في الأنترنت حاول مرة اخرى!", Toast.LENGTH_SHORT).show();
                            }
                        }).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {


                            /*
                                if(MatchedExp.isEmpty()){
                                    AlertDialog myQuittingDialogBox = new AlertDialog.Builder(AddQuestionActivity.this)
                                            .setTitle("تم الإرسال")
                                            .setMessage("نأسف ! في الوقت الحالي لايوجد خبير يتطابق مع مجال سؤالك \n سيتواصل معك الخبير ما ان يتوفر شكرا لإنتظارك ")
                                            .setPositiveButton("حسنا", new DialogInterface.OnClickListener() {
                                                public void onClick(DialogInterface dialog2, int whichButton) {
                                                    dialog2.dismiss();
                                                    finish();
                                                }})
                                            .create();
                                    myQuittingDialogBox.show();
                                }
                                else{



                                //notification
                                AlertDialog myQuittingDialogBox = new AlertDialog.Builder(AddQuestionActivity.this)
                                        .setTitle("تم الإرسال")
                                        .setMessage("لقد تم إرسال سؤالك الى الخبراء الأكثر تطابقا مع مجال سؤالك! \n سيتواصل معك الخبير قريبا شكرا لانتظارك ")
                                        .setPositiveButton("حسنا", new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog2, int whichButton) {
                                                dialog2.dismiss();
                                                finish();
                                            }})
                                        .create();
                                myQuittingDialogBox.show();

                            }



                             */
                            }
                        });

                    }//data snapshot
                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                    }
                };//second listener
            queryExp.addValueEventListener(secondListener);
            }
        @Override
        public void onCancelled(@NonNull DatabaseError error) {
            Log.d(TAG, error.getMessage()); //Don't ignore errors!
        }
    };
    quesRef.addListenerForSingleValueEvent(firstListener);
}
}