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
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
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
    public FirebaseUser userAuth;
    public DatabaseReference refMeets;//
    public MeetAdapter participantsAdapter;
    public Meet meet;
    public StorageReference mStorageRef;//
    private Uri mImageUri;//
    public List<Participant> participants = new ArrayList<>();//Учасники клубу Link
    public List<User> users = new ArrayList<>();//Учасники клубу Link

    public FirebaseDatabase database;

    TextView tvCreateDate;
    TextView tvCreator;
    TextView tvDescription;
    TextView tvName;
    TextView tvDateStart;
    TextView tvDateEnd;
    Button btnCome;
    Button btnEdit;
    Button btnDestroy;
    ImageView imageAvatar;
    LinearLayout adminView;
    ListView partLv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meet_profile);

        tvCreateDate = findViewById(R.id.tvCreateDate);
        tvCreator = findViewById(R.id.tvCreator);
        tvDescription = findViewById(R.id.tvDescription);
        tvName = findViewById(R.id.tvName);
        tvDateStart = findViewById(R.id.tvDateStart);
        btnCome = findViewById(R.id.btnCome);
        btnEdit = findViewById(R.id.btnEdit);
        btnDestroy = findViewById(R.id.btnDestroy);
        imageAvatar = findViewById(R.id.imageAvatar);
        partLv = findViewById(R.id.partLV);

        mStorageRef = FirebaseStorage.getInstance().getReference("uploads");

        meet = new Meet();

        adminView =  findViewById(R.id.adminLinear);
        adminView.setVisibility(View.GONE);
        tvCreator.setText("No name");

        Bundle extras = getIntent().getExtras();
        final String meetID = extras.getString("meetId");
        meet.id = meetID;



        userAuth = FirebaseAuth.getInstance().getCurrentUser();

        database = FirebaseDatabase.getInstance();
        refMeets = database.getReference("Meets");
        Query query = database.getReference("Meets").orderByKey().equalTo(meetID);//Id

        btnCome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(userAuth.getUid() != null){
                    createParticipant(userAuth.getUid(), meetID);
                }
            }
        });

        /*Meet out*/
        ValueEventListener meetListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                // Get Post object and use the values to update the UI
                Log.d(TAG ,"Count "+snapshot.getChildrenCount());
                for (DataSnapshot postSnapshot: snapshot.getChildren()) {
                    meet = postSnapshot.getValue(Meet.class);

                    /*Meet Id*/
                    Bundle extras = getIntent().getExtras();
                    meet.id = extras.getString("meetId");

                    if (userAuth.getUid().equals(meet.creatorId)) {
                        adminView.setVisibility(View.VISIBLE);
                    }

                    /*Particicpant*/
                    getParticipant();

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


        btnDestroy.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v) {
                Log.d(TAG ,"BtnDestroy Press ");
                Log.d(TAG ,"User Id " + userAuth.getUid());
                Log.d(TAG ,"Creator meet Id " + meet.creatorId);
                Log.d(TAG ,"Meet Id " + meetID);

                if(userAuth.getUid().equals(meet.creatorId)){
                    Log.d(TAG ,"DestroyOk ");

                    destroyMeet(meetID);
                }
            }
        });
    }

    public void Render(){
        Log.d(TAG, "Rendr " + meet.name);

        tvCreateDate.setText(meet.create_meeting);
        tvName.setText(meet.name);
        tvDescription.setText(meet.description);
        tvDateStart.setText(meet.meetStart);
        Picasso.get().load(meet.getAvatar()).transform(new CircleTransform()).into(imageAvatar);

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        Query userQ = database.getReference("Users").orderByChild("id").equalTo(meet.creatorId);
        ValueEventListener userListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                for (DataSnapshot userSnapshot: dataSnapshot.getChildren()) {
                    User userData = userSnapshot.getValue(User.class);
                    if(userData.getName().equals(null)){
                    }else {
                        tvCreator.setText(userData.getName());
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Getting Post failed, log a message

                // ...
            }
        };
        userQ.addValueEventListener(userListener);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQURET && resultCode == RESULT_OK && resultCode == RESULT_OK
        && data != null && data.getData() != null){
            mImageUri = data.getData(); //patch

            Picasso.get().load(mImageUri).into(imageAvatar);
        }
    }

    public void createParticipant(String userId, String meetId){
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference refParticipant = database.getReference("Participant");
        Participant participant = new Participant();
        participant.setUserId(userId);
        participant.setMeetId(meetId);
        refParticipant.push().setValue(participant);
    }

    public void getParticipant(){
        Log.d(TAG ,"getParti ");
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        Query query = database.getReference("Participant").orderByChild("meetId").equalTo(meet.id);//Запит
        Log.d(TAG ,"getParti Meet Id: " + meet.id);

        ValueEventListener partListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                // Get Post object and use the values to update the UI
                Log.d(TAG ,"Part Count "+snapshot.getChildrenCount());
                for (DataSnapshot partSnapshot: snapshot.getChildren()) {
                    Participant part = partSnapshot.getValue(Participant.class);
                    participants.add(part);
                    Log.d(TAG, "Part User Id: " + part.userId);
                    Log.d(TAG, "Part Meet Id: " + part.meetId);
                    //getUser("eAVHIHBWN4gyYoGqNutsVNq8RSG3");
                }
                Log.d(TAG ,"Part obj Count " + participants.size());
                Log.w(TAG, "Part count: " + participants.size());
//                AdapterPerson adbPerson;
//                ArrayAdapter arrayAdapter = new ArrayAdapter(this, partLv, participants, participants);
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Getting Post failed, log a message
                Log.w(TAG, "loadPost:onCancelled", databaseError.toException());
            }
        };
        query.addValueEventListener(partListener);

    }

    public void destroyMeet(String id){
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        database.getReference("Meets").child(id).removeValue();//Id

        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    //Create date +
    //Autor id create +
    //Avatar +
    //Name +
    //Descr +
    //Map point
    //Start +
    //End +
    //Btn GO +
    //User GO List +

    //if(autor_id == curent_id)
        //Destroy +
        //Edit +

}
