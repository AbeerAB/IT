package com.h.itservices.model;

public class Likes {

    private String like_id;
    private String qa_id;
    private String publisher_id;
    private boolean like=false;


    public Likes() {
    }
    public Likes(String like_id,String qa_id, String publisher_id,boolean like) {
        this.like_id=like_id;
        this.qa_id=qa_id;
        this.publisher_id=publisher_id;
        this.like=like;
    }


    public String getPublisher_id() {
        return publisher_id;
    }

    public void setPublisher_id(String publisher_id) {
        this.publisher_id = publisher_id;
    }


   public String getLike_id() {
        return like_id;
    }

    public void setLike_id(String like_id) {
        this.like_id = like_id;
    }

    public String getQa_id() {
        return qa_id;
    }
    public void setQa_id(String qa_id) {
        this.qa_id = qa_id;
    }
    public boolean getLike() {
        return like;
    }
    public void setLike(boolean like) {
        this.like = like;
    }


}
