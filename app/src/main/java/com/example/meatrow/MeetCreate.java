package com.example.meatrow;

import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.io.Serializable;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class MeetCreate extends AppCompatActivity {
    private static final  int PICK_IMAGE_REQURET = 1;
    private static final String TAG = "Neko";
    private FirebaseAuth mAuth;
    public FirebaseUser curUser;
    private FirebaseDatabase database;
    private DatabaseReference myRef;
    public StorageReference mStorageRef;//Image Files Store
    private Uri mImageUri;//
    public String imgAvatarHref;


    static Meet meet;

    EditText Name;
    EditText Description;
    EditText startTime;
    EditText endTime;
    CalendarView MeetStartData;
    ImageView ImageAvatar;
    Button BtnLoadAvatar;
    Button MeetCreate;

    TimePickerDialog timePickerDialog;

    String name;
    String description;
    String meetStart;
    String meetEnd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meet_create);

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN); //Auto Open Keyboard none

        Name = (EditText) findViewById(R.id.name);
        Description = findViewById(R.id.description);
        MeetStartData = (CalendarView) findViewById(R.id.meetStart);
        BtnLoadAvatar = (Button) findViewById(R.id.btnLoadAvatar);
        ImageAvatar = (ImageView) findViewById(R.id.imageAvatar);
        startTime = findViewById(R.id.meetStartTime);
        endTime = findViewById(R.id.meetStartTime);

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        myRef = database.getReference("Meets");

        mStorageRef = FirebaseStorage.getInstance().getReference("uploads");

        imgAvatarHref = null;


        /*User now */
        curUser = FirebaseAuth.getInstance().getCurrentUser();
        if (curUser != null) {
            //curUser.getUid();
        }
        else {
            Intent intent = new Intent(this, LoginRegistrateActivity.class);
            startActivity(intent);
            //uId.setText("Вхід не виконаний");
        }

        startTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               timePickerDialog = new TimePickerDialog(MeetCreate.this, new TimePickerDialog.OnTimeSetListener() {
                    public void onTimeSet(TimePicker timePicker, int hourOfDay, int minutes) {
                        startTime.setText(hourOfDay+":"+minutes);
                    }
                }, 0, 0, true);
               timePickerDialog.show();
            }
        });

        MeetStartData.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {

            @Override
            public void onSelectedDayChange(CalendarView view, int year,
                                            int month, int dayOfMonth) {
                int mYear = year;
                int mMonth = month;
                int mDay = dayOfMonth;
                meetStart = new StringBuilder().append(mDay).append("/").append(mMonth + 1)
                        .append("/").append(mYear)
                        .append(" ").toString();
            }
        });

        BtnLoadAvatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openFileChhooser();
            }
        });
    }

    //Image Uploaader
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQURET && resultCode == RESULT_OK && resultCode == RESULT_OK
                && data != null && data.getData() != null){
            mImageUri = data.getData(); //patch
            Picasso.get().load(mImageUri).into(ImageAvatar);

            //uploadImage(); //Upload Img to Server
        }
    }

    //update user info
    private String uploadImage(){
        if(mImageUri != null){
            final ProgressDialog progressDialog = new ProgressDialog(this);
            progressDialog.setTitle("Upload...");
            progressDialog.show();

            String ImgName = "images/" + UUID.randomUUID().toString();
            final StorageReference reference = mStorageRef.child(ImgName);
            reference.putFile(mImageUri)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            Toast.makeText(MeetCreate.this, "Upload succeful", Toast.LENGTH_SHORT).show();
                            progressDialog.cancel();

                            //taskSnapshot.getUploadSessionUri();
                            Log.d(TAG, "MC TaskSn gUSU Avatar href: " + taskSnapshot.getUploadSessionUri());
                            Log.d(TAG, "MC TaskSn gmgp Avatar href: " + taskSnapshot.getMetadata().getName());

                            reference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    imgAvatarHref = uri.toString();
                                    Log.d(TAG, "MC TaskSn func Avatar href: " + uri.toString());
                                    meetCretor(uri.toString());
                                }
                            });

                            //reference.child("users/me/profile.png").getDownloadUrl().getResult();
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(MeetCreate.this, "Unsuccessful uploads", Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                            double progress = (100.0*taskSnapshot.getBytesTransferred()/taskSnapshot.getTotalByteCount());
                            progressDialog.setMessage("Upload "+(int)progress+"%");
                        }
                    });
                return ImgName;
        }else {
            Toast.makeText(this, "No file seelcted", Toast.LENGTH_SHORT).show();
            meetCretor(null);
            return null;
        }
    }

    public void openFileChhooser(){
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, PICK_IMAGE_REQURET);
    }

    public void btnCreate(View view){

        //meet.setAvatarHref(imgAvatarHref);
        uploadImage();
    }

    private void meetCretor(String avatarSrc){
        meetStart = meetStart + " " + startTime.getText();

        name = Name.getText().toString();
        description = Description.getText().toString();
        meet = new Meet(curUser.getUid(), name, description, meetStart, meetEnd);

        Date date = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss");
        String strDate = formatter.format(date);
        meet.create_meeting = strDate;


       // String serverAvaSrc = uploadImage(); //Upload Img to Server
        meet.setAvatar(avatarSrc);

        myRef.push().setValue(meet);

        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

}