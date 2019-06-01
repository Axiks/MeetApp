package com.example.meatrow;

import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.nio.file.FileStore;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class MeetProfile extends AppCompatActivity {
    private static final  int PICK_IMAGE_REQURET = 1;
    private static final String TAG = "Neko";
    public FirebaseAuth uAuth;
    public DatabaseReference refMeets;//
    public MeetAdapter participantsAdapter;
    public Meet meet;
    public StorageReference mStorageRef;//
    private Uri mImageUri;//

    TextView tvCreateDate;
    TextView tvCreator;
    TextView tvDescription;
    TextView tvName;
    TextView tvDateStart;
    TextView tvDateEnd;
    Button btnCome;
    Button btnEdit;
    Button btnDestroy;
    Button btnUpload;
    ImageView imageAvatar;
    RecyclerView recTeams;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meet_profile);

        tvCreateDate = findViewById(R.id.tvCreateDate);
        tvCreator = findViewById(R.id.tvCreator);
        tvDescription = findViewById(R.id.tvDescription);
        tvName = findViewById(R.id.tvName);
        tvDateStart = findViewById(R.id.tvDateStart);
        tvDateEnd = findViewById(R.id.tvDateEnd);
        btnCome = findViewById(R.id.btnCome);
        btnEdit = findViewById(R.id.btnEdit);
        btnDestroy = findViewById(R.id.btnDestroy);
        btnUpload = findViewById(R.id.btnUpload);
        imageAvatar = findViewById(R.id.imageAvatar);
        recTeams = findViewById(R.id.recTeams);

        mStorageRef = FirebaseStorage.getInstance().getReference("uploads");

        Bundle extras = getIntent().getExtras();
        String meetID = extras.getString("meetId");

        meet = new Meet();

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);//Виводимо у вигляді списку
        recTeams.setLayoutManager(layoutManager);

        uAuth = FirebaseAuth.getInstance();

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        refMeets = database.getReference("Meets");
        Query query = database.getReference("Meets").orderByKey().equalTo(meetID);//Id

        /*Meet out*/
        ValueEventListener meetListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                // Get Post object and use the values to update the UI
                Log.d(TAG ,"Count "+snapshot.getChildrenCount());
                for (DataSnapshot postSnapshot: snapshot.getChildren()) {
                    meet = postSnapshot.getValue(Meet.class);
                    Log.d(TAG, "Keyyy: " + postSnapshot.getKey());
                    Log.d(TAG, "Valueeee: " + postSnapshot.getValue(Meet.class).name);
                    Log.d(TAG, "meetObj: " + meet.name);
                }
                Render();
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Getting Post failed, log a message
                Log.w(TAG, "loadPost:onCancelled", databaseError.toException());
            }
        };
        //refMeets.addValueEventListener(meetListener);
        query.addValueEventListener(meetListener);

        btnUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openFileChhooser();
            }
        });
    }

    public void Render(){
        Log.d(TAG, "Rendr " + meet.name);
        tvCreator.setText(meet.creatorId);
        tvCreateDate.setText(meet.create_meeting);
        tvName.setText(meet.name);
        tvDescription.setText(meet.description);
        tvDateStart.setText(meet.meetStart);
        tvDateEnd.setText(meet.meetEnd);
        Picasso.get().load(meet.getAvatar()).into(imageAvatar);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQURET && resultCode == RESULT_OK && resultCode == RESULT_OK
        && data != null && data.getData() != null){
            mImageUri = data.getData(); //patch

            Picasso.get().load(mImageUri).into(imageAvatar);

            uploadImage(); //Upload Img to Server
        }
    }

    //update user info
    private void uploadImage(){
        if(mImageUri != null){
            final ProgressDialog progressDialog = new ProgressDialog(this);
            progressDialog.setTitle("Upload...");
            progressDialog.show();

            StorageReference reference = mStorageRef.child("images/" + UUID.randomUUID().toString());
            reference.putFile(mImageUri)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            Toast.makeText(MeetProfile.this, "Upload succeful", Toast.LENGTH_SHORT).show();
                            progressDialog.cancel();

                            //Добавлення записі об фотці в бд
                        }
                    })
            .addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(MeetProfile.this, "Unsuccessful uploads", Toast.LENGTH_SHORT).show();
                }
            })
            .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                    double progress = (100.0*taskSnapshot.getBytesTransferred()/taskSnapshot.getTotalByteCount());
                    progressDialog.setMessage("Upload "+(int)progress+"%");
                }
            })
            ;
        }else {
            Toast.makeText(this, "No file seelcted", Toast.LENGTH_SHORT).show();
        }
    }

    public void openFileChhooser(){
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, PICK_IMAGE_REQURET);
    }

    //Create date +
    //Autor id create +
    //Avatar +
    //Name +
    //Descr +
    //Map point
    //Start +
    //End +
    //Btn GO
    //User GO

    //if(autor_id == curent_id)
        //Destroy
        //Edit

}
