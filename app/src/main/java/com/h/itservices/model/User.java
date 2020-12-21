package com.h.itservices.model;

import java.util.ArrayList;

public class User {
    private String user_id;
    private String user_name;
    private String email;
    private String type;
    private ArrayList<String> exp;
    private String password ;
    private String profile_image_name ;
    public User(){ }

    public User(String user_id, String user_name, String password , String email, String type, ArrayList<String> exp, String profile_image_name) {
        this.user_id = user_id;
        this.user_name = user_name;
        this.email = email;
        this.type = type;
        this.password = password;
        this.exp = exp;
        this.profile_image_name = profile_image_name;
    }


    public ArrayList<String> getExp() {
        return exp;
    }

    public void setExp(ArrayList<String> exp) {
        this.exp = exp;
    }
//    public String getExp() {
//        return exp;
//    }
//
//    public void setExp(String  exp) {
//        this.exp = exp;
//    }

    public String getUser_id() {
        return user_id;
    }
    public String getProfile_image_name() {
        return profile_image_name;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }


    public String getPassword() {return password;}
    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
    public void setProfile_image_name(String profile_image_name) {
        this.profile_image_name = profile_image_name;
    }
}
