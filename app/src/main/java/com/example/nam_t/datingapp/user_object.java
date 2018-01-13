package com.example.nam_t.datingapp;

/**
 * Created by nam_t on 13-Jan-18.
 */

public class user_object {
    private String user_name;
    private int user_age;
    private String user_pic;
    public user_object (String user_name,int user_age,String user_pic){
        this.user_name=user_name;
        this.user_age=user_age;
        this.user_pic=user_pic;
    }
    public String getUser_name(){
        return user_name;
    }
    public void setUser_name(String user_name){
        this.user_name=user_name;
    }
    public int getUser_age(){
        return user_age;
    }
    public void setUser_age(int user_age){
        this.user_age=user_age;
    }
    public String getUser_pic(){
        return user_pic;
    }
    public void setUser_pic(String user_pic){
        this.user_pic=user_pic;
    }
}
