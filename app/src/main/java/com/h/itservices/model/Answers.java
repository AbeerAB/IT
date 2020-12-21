package com.h.itservices.model;

import java.util.ArrayList;

public class Answers {

    private String answers_id;
    private String qa_id;
    private String question_id;
    private String publisher_id;
    private String publisher_name;
    private String answers_text;
    private String date_time;
    private ArrayList<Likes> likes;




    public Answers() {
    }
    public Answers(String answers_id,String qa_id,String question_id,String publisher_id, String publisher_name,String answers_text,String date_time,ArrayList<Likes> likes) {
        this.answers_id=answers_id;
        this.qa_id=qa_id;
        this.question_id=question_id;
        this.publisher_id=publisher_id;
        this.publisher_name=publisher_name;
        this.answers_text=answers_text;
        this.date_time=date_time;
        this.likes=likes;
    }


    public String getPublisher_id() {
        return publisher_id;
    }

    public void setPublisher_id(String publisher_id) {
        this.publisher_id = publisher_id;
    }
    public String getAnswers_id() {
        return answers_id;
    }

    public void setAnswers_id(String answers_id) {
        this.answers_id = answers_id;
    }


       public String getQa_id() {
        return qa_id;
    }

    public void setQa_id(String qa_id) {
        this.qa_id = qa_id;
    }

  public String getQuestion_id() {
        return question_id;
    }

    public void setQuestion_id(String comment_id) {
        this.question_id = comment_id;
    }


   public String getPublisher_name() {
        return publisher_name;
    }

    public void setPublisher_name(String publisher_name) {
        this.publisher_name = publisher_name;
    }


    public String getDate_time() {
        return date_time;
    }

    public void setDate_time(String date_time) {
        this.date_time = date_time;
    }

    public String getAnswers_text() {
        return answers_text;
    }

    public void setAnswers_text(String answers_text) {
        this.answers_text = answers_text;
    }
    public ArrayList<Likes> getLikes() {
        return likes;
    }
    public void setLikes(ArrayList<Likes> likes) {
        this.likes = likes;
    }

}
