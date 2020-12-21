package com.h.itservices.model;

public class Matched {
    private String ID;
    private String expert_ID;
    private String expert_username;
    private String question_Id;
    private double Score;

    public Matched(){}


    public Matched(String ID,String question_Id, String expert_ID,String username, double score) {
        this.ID = ID;
        this.expert_ID = expert_ID;
        this.question_Id = question_Id;
        Score = score;
        expert_username=username;
    }

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public String getExpert_ID() {
        return expert_ID;
    }

    public void setExpert_ID(String expert_ID) {
        this.expert_ID = expert_ID;
    }

    public String getQuestion_Id() {
        return question_Id;
    }

    public void setQuestion_Id(String question_Id) {
        this.question_Id = question_Id;
    }

    public double getScore() {
        return Score;
    }

    public void setScore(double score) {
        Score = score;
    }

    public String getExpert_username() {
        return expert_username;
    }

    public void setExpert_username(String expert_username) {
        this.expert_username = expert_username;
    }
}
