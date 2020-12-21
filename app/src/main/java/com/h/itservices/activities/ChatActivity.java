package com.h.itservices.activities;

import android.Manifest;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.h.itservices.R;
import com.h.itservices.adapter.AnswersAdapter;
import com.h.itservices.adapter.ChatAdapter;
import com.h.itservices.model.Answers;
import com.h.itservices.model.Chat;
import com.intentfilter.androidpermissions.PermissionManager;

import org.json.JSONObject;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import static java.util.Collections.singleton;

public class ChatActivity extends AppCompatActivity {


    ProgressDialog progressDialog;

    ArrayList<Chat> chats;
    private FirebaseAuth mAuth;
    ChatAdapter chatAdapter;
    RecyclerView recycle_view;
    TextView answwer_ampty;
    String name;
    public SharedPreferences prefs;
    public static String MY_PREFS_NAME = "itservice";
    public static SharedPreferences.Editor editor;
    private DatabaseReference myRef ;
    private FirebaseDatabase database;


    EditText msg;

    ImageView send,attach;
    private Uri image_uri;

    String publisher_id,my_id,qa_id;
    public static final String FCM_MESSAGE_URL = "https://fcm.googleapis.com/fcm/send";
    public static MediaType JSON= MediaType.parse("application/json; charset=utf-8");
    OkHttpClient mClient = new OkHttpClient();

    String backType="app_back";
    private StorageReference mStorage;
    StorageReference filepath;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chat_activity);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        mAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        myRef = database.getReference();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);




        prefs = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
        editor = prefs.edit();

        msg=findViewById(R.id.msg);
        send=findViewById(R.id.send);
        attach=findViewById(R.id.attach);


        progressDialog=new ProgressDialog(ChatActivity.this);
        progressDialog.setCancelable(true);
        progressDialog.setMessage("انتظر.....");


        answwer_ampty=findViewById(R.id.answwer_ampty);

        chats=new ArrayList<>();

        my_id=FirebaseAuth.getInstance().getCurrentUser().getUid();

        try {

            publisher_id=getIntent().getStringExtra("publisher_id");
            qa_id=getIntent().getStringExtra("qa_id");
            name=getIntent().getStringExtra("name");

            getSupportActionBar().setTitle("محادثه");

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

                if(backType.equalsIgnoreCase("notification")){
                    startActivity(new Intent(ChatActivity.this,MainActivity.class));

                }else {
                    finish();

                }
            }
        });
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                if(!msg.getText().toString().isEmpty()){
                    progressDialog.show();

                    final Chat chat=new Chat(qa_id,publisher_id,prefs.getString("username",""),msg.getText().toString(),getDate(),"");

                    myRef.child("Messages").push().setValue(chat).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(ChatActivity.this, "مشكلة في الأنترنت حاول مرة اخرى!", Toast.LENGTH_SHORT).show();
                            progressDialog.dismiss();
                        }
                    }).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            progressDialog.dismiss();
                            sendChatNotification(prefs.getString("username",""),msg.getText().toString(),publisher_id,qa_id,name);
                            msg.setText("");
                            recycle_view.smoothScrollToPosition(21);



                        }
                    });
                }else{
                    Toast.makeText(ChatActivity.this, "أدخل الرسالة", Toast.LENGTH_SHORT).show();
                }


            }
        });

        attach.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                PermissionManager permissionManager = PermissionManager.getInstance(ChatActivity.this);
                permissionManager.checkPermissions(singleton(Manifest.permission.READ_EXTERNAL_STORAGE), new PermissionManager.PermissionRequestListener() {
                    @Override
                    public void onPermissionGranted() {

                        Intent gallery = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);
                        startActivityForResult(gallery, 300);
                    }

                    @Override
                    public void onPermissionDenied() {
                    }
                });



            }
        });


        recycle_view=(RecyclerView)findViewById(R.id.recycle_view);

        chatAdapter = new ChatAdapter(ChatActivity.this, chats);
        LinearLayoutManager layoutManager = new LinearLayoutManager(ChatActivity.this, LinearLayoutManager.VERTICAL, false);
        recycle_view.setLayoutManager(layoutManager);
        recycle_view.setAdapter(chatAdapter);

        get_chat_list();

    }
    public void get_chat_list(){
        progressDialog.show();



        DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference().child("Messages");

        rootRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                chats.clear();
                progressDialog.dismiss();
                for (DataSnapshot ds : dataSnapshot.getChildren()) {

//                    if(prefs.getString("user_type","").equalsIgnoreCase("exp")){
                        if (String.valueOf(ds.child("exp_id").getValue()).equalsIgnoreCase(publisher_id)
                                && String.valueOf(ds.child("user_id").getValue()).equalsIgnoreCase(qa_id) ){
                            Chat an = ds.getValue(Chat.class);
                            chats.add(an);
                        }

//                    }


                }

                if(chats.size()==0){
                    answwer_ampty.setVisibility(View.VISIBLE);
                }else {
                    answwer_ampty.setVisibility(View.GONE);

                }
                chatAdapter.notifyDataSetChanged();
                recycle_view.smoothScrollToPosition(21);



            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }
    public void uploadChatImage() {
        progressDialog.show();
        final String img_titel = "chat_" + (int) (new Date().getTime() / 1000);


        mStorage = FirebaseStorage.getInstance().getReference();
        if (image_uri!=null) {

            filepath = mStorage.child("Chat_Images/").child(img_titel);
            filepath.putFile(image_uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                }
            }).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                    final Chat chat=new Chat(qa_id,publisher_id,prefs.getString("username",""),"",getDate(),img_titel);

                    myRef.child("Messages").push().setValue(chat).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
//                            Toast.makeText(ChatActivity.this, "مشكلة في الأنترنت حاول مرة اخرى!", Toast.LENGTH_SHORT).show();
                            progressDialog.dismiss();
                        }
                    }).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            progressDialog.dismiss();
                            recycle_view.smoothScrollToPosition(21);



                        }
                    });




                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(ChatActivity.this, "يوجد مشكلة في الأنترنت حاول مجددا", Toast.LENGTH_SHORT).show();
                }
            });
            /* */

        }
    }

    public String getDate(){
        Calendar calFordDate = Calendar.getInstance();
        SimpleDateFormat currentDate = new SimpleDateFormat("dd-MMMM-yyyy");
        String saveCurrentDate = currentDate.format(calFordDate.getTime());

        return saveCurrentDate;

    }

    public void sendChatNotification(final String username, final String msg,final String publisher_id,final String qa_id,final String name ) {

        new AsyncTask<String, String, String>() {
            @Override
            protected String doInBackground(String... params) {
                try {
                    JSONObject message_root = new JSONObject();
                    JSONObject notification = new JSONObject();
                    notification.put("title", username);

                    notification.put("type", "chat");
                    notification.put("decription", msg);
                    notification.put("publisher_id", publisher_id);
                    notification.put("qa_id", qa_id);
                    notification.put("name", name);


                    message_root.put("data", notification);

                     if(prefs.getString("user_type","").equalsIgnoreCase("exp")){
                         message_root.put("to", "/topics/"+qa_id);

                     }else {
                         message_root.put("to", "/topics/"+publisher_id);

                     }


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

        if(backType.equalsIgnoreCase("notification")){
            super.onBackPressed();

            startActivity(new Intent(ChatActivity.this,MainActivity.class));

        }else {
            super.onBackPressed();

            finish();

        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == 300 && data != null) {
            image_uri = data.getData();
            uploadChatImage();
        }
    }

}