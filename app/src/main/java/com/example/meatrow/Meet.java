package com.example.meatrow;

import android.net.Uri;
import android.support.annotation.NonNull;
import android.util.Log;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.ServerValue;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.io.Serializable;
import java.lang.annotation.Retention;
import java.util.Date;
import java.util.List;

public class Meet implements Serializable {
    public String id;
    public String name;
    public String description;
    public String meetStart;
    public String meetEnd;
    public String creatorId;
    private String avatar;
    private String avatarHref;
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

    public String getAvatar(){
        return avatar;
    }

    public void setAvatar(String ImageUrl){
        this.avatar = ImageUrl;
    }

//    private void avatarToHref(){
//        StorageReference mStorageRef = FirebaseStorage.getInstance().getReference("uploads");
//
//        mStorageRef.child(avatar).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
//            @Override
//            public void onSuccess(Uri uri) {
//                // Got the download URL for "YourFolderName/YourFile.pdf"
//                // Add it to your database
//                avatarHref = uri.toString();
//                Log.d("Mee", "Uri Uri ownload: "+uri.toString());
//
//            }
//        }).addOnFailureListener(new OnFailureListener() {
//            @Override
//            public void onFailure(@NonNull Exception exception) {
//                // Handle any error
//                Log.d("Mee", "Uri Uri ownload: ");
//                avatarHref = null;
//            }
//        });
//    }
}
