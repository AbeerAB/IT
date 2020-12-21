package com.h.itservices.fragments;

import android.app.ProgressDialog;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.h.itservices.R;
import com.h.itservices.activities.AddQuestionActivity;
import com.h.itservices.adapter.QuestionsAdapter;
import com.h.itservices.model.Answers;
import com.h.itservices.model.Likes;
import com.h.itservices.model.Matched;
import com.h.itservices.model.Questions;

import java.util.ArrayList;
import java.util.Set;

import static android.content.Context.MODE_PRIVATE;

public class All_Home extends Fragment {

    DrawerLayout drawerLayout;
    NavigationView navigationView;
    Toolbar toolbar;
    Menu menu;
    ProgressDialog progressDialog;
    QuestionsAdapter questionsAdapter;
    RecyclerView q_list;
    ArrayList<Questions> questions;
    private FirebaseAuth mAuth;
    public SharedPreferences prefs;
    public static String MY_PREFS_NAME = "itservice";
    public static SharedPreferences.Editor editor;
    ArrayList<String>myExp;
    TextView qa_ampty;
    private SearchView searchView = null;
    private SearchView.OnQueryTextListener queryTextListener;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_my_questions, null, false);
        mAuth = FirebaseAuth.getInstance();


        prefs = getActivity().getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
        editor = prefs.edit();


        progressDialog=new ProgressDialog(getActivity());
        progressDialog.setCancelable(false);
        progressDialog.setMessage("انتظر.....");
        questions=new ArrayList<>();
        q_list=(RecyclerView)view.findViewById(R.id.q_list);
        qa_ampty=(TextView) view.findViewById(R.id.qa_ampty);

        questionsAdapter = new QuestionsAdapter(getActivity(), questions);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        q_list.setLayoutManager(layoutManager);
        q_list.setAdapter(questionsAdapter);



        FloatingActionButton fab = view.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent add = new Intent(getActivity(), AddQuestionActivity.class);
                startActivity(add);
            }
        });

        return view;

    }

    @Override
    public void onResume() {
        super.onResume();
        get_All_questions_list();
    }
    public void get_All_questions_list(){
        progressDialog.show();

        DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference().child("Question");

        rootRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                progressDialog.dismiss();
                questions.clear();

                ArrayList<String>questionsExp=new ArrayList<String>();

                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    ArrayList<Answers>qaAnswers=new ArrayList<Answers>();
                    ArrayList<Likes>likes=new ArrayList<Likes>();
                    ArrayList<Matched>experts=new ArrayList<>();

                    String question_id = (String) ds.child("question_id").getValue();
                    String publisher_id = (String) ds.child("publisher_id").getValue();
                    String publisher_name = (String) ds.child("publisher_name").getValue();
                    String question_title = (String) ds.child("question_title").getValue();
                    String question_text = (String) ds.child("question_text").getValue();
                    String date_time = (String) ds.child("date_time").getValue();
                    String user_type = (String) ds.child("user_type").getValue();
                    String profile_image_name = (String) ds.child("publisher_img").getValue();
                    questionsExp = (ArrayList<String>) ds.child("exp").getValue();


                        for (DataSnapshot ds2 : ds.child("answers").getChildren()) {

                            ArrayList<Likes>likes_answers=new ArrayList<Likes>();

                            String answers_id= ds2.getKey();
                            String qa_id= (String) ds2.child("qa_id").getValue();
                            String publisher_id_an= (String) ds2.child("publisher_id").getValue();
                            String publisher_name_an= (String) ds2.child("publisher_name").getValue();
                            String answers_text= (String) ds2.child("answers_text").getValue();
                            String date_time_an= (String) ds2.child("date_time").getValue();
                            String comment_id_an= (String) ds2.child("question_id").getValue();

                            for (DataSnapshot dslike : ds.child("likes").getChildren()) {
                                Likes like= dslike.getValue(Likes.class);
                                like.setLike_id(dslike.getKey());
                                likes_answers.add(like);

                            }

//                        Answers an = ds.getValue(Answers.class);
                            qaAnswers.add(new Answers(answers_id,qa_id,comment_id_an,publisher_id_an,publisher_name_an,answers_text,date_time_an,likes_answers));

                        }
                        for (DataSnapshot ds2 : ds.child("likes").getChildren()) {
                            Likes like= ds2.getValue(Likes.class);
                            like.setLike_id(ds2.getKey());

                            likes.add(like);

                        }
                    for (DataSnapshot ds3 : ds.child("experts").getChildren()) {
                        Matched match= ds3.getValue(Matched.class);
                        match.setID(ds3.getKey());

                        experts.add(match);

                    }

                        questions.add(new Questions(ds.getKey(), publisher_id, publisher_name, profile_image_name,question_title, question_text, date_time, user_type,
                                questionsExp, qaAnswers,likes,experts));

                }


                if(questions.size()==0){
                    qa_ampty.setVisibility(View.VISIBLE);
                    Toast.makeText(getActivity(), "لا يوجد اسئلة", Toast.LENGTH_SHORT).show();
                }else {
                    qa_ampty.setVisibility(View.GONE);

                }
                questionsAdapter.notifyDataSetChanged();


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menusearch, menu);
        MenuItem searchItem = menu.findItem(R.id.action_search);
        SearchManager searchManager = (SearchManager) getActivity().getSystemService(Context.SEARCH_SERVICE);

        if (searchItem != null) {
            searchView = (SearchView) searchItem.getActionView();
        }
        if (searchView != null) {
            searchView.setSearchableInfo(searchManager.getSearchableInfo(getActivity().getComponentName()));

            queryTextListener = new SearchView.OnQueryTextListener() {
                @Override
                public boolean onQueryTextChange(String newText) {
                    ArrayList<Questions> result = new ArrayList<Questions>();
                    for(Questions item : questions){

                        if(item.getQuestion_title().toLowerCase().contains(newText.toLowerCase())||item.getQuestion_text().toLowerCase().contains(newText.toLowerCase())){
                            result.add(item);
                            questionsAdapter = new QuestionsAdapter(getActivity(), result);
                            LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
                            q_list.setLayoutManager(layoutManager);
                            q_list.setAdapter(questionsAdapter);
                        }
                    }

                    return false;
                }
                @Override
                public boolean onQueryTextSubmit(String query) {

                    return true;
                }
            };
            searchView.setOnQueryTextListener(queryTextListener);
        }
        super.onCreateOptionsMenu(menu, inflater);
    }
}