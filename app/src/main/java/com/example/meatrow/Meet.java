package com.example.meatrow;

import java.util.Date;
import java.util.List;

public class Meet{
    public int Id;
    public int creator_id;
    public String Avatar;
    public String Name;
    public String Description;
    public Double H_coordinates;
    public Double W_coordinates;
    public String Date;
    public String  Time;
    public Date End_meeting;
    public Date Create_meeting;
    public Integer UserCount;
    public List<String> preferenceTags;
    public List<String> SocialLinks;

    public Meet(){

    };

    public Meet(String avatar, String name, String description, String date, String time, Integer userCount){
        Avatar = avatar;
        Name = name;
        Description = description;
        Date = date;
        Time = time;
        UserCount = userCount;
    }
}
