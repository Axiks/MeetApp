package com.example.meatrow;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

public class Meet implements Serializable {
    //public String avatar;
    public String name;
    public String description;
    public String meetStart;
    public String meetEnd;
    //public Double H_coordinates;
    //public Double W_coordinates;
    //public Date end_meeting;
    //public Date create_meeting;
    //public Integer userCount;
    //public List<String> preferenceTags;
    //public List<String> SocialLinks;

    public Meet(){

    };

    public Meet(String name, String description, String meetStart, String meetEnd){
        this.name = name;
        this.description = description;
        this.meetStart = meetStart;
        this.meetEnd = meetEnd;
    }
}
