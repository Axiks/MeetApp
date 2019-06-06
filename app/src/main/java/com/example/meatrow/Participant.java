package com.example.meatrow;

import java.io.Serializable;

public class Participant implements Serializable {
    private static final String TAG = "Neko";

    public String meetId;
    public String userId;
    private String create_date;

    public String getMeetId(){
        return meetId;
    }

    public void setMeetId(String meetId){
        this.meetId = meetId;
    }

    public String getUserId(){
        return userId;
    }

    public void setUserId(String userId){
        this.userId = userId;
    }

}
