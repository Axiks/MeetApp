package com.example.meatrow;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.jar.Attributes;

public class MainActivity extends AppCompatActivity {
    public MeetAdapter meetAdapter;
    public DatabaseReference myRef;
    public FirebaseDatabase database;
    public RecyclerView numbersList;

    TextView uId;
    private static final String TAG = "Neko";
    public List<Meet> meets = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        uId = (TextView) findViewById(R.id.mainUid);
        /*Rec View*/
        numbersList = (RecyclerView) findViewById(R.id.recView);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);//Виводимо у вигляді списку
        numbersList.setLayoutManager(layoutManager);

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        myRef = database.getReference("Meets");

        /*User now */
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            uId.setText(user.getUid());
        }
        else {
            Intent intent = new Intent(this, LoginRegistrateActivity.class);
            startActivity(intent);
            //uId.setText("Вхід не виконаний");
        }

        /*Meet out*/
        ValueEventListener postListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                // Get Post object and use the values to update the UI

                Log.d(TAG ,"Count "+snapshot.getChildrenCount());
                for (DataSnapshot postSnapshot: snapshot.getChildren()) {
                    Meet meet = postSnapshot.getValue(Meet.class);
                    meet.id = postSnapshot.getKey();
                    meets.add(meet);
                    Log.d(TAG, "Key: " + postSnapshot.getKey());
                    Log.d(TAG, "Value: " + postSnapshot.getValue(Meet.class).name);
                }

                //RecView
                Log.d(TAG, "MA create new Adapter");
                meetAdapter = new MeetAdapter();
                Log.d(TAG, "MA set Adapter");
                numbersList.setAdapter(meetAdapter);
                Log.d(TAG, "MA loadMeet");
                //meetAdapter.setItems(loadMeet());
                meetAdapter.setItems(meets);
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Getting Post failed, log a message
                Log.w(TAG, "loadPost:onCancelled", databaseError.toException());
                // ...
            }
        };
        myRef.addValueEventListener(postListener);

        Log.d(TAG, "Program Launch");
    }

    public void loginActivity(View view){
        Intent intent = new Intent(this, LoginRegistrateActivity.class);
        startActivity(intent);
    }

    public void meetCreateActivity(View view){
        Intent intent = new Intent(this, MeetCreate.class);
        startActivity(intent);
    }

}
