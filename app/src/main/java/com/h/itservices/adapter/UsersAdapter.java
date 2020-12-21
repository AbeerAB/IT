package com.h.itservices.adapter;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.h.itservices.R;
import com.h.itservices.model.User;


import java.util.List;


public class UsersAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context context;
    private LayoutInflater inflater;
    List<User> users;
    MyHolder myHolder;
    ProgressDialog progressDialog;
    private DatabaseReference myRef ;
    private FirebaseDatabase database;
    public UsersAdapter(Context context, List<User> users) {
        this.context = context;
        inflater = LayoutInflater.from(context);
        this.users = users;

        progressDialog=new ProgressDialog(context);
        progressDialog.setCancelable(false);
        progressDialog.setMessage(context.getResources().getString(R.string.msg_login));

        database = FirebaseDatabase.getInstance();
        myRef = database.getReference();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.row_user, parent, false);
        MyHolder holder = new MyHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        final User user = users.get(position);

        myHolder = (MyHolder) holder;

        myHolder.user_name.setText(user.getUser_name());
        myHolder.email.setText(user.getEmail());

        myHolder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog myQuittingDialogBox = new AlertDialog.Builder(context)
                        .setTitle("الحذف")
                        .setMessage("هل متأكد من حذف المستخدم ؟")

                        .setPositiveButton("نعم", new DialogInterface.OnClickListener() {

                            public void onClick(DialogInterface dialog2, int whichButton) {

                                progressDialog.show();

                                DatabaseReference remove = FirebaseDatabase.getInstance().getReference()
                                        .child("User").child(user.getUser_id());

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
        return users.size();
    }

    class MyHolder extends RecyclerView.ViewHolder {

        TextView user_name,email;
        ImageView delete;

        public MyHolder(View itemViewRes) {
            super(itemViewRes);
            user_name = itemViewRes.findViewById(R.id.user_name);
            email = itemViewRes.findViewById(R.id.email);
            delete = itemViewRes.findViewById(R.id.delete);
        }
    }



}