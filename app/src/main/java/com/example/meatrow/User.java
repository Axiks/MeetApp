package com.example.meatrow;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class User {
    int Id;
    private String name;
    private String surname;
    private String description;
    private Date birthday;
    private String avatar_src;
    private List<String>  preferenceTags;
    private List<String> socialLinks;

    User(){
        preferenceTags = new ArrayList<>();
        socialLinks = new ArrayList<>();
    }

    User(String name, String surname, String description, Date birthday){
        setName(name);
        setSurname(surname);
        setDescription(description);
        setBirthday(birthday);
        setAvatar_src("http://i.imgur.com/JcJwS6g.jpg");
        preferenceTags = new ArrayList<>();
        socialLinks = new ArrayList<>();
    }

    public  Integer getId(){
        return Id;
    }
    public String getName ( )
    {
        return name;
    }

    public void setName (String userName)
    {
        name = userName;
    }

    public String getSurname ( )
    {
        return surname;
    }

    public void setSurname (String userSurname)
    {
        surname = userSurname;
    }

    public String getDescription ( )
    {
        return description;
    }

    public void setDescription (String userDescription )
    {
        description = userDescription ;
    }

    public String getAvatar_src ( )
    {
        return avatar_src;
    }

    public void setAvatar_src (String userAvatar_src)
    {
        avatar_src = userAvatar_src;
    }

    public Date getBirthday ( )
    {
        return birthday;
    }

    public void setBirthday (Date userBirthday)
    {
        birthday = userBirthday;
    }

    public List<String>  getPreferenceTags ( )
    {
        return preferenceTags;
    }

    public void setPreferenceTags (List<String> userPreferenceTags)
    {
        preferenceTags = userPreferenceTags;
    }

    public void setPreferenceTags (String userPreferenceTag)
    {
        preferenceTags.add(userPreferenceTag);
    }

    public List<String>  getSocialLinks ( )
    {
        return socialLinks;
    }

    public void setSocialLinks (List<String> userSocialLinks)
    {
        socialLinks = userSocialLinks;
    }

    public void setSocialLinks (String userSocialLink)
    {
        socialLinks.add(userSocialLink);
    }
}
