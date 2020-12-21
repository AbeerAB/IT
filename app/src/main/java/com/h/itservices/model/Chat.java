package com.h.itservices.model;

public class Chat {

    private String user_id;
    private String exp_id;
    private String user_name;
    private String message;
    private String date_time;
    private String img_name;




    public Chat() {
    }
    public Chat(String user_id, String exp_id, String user_name,String message,String date_time,String img_name) {
        this.user_id=user_id;
        this.exp_id=exp_id;
        this.user_name=user_name;
        this.message=message;
        this.exp_id=exp_id;
        this.date_time=date_time;
        this.img_name=img_name;
    }


    public String getUser_id() {
        return user_id;
    }
    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }
    public String getUser_name() {
        return user_name;
    }
    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public String getExp_id() {
        return exp_id;
    }
    public void setExp_id(String exp_id) {
        this.exp_id = exp_id;
    }


    public String getMessage() {
        return message;
    }
    public void setMessage(String message) {
        this.message = message;
    }


    public String getDate_time() {
        return date_time;
    }

    public void setDate_time(String date_time) {
        this.date_time = date_time;
    }

    public String getImg_name() {
        return img_name;
    }

    public void setImg_name(String img_name) {
        this.img_name = img_name;
    }



}
