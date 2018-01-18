package com.example.nam_t.datingapp.Chat;

/**
 * Created by nam_t on 16-Jan-18.
 */

public class ChatObject {
    private String imageProfileUser;
    private String textMessage;
    private String imageMessageDb;
    private boolean currentUser;
    private boolean imageMessage;

    public String getImageMessageDb() {
        return imageMessageDb;
    }

    public ChatObject(String imageProfileUser, String textMessage, String imageMessageDb, boolean currentUser, boolean imageMessage) {

        this.imageProfileUser = imageProfileUser;
        this.textMessage = textMessage;
        this.imageMessageDb = imageMessageDb;
        this.currentUser = currentUser;
        this.imageMessage = imageMessage;
    }

    //    public ChatObject(String imageProfileUser, String textMessage, boolean currentUser) {
//        this.imageProfileUser = imageProfileUser;
//        this.textMessage = textMessage;
//        this.currentUser = currentUser;
//    }

    public boolean isCurrentUser() {
        return currentUser;
    }

    public String getImageProfileUser() {
        return imageProfileUser;
    }


    public String getTextMessage() {
        return textMessage;
    }

    public boolean isImageMessage() {
        return imageMessage;
    }
}