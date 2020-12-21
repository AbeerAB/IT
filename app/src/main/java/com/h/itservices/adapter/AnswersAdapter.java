package com.h.itservices.adapter;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.h.itservices.R;
import com.h.itservices.activities.AddCommentActivity;
import com.h.itservices.activities.ChatActivity;
import com.h.itservices.model.Answers;
import com.h.itservices.model.Chat;
import com.h.itservices.model.Likes;
import com.h.itservices.model.Questions;

import java.util.List;

import static android.content.Context.MODE_PRIVATE;


public class AnswersAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context context;
    private LayoutInflater inflater;
    List<Answers> answers;
    MyHolder myHolder;
    public static String MY_PREFS_NAME = "itservice";
    public static SharedPreferences.Editor editor;
    public SharedPreferences prefs;
    private DatabaseReference myRef ;
    private FirebaseDatabase database;

    public AnswersAdapter(Context context, List<Answers> answers) {
        this.context = context;
        inflater = LayoutInflater.from(context);
        this.answers = answers;

        prefs = context.getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
        editor = prefs.edit();
        database = FirebaseDatabase.getInstance();
        myRef = database.getReference();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.row_answer, parent, false);
        MyHolder holder = new MyHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        final Answers answer = answers.get(position);
        myHolder = (MyHolder) holder;
        myHolder.date.setText(answer.getDate_time());
        myHolder.username.setText(answer.getPublisher_name());
        myHolder.qa_titel.setText(answer.getAnswers_text());


        if(prefs.getString("user_type","").equalsIgnoreCase("admin")){
            myHolder.like.setVisibility(View.GONE);
            myHolder.count_likes.setVisibility(View.GONE);
            myHolder.chat.setVisibility(View.GONE);

        }else {
            myHolder.like.setVisibility(View.VISIBLE);
            myHolder.count_likes.setVisibility(View.VISIBLE);
            myHolder.chat.setVisibility(View.VISIBLE);

        }




       if(prefs.getString("user_type","").equalsIgnoreCase("exp")){
           if(FirebaseAuth.getInstance().getCurrentUser().getUid().equalsIgnoreCase(answer.getPublisher_id())){
               myHolder.chat.setVisibility(View.VISIBLE);

           }else {
               myHolder.chat.setVisibility(View.GONE);

           }
       }else {
           if(FirebaseAuth.getInstance().getCurrentUser().getUid().equalsIgnoreCase(answer.getQa_id())){
               myHolder.chat.setVisibility(View.VISIBLE);

           }else {
               myHolder.chat.setVisibility(View.GONE);
           }
       }

            myHolder.chat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(context, ChatActivity.class);
                intent.putExtra("qa_id",answer.getQa_id());
                intent.putExtra("publisher_id",answer.getPublisher_id());
                intent.putExtra("name",answer.getPublisher_name());
                context.startActivity(intent);


            }
        });


        if(answer.getLikes().isEmpty()){
            myHolder.count_likes.setText("0");


        }else {
            for(int i=0;i<answer.getLikes().size();i++){
                if(answer.getQuestion_id().equalsIgnoreCase(answer.getLikes().get(i).getQa_id())
                        &&answer.getLikes().get(i).getPublisher_id().equalsIgnoreCase(FirebaseAuth.getInstance().getCurrentUser().getUid()
                )){
                    if(answer.getLikes().get(i).getLike()){
                        myHolder.like.setChecked(true);
                    }else {
                        myHolder.like.setChecked(false);
                    }


                }


            }

            myHolder.count_likes.setText(answer.getLikes().size()+"");

        }

        myHolder.like.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {

                if(b){

                    Log.d("ddddddddderr",answer.getQuestion_id());
                    Log.d("ddddddddderr",answer.getAnswers_id());
                    Likes like=new Likes("0",answer.getQuestion_id(),FirebaseAuth.getInstance().getCurrentUser().getUid(),true);
                    myRef.child("Question").child(answer.getQuestion_id()).child("answers").child(answer.getAnswers_id()).child("likes").push().setValue(like).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(context, "مشكلة في الأنترنت حاول مرة اخرى!", Toast.LENGTH_SHORT).show();
                        }
                    }).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {

                        }
                    });



                }else {
                    for(int i=0;i<answer.getLikes().size();i++) {
                        if(answer.getQuestion_id().equalsIgnoreCase(answer.getLikes().get(i).getQa_id())&&
                                answer.getLikes().get(i).getPublisher_id().equalsIgnoreCase(FirebaseAuth.getInstance().getCurrentUser().getUid())){

                            DatabaseReference remove = FirebaseDatabase.getInstance().getReference().
                                    child("Question").child(answer.getQuestion_id()).child("answers").child(answer.getAnswers_id()).child("likes").child(answer.getLikes().get(i).getLike_id());
                            remove.removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                }
                            });


                            break;

                        }else {

                        }
                    }
//
//



                }

            }
        });


    }

    @Override
    public int getItemCount() {
        return answers.size();
    }

    class MyHolder extends RecyclerView.ViewHolder {

        TextView username, date,qa_titel,count_likes;
        LinearLayout click_row;
        ImageView chat;
        CheckBox like;

        public MyHolder(View itemViewRes) {
            super(itemViewRes);
            username = itemViewRes.findViewById(R.id.username);
            date = itemViewRes.findViewById(R.id.date);
            username = itemViewRes.findViewById(R.id.username);
            qa_titel = itemViewRes.findViewById(R.id.qa_titel);
            click_row = itemViewRes.findViewById(R.id.click_row);
            chat = itemViewRes.findViewById(R.id.chat);
            count_likes = itemViewRes.findViewById(R.id.count_likes);
            like = itemViewRes.findViewById(R.id.like);

        }
    }
}