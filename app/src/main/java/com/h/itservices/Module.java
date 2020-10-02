package com.h.itservices;

public class Module {
    private String user_id;
    private String user_name;
    private String email;
    private String type;
    private String other;
    private String exp;
    private String password ;

    public Module(String user_id, String user_name,  String password ,String email, String type, String exp,String other) {
        this.user_id = user_id;
        this.user_name = user_name;
        this.email = email;
        this.type = type;
        this.password = password;
        this.exp = exp;
        this.other = other;
    }

    public String getOther() {
        return other;
    }

    public void setOther(String other) {
        this.other = other;
    }

    public String getExp() {
        return exp;
    }

    public void setExp(String exp) {
        this.exp = exp;
    }

    public String getUser_id() {
        return user_id;
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
}
