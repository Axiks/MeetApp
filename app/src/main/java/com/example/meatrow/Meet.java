package com.example.meatrow;

import com.google.firebase.database.ServerValue;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

public class Meet implements Serializable {
    public String id;
    public String name;
    public String description;
    public String meetStart;
    public String meetEnd;
    public String creatorId;
    //public String avatar;
    //public Double H_coordinates;
    //public Double W_coordinates;
    public String create_meeting;
    //public Integer userCount;
    //public List<String> preferenceTags;
    //public List<String> SocialLinks;

    public Meet(){

    }

    public Meet(String name, String description, String meetStart, String meetEnd){
        this.name = name;
        this.description = description;
        this.meetStart = meetStart;
        this.meetEnd = meetEnd;
    }

    public Meet(String creatorId, String name, String description, String meetStart, String meetEnd){
        this.creatorId = creatorId;
        this.name = name;
        this.description = description;
        this.meetStart = meetStart;
        this.meetEnd = meetEnd;
    }
}
