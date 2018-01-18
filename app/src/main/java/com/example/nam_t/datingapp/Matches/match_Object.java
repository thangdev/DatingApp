package com.example.nam_t.datingapp.Matches;

/**
 * Created by nam_t on 16-Jan-18.
 */

public class match_Object {
    private String userId;
    private String name;
    private String profileImageUrl;
    public match_Object (String userId, String name, String profileImageUrl){
        this.userId = userId;
        this.name = name;
        this.profileImageUrl = profileImageUrl;
    }

    public String getUserId(){
        return userId;
    }
    public void setUserID(String userID){
        this.userId = userId;
    }

    public String getName(){
        return name;
    }
    public void setName(String name){
        this.name = name;
    }

    public String getProfileImageUrl(){
        return profileImageUrl;
    }

}
