package com.example.nam_t.datingapp.Users;

/**
 * Created by nam_t on 13-Jan-18.
 */

public class user_object {
    private String userID;
    private String user_name;
    private String user_age;
    private String user_ProfileImgURL;
    public user_object(String userID, String user_name, String user_age, String user_ProfileImgURL){
        this.userID=userID;
        this.user_name=user_name;
        this.user_age=user_age;
        this.user_ProfileImgURL=user_ProfileImgURL;
    }
    public String getUserID(){ return userID;}
    public void setUserID(String userID){this.userID=userID;}

    public String getUser_name(){
        return user_name;
    }
    public void setUser_name(String user_name){
        this.user_name=user_name;
    }

    public String getUser_age(){
        return user_age;
    }
    public void setUser_age(String user_age){
        this.user_age=user_age;
    }

    public String getUser_ProfileImgURL(){
        return user_ProfileImgURL;
    }
    public void setUser_ProfileImgURL(String user_ProfileImgURL){
        this.user_ProfileImgURL=user_ProfileImgURL;
    }
}
