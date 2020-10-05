package com.h.itservices;

public class Post {


    private String postKey;
    private String title;
    private String description;
    private String userId;
    private String Date;

    public Post(String title, String description, String userId, String date, String time, String publisher) {
        this.title = title;
        this.description = description;
        this.userId = userId;
        Date = date;
        Time = time;
        this.publisher = publisher;
    }

    // make sure to have an empty constructor inside ur model class
    public Post() {
    }


    public String getPostKey() {
        return postKey;
    }

    public void setPostKey(String postKey) {
        this.postKey = postKey;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getDate() {
        return Date;
    }

    public void setDate(String date) {
        Date = date;
    }

    public String getTime() {
        return Time;
    }

    public void setTime(String time) {
        Time = time;
    }

    public String getPublisher() {
        return publisher;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }

    private String Time;
    private String publisher;


        public Post(String title, String description,  String userId) {
            this.title = title;
            this.description = description;
            //this.picture = picture;
            this.userId = userId;

        }





}
