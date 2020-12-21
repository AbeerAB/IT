package com.h.itservices.adapter;

import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
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
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.h.itservices.R;
import com.h.itservices.activities.AddCommentActivity;
import com.h.itservices.model.Answers;
import com.h.itservices.model.Likes;
import com.h.itservices.model.Questions;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

import static android.content.Context.MODE_PRIVATE;


public class QuestionsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context context;
    private LayoutInflater inflater;
    List<Questions> questions;
    MyHolder myHolder;
    private DatabaseReference myRef ;
    private FirebaseDatabase database;
    ProgressDialog progressDialog;

    public SharedPreferences prefs;
    public static String MY_PREFS_NAME = "itservice";
    public static SharedPreferences.Editor editor;
    public QuestionsAdapter(Context context, List<Questions> questions) {
        this.context = context;
        inflater = LayoutInflater.from(context);
        this.questions = questions;
        database = FirebaseDatabase.getInstance();
        myRef = database.getReference();
        prefs = context.getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
        editor = prefs.edit();

        progressDialog=new ProgressDialog(context);
        progressDialog.setCancelable(false);
        progressDialog.setMessage(context.getResources().getString(R.string.msg_login));
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.row_qa, parent, false);
        MyHolder holder = new MyHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        final Questions g = questions.get(position);
        myHolder = (MyHolder) holder;
        myHolder.date.setText(g.getDate_time());
        myHolder.username.setText(g.getPublisher_name());
        myHolder.username.setText(g.getPublisher_name());
        myHolder.qa_titel.setText(g.getQuestion_title());



        if(prefs.getString("user_type","").equalsIgnoreCase("admin")){
            myHolder.like.setVisibility(View.GONE);
            myHolder.count_likes.setVisibility(View.GONE);
            myHolder.delete.setVisibility(View.VISIBLE);

        }else {
            myHolder.like.setVisibility(View.VISIBLE);
            myHolder.count_likes.setVisibility(View.VISIBLE);
            myHolder.delete.setVisibility(View.GONE);

        }



        if(!g.getPublisher_img().isEmpty()){
            StorageReference mStorage = FirebaseStorage.getInstance().getReference();
            StorageReference filepath = mStorage.child("Profile_images/").child(g.getPublisher_img());
            Glide.with(context).load(filepath).placeholder(R.drawable.user_empty)
                    .error(R.drawable.user_empty).into(myHolder.user_icon);


        }


        if(g.getAnswers()==null){
            myHolder.qa_count.setText("0");


        }else {
            myHolder.qa_count.setText(g.getAnswers().size()+"");


        }

        if(g.getLikes().isEmpty()){
            myHolder.count_likes.setText("0");


        }else {
            for(int i=0;i<g.getLikes().size();i++){
                if(g.getQuestion_id().equalsIgnoreCase(g.getLikes().get(i).getQa_id())
                        &&g.getLikes().get(i).getPublisher_id().equalsIgnoreCase(FirebaseAuth.getInstance().getCurrentUser().getUid()
              )){
                    if(g.getLikes().get(i).getLike()){
                        myHolder.like.setChecked(true);
                    }else {
                        myHolder.like.setChecked(false);
                    }


                }


            }

            myHolder.count_likes.setText(g.getLikes().size()+"");

        }

        myHolder.like.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {

                if(b){
                    Likes like=new Likes("0",g.getQuestion_id(),FirebaseAuth.getInstance().getCurrentUser().getUid(),true);
                    myRef.child("Question").child(g.getQuestion_id()).child("likes").push().setValue(like).addOnFailureListener(new OnFailureListener() {
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
                    for(int i=0;i<g.getLikes().size();i++) {
                        if(g.getQuestion_id().equalsIgnoreCase(g.getLikes().get(i).getQa_id())&&
                                g.getLikes().get(i).getPublisher_id().equalsIgnoreCase(FirebaseAuth.getInstance().getCurrentUser().getUid())){

                            DatabaseReference remove = FirebaseDatabase.getInstance().getReference().
                            child("Question").child(g.getQuestion_id()).child("likes").child(g.getLikes().get(i).getLike_id());
                            remove.removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                }
                            });


                            break;

                        }else {

                        }
                    }





                }

            }
        });



        myHolder.click_row.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(context, AddCommentActivity.class);
                intent.putExtra("question_id",g.getQuestion_id());
                intent.putExtra("publisher_uid",g.getPublisher_id());
                intent.putExtra("publisher_name",g.getPublisher_name());
                intent.putExtra("qa_titel",g.getQuestion_title());
                intent.putExtra("qa_text",g.getQuestion_text());
                intent.putExtra("qa_uid",g.getQuestion_id());
                context.startActivity(intent);

            }
        });

        myHolder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog myQuittingDialogBox = new AlertDialog.Builder(context)
                        .setTitle("الحذف")
                        .setMessage("هل انت متأكد من حذف السؤال ؟")

                        .setPositiveButton("نعم", new DialogInterface.OnClickListener() {

                            public void onClick(DialogInterface dialog2, int whichButton) {

                                progressDialog.show();

                                DatabaseReference remove = FirebaseDatabase.getInstance().getReference()
                                        .child("Question").child(g.getQuestion_id());

                                remove.removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        progressDialog.dismiss();
                                        Toast.makeText(context, "تم الحذف", Toast.LENGTH_LONG).show();


                                    }
                                });



                            }

                        })
                        .setNegativeButton("لا", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {

                                dialog.dismiss();

                            }
                        })
                        .create();
                myQuittingDialogBox.show();

            }
        });

    }

    @Override
    public int getItemCount() {
        return questions.size();
    }

    class MyHolder extends RecyclerView.ViewHolder {

        TextView username, date,qa_titel,qa_count,count_likes;
        LinearLayout click_row;
        CircleImageView user_icon;
        ImageView  delete;
        CheckBox like;


        public MyHolder(View itemViewRes) {
            super(itemViewRes);
            username = itemViewRes.findViewById(R.id.username);
            date = itemViewRes.findViewById(R.id.date);
            username = itemViewRes.findViewById(R.id.username);
            qa_titel = itemViewRes.findViewById(R.id.qa_titel);
            qa_count = itemViewRes.findViewById(R.id.qa_count);
            click_row = itemViewRes.findViewById(R.id.click_row);
            user_icon = itemViewRes.findViewById(R.id.user_icon);
            like = itemViewRes.findViewById(R.id.like);
            count_likes = itemViewRes.findViewById(R.id.count_likes);
            delete = itemViewRes.findViewById(R.id.delete);
        }
    }
}