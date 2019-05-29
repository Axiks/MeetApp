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
    public FirebaseAuth mAuth;
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

        mAuth = FirebaseAuth.getInstance();

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        myRef = database.getReference("Meets");

        /*User now */
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            uId.setText(user.getUid());
        }
        else {
            uId.setText("Вхід не виконаний");
        }

        /*Meet out*/
        Meet mmm = new Meet( "Na picu q", "Jdemo na picu 1", "2019,07,01,16,00,00","16:00");
        meets.add(mmm);

        ValueEventListener postListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                // Get Post object and use the values to update the UI

                Log.d(TAG ,"Count "+snapshot.getChildrenCount());
                for (DataSnapshot postSnapshot: snapshot.getChildren()) {
                    Meet meet = postSnapshot.getValue(Meet.class);
                    meets.add(meet);
                    Log.d(TAG, "Key: " + postSnapshot.getKey());
                    Log.d(TAG, "Value: " + postSnapshot.getValue(Meet.class).name);
                }

                //Rec View 2
                Log.d(TAG, "MA create new Adapter");
                meetAdapter = new MeetAdapter();
                Log.d(TAG, "MA set Adapter");
                numbersList.setAdapter(meetAdapter);
                Log.d(TAG, "MA loadMeet");
                //meetAdapter.setItems(loadMeet());
                meetAdapter.setItems(meets);

//                Log.d(TAG, "Pre Name start "+ dataSnapshot.getValue(Meet.class).description);
//                Log.d(TAG, "Succres "+dataSnapshot.getValue().toString());
//                Meet meet = new Meet(dataSnapshot.getValue(Meet.class).name, dataSnapshot.getValue(Meet.class).description, "2019,07,04,16,00,00","16:00");
//                Log.d(TAG, "Pre Name end "+ meet.description);
//                meets.add(meet);
//
//                Log.d(TAG, "Pre SIZE "+ meets.size());
//
//                //Meet meet = dataSnapshot.getValue(Meet.class);
//                //meet = new Meet("", "Na shslyk", "Jdemo na shaslyk", "2019,07,04,16,00,00","16:00", 8);
//
//                //Rec View 2
//                Log.d(TAG, "MA create new Adapter");
//                meetAdapter = new MeetAdapter();
//                Log.d(TAG, "MA set Adapter");
//                numbersList.setAdapter(meetAdapter);
//                Log.d(TAG, "MA loadMeet");
//                //meetAdapter.setItems(loadMeet());
//                meetAdapter.setItems(meets);
//
//                Log.d(TAG, "Post SIZE "+ meets.size());
//
//                // ...
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

    private Collection<Meet> loadMeet(){
        Meet m1 = new Meet("Na picu", "Jdemo na picu", "2019,07,01,16,00,00","16:00");
        Meet m2 = new Meet("Na shslyk", "Jdemo na shaslyk", "2019,07,04,16,00,00","16:00");
        Meet m3 = new Meet("Na grecku", "Jdemo grecku", "2019,07,10,16,00,00","16:00");
        meets.add(m2);
        meets.add(m1);
        //meets = Arrays.asList(m1, m2, m3);
        return meets;
    }

    public void testActivity(View view){
        Intent intent = new Intent(this, Activity2.class);
        startActivity(intent);
    }

    public void loginActivity(View view){
        Intent intent = new Intent(this, LoginRegistrateActivity.class);
        startActivity(intent);
    }

    public void meetCreateActivity(View view){
        Intent intent = new Intent(this, MeetCreate.class);
        startActivity(intent);
    }

    public void test(View view){
        Log.d(TAG, "Btn SIZE "+ meets.size());
    }
}
