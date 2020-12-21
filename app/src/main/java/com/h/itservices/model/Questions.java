package com.h.itservices.model;

import java.util.ArrayList;

public class Questions {
    private String question_id;
    private String publisher_id;
    private String publisher_name;
    private String publisher_img;
    private String question_title;
    private String question_text;
    private String date_time;
    private String user_type;
    private ArrayList<String> exp;
    private ArrayList<Answers> answers;
    private ArrayList<Likes> likes;
    private ArrayList<Matched> experts;


    public Questions() { }

    public Questions(String question_id, String publisher_id, String publisher_name, String publisher_img, String question_title, String question_text, String date_time, String user_type, ArrayList<String> exp, ArrayList<Answers> answers, ArrayList<Likes> likes, ArrayList<Matched> experts) {
        this.question_id = question_id;
        this.publisher_id = publisher_id;
        this.publisher_name = publisher_name;
        this.publisher_img = publisher_img;
        this.question_title = question_title;
        this.question_text = question_text;
        this.date_time = date_time;
        this.user_type = user_type;
        this.exp = exp;
        this.answers = answers;
        this.likes = likes;
        this.experts = experts;
    }

    public String getQuestion_id() {
        return question_id;
    }

    public void setQuestion_id(String question_id) {
        this.question_id = question_id;
    }

    public String getPublisher_id() {
        return publisher_id;
    }

    public void setPublisher_id(String publisher_id) {
        this.publisher_id = publisher_id;
    }

    public String getPublisher_name() {
        return publisher_name;
    }

    public void setPublisher_name(String publisher_name) {
        this.publisher_name = publisher_name;
    }

    public String getPublisher_img() {
        return publisher_img;
    }

    public void setPublisher_img(String publisher_img) {
        this.publisher_img = publisher_img;
    }

    public String getQuestion_title() {
        return question_title;
    }

    public void setQuestion_title(String question_title) {
        this.question_title = question_title;
    }

    public String getQuestion_text() {
        return question_text;
    }

    public void setQuestion_text(String question_text) {
        this.question_text = question_text;
    }

    public String getDate_time() {
        return date_time;
    }

    public void setDate_time(String date_time) {
        this.date_time = date_time;
    }

    public String getUser_type() {
        return user_type;
    }

    public void setUser_type(String user_type) {
        this.user_type = user_type;
    }

    public ArrayList<String> getExp() {
        return exp;
    }

    public void setExp(ArrayList<String> exp) {
        this.exp = exp;
    }

    public ArrayList<Answers> getAnswers() {
        return answers;
    }

    public void setAnswers(ArrayList<Answers> answers) {
        this.answers = answers;
    }

    public ArrayList<Likes> getLikes() {
        return likes;
    }

    public void setLikes(ArrayList<Likes> likes) {
        this.likes = likes;
    }

    public ArrayList<Matched> getExperts() {
        return experts;
    }

    public void setExperts(ArrayList<Matched> experts) {
        this.experts = experts;
    }
}
