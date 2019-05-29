package com.example.meatrow;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class MeetProfile extends AppCompatActivity {
    private static final String TAG = "Neko";
    public FirebaseAuth uAuth;
    public DatabaseReference refMeets;
    public MeetAdapter meetAdapter;
    public Meet meet;

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
        imageAvatar = findViewById(R.id.imageAvatar);
        recTeams = findViewById(R.id.recTeams);

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
    }

    public void Render(){
        Log.d(TAG, "Rendr " + meet.name);

        tvName.setText(meet.name);
        tvDescription.setText(meet.description);
        tvDateStart.setText(meet.meetStart);
        tvDateEnd.setText(meet.meetEnd);
    }

    //Create data
    //Autor id create
    //Avatar
    //Name
    //Descr
    //Map point
    //Start
    //End
    //Btn GO
    //User GO

    //if(autor_id == curent_id)
        //Destroy
        //Edit

}
