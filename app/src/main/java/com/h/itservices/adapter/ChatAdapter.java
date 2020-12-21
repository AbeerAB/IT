package com.h.itservices.adapter;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.h.itservices.R;
import com.h.itservices.activities.ChatActivity;
import com.h.itservices.model.Answers;
import com.h.itservices.model.Chat;

import java.util.List;

import static android.content.Context.MODE_PRIVATE;


public class ChatAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context context;
    private LayoutInflater inflater;
    List<Chat>chats;
    MyHolder myHolder;
    public SharedPreferences prefs;
    public static String MY_PREFS_NAME = "itservice";
    public static SharedPreferences.Editor editor;
    public ChatAdapter(Context context, List<Chat> chats) {
        this.context = context;
        inflater = LayoutInflater.from(context);
        this.chats = chats;

        prefs = context.getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
        editor = prefs.edit();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.row_chat, parent, false);
        MyHolder holder = new MyHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        final Chat chat = chats.get(position);
        myHolder = (MyHolder) holder;


        if(prefs.getString("user_type","").equalsIgnoreCase("exp")){

            if(chat.getExp_id().equalsIgnoreCase(FirebaseAuth.getInstance().getCurrentUser().getUid())

                    &&prefs.getString("username","").equalsIgnoreCase(chat.getUser_name())){

                if(!chat.getMessage().equalsIgnoreCase("")){
                    myHolder.img__send.setVisibility(View.GONE);
                    myHolder.img__rcv.setVisibility(View.GONE);

                    myHolder.msg_rcv.setVisibility(View.GONE);
                    myHolder.msg_send.setVisibility(View.VISIBLE);
                    myHolder.msg_rcv.setVisibility(View.GONE);

                    myHolder.msg_send.setText(chat.getMessage());

                }else {
                    myHolder.msg_rcv.setVisibility(View.GONE);
                    myHolder.msg_send.setVisibility(View.GONE);


                    myHolder.img__rcv.setVisibility(View.GONE);
                    myHolder.img__send.setVisibility(View.VISIBLE);


                    StorageReference mStorage = FirebaseStorage.getInstance().getReference();
                    StorageReference filepath = mStorage.child("Chat_Images/").child(chat.getImg_name());
                    Glide.with(context).load(filepath).placeholder(R.drawable.empty)
                            .error(R.drawable.empty).into(myHolder.img__send);

                }

            }else {
                if(!chat.getMessage().isEmpty()) {
                    myHolder.msg_rcv.setVisibility(View.VISIBLE);
                    myHolder.msg_send.setVisibility(View.GONE);

                    myHolder.img__rcv.setVisibility(View.GONE);
                    myHolder.img__send.setVisibility(View.GONE);

                    myHolder.msg_rcv.setText(chat.getMessage());

                }else if (!chat.getImg_name().isEmpty()){
                    myHolder.msg_rcv.setVisibility(View.GONE);
                    myHolder.msg_send.setVisibility(View.GONE);


                    myHolder.img__rcv.setVisibility(View.VISIBLE);
                    myHolder.msg_send.setVisibility(View.GONE);
                    StorageReference mStorage = FirebaseStorage.getInstance().getReference();
                    StorageReference filepath = mStorage.child("Chat_Images/").child(chat.getImg_name());
                    Glide.with(context).load(filepath).placeholder(R.drawable.empty)
                            .error(R.drawable.empty).into(myHolder.img__rcv);

                }

            }

        }else {
            if(chat.getUser_id().equalsIgnoreCase(FirebaseAuth.getInstance().getCurrentUser().getUid())
                    &&prefs.getString("username","").equalsIgnoreCase(chat.getUser_name())){

                if(!chat.getMessage().equalsIgnoreCase("")){
                    myHolder.img__send.setVisibility(View.GONE);
                    myHolder.msg_rcv.setVisibility(View.GONE);

                    myHolder.msg_send.setVisibility(View.VISIBLE);
                    myHolder.msg_rcv.setVisibility(View.GONE);

                    myHolder.msg_send.setText(chat.getMessage());

                }else {

                    myHolder.img__send.setVisibility(View.VISIBLE);
                    myHolder.msg_rcv.setVisibility(View.GONE);
                    myHolder.msg_send.setVisibility(View.GONE);
                    StorageReference mStorage = FirebaseStorage.getInstance().getReference();
                    StorageReference filepath = mStorage.child("Chat_Images/").child(chat.getImg_name());
                    Glide.with(context).load(filepath).placeholder(R.drawable.empty)
                            .error(R.drawable.empty).into(myHolder.img__send);

                }
            }else {

                if(!chat.getMessage().isEmpty()) {
                    myHolder.img__rcv.setVisibility(View.GONE);
                    myHolder.img__send.setVisibility(View.GONE);

                    myHolder.msg_rcv.setVisibility(View.VISIBLE);
                    myHolder.msg_send.setVisibility(View.GONE);

                    myHolder.msg_rcv.setText(chat.getMessage());

                }else if (!chat.getImg_name().isEmpty()){
                    myHolder.img__rcv.setVisibility(View.VISIBLE);
                    myHolder.msg_rcv.setVisibility(View.GONE);

                    myHolder.msg_send.setVisibility(View.GONE);
                    myHolder.msg_rcv.setVisibility(View.GONE);

                    StorageReference mStorage = FirebaseStorage.getInstance().getReference();
                    StorageReference filepath = mStorage.child("Chat_Images/").child(chat.getImg_name());
                    Glide.with(context).load(filepath).placeholder(R.drawable.empty)
                            .error(R.drawable.empty).into(myHolder.img__rcv);

                }

            }

        }






//        myHolder.msg_send.setText(chat.getUser_name());




//        else {
//            if(chat.getUser_id().equalsIgnoreCase(FirebaseAuth.getInstance().getCurrentUser().getUid())){
//                myHolder.msg_send.setText(chat.getMessage());
//
//            }else {
//                myHolder.msg_rcv.setText(chat.getMessage());
//
//            }
//        }


    }

    @Override
    public int getItemCount() {
        return chats.size();
    }

    class MyHolder extends RecyclerView.ViewHolder {

        TextView msg_rcv,msg_send;
        ImageView img__rcv,img__send;
//        LinearLayout click_row;

        public MyHolder(View itemViewRes) {
            super(itemViewRes);
//            username = itemViewRes.findViewById(R.id.username);
//            date = itemViewRes.findViewById(R.id.date);
            msg_rcv = itemViewRes.findViewById(R.id.msg_rcv);
            msg_send = itemViewRes.findViewById(R.id.msg_send);
            img__rcv = itemViewRes.findViewById(R.id.img__rcv);
            img__send = itemViewRes.findViewById(R.id.img__send);

//            click_row = itemViewRes.findViewById(R.id.click_row);
        }
    }
}