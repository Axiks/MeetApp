package com.example.meatrow;

import java.sql.DataTruncation;

public class Upload {
    private String mName;
    private String mImageUrl;

    public Upload(){
        //empty
    }

    public Upload(String name, String imageUrl){

        if(name.trim().equals("")){
            name = "NoName";
        }
        mName = name;
        imageUrl = imageUrl;
    }

    public String getmName(){
        return mName;
    }

    public void setmName(String name){
        mName = name;
    }

    public String getImageUri(){
        return mImageUrl;
    }

    public void srtImageUri(String imageUrl){
        mImageUrl = imageUrl;
    }

}
