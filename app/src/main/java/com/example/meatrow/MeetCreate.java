package com.example.meatrow;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;

import java.io.Serializable;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class MeetCreate extends AppCompatActivity {
    private static final String TAG = "Neko";
    private FirebaseAuth mAuth;
    public FirebaseUser curUser;
    private FirebaseDatabase database;
    private DatabaseReference myRef;

    static Meet meet;

    EditText Name;
    EditText Description;
    CalendarView MeetStartData;
    CalendarView MeetEndData;
    Button MeetCreate;

    String name;
    String description;
    String meetStart;
    String meetEnd;

//    static class Meet implements Serializable {
//        public String name;
//        public String description;
//        public String meetStart;
//        public String meetEnd;
//
//        public Meet() {
//
//        }
//
//        Meet(String name, String description, String meetStart, String meetEnd){
//            this.name = name;
//            this.description = description;
//            this.meetStart = meetStart;
//            this.meetEnd = meetEnd;
//        }
//    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meet_create);

        Name = (EditText) findViewById(R.id.name);
        Description = (EditText) findViewById(R.id.description);
        MeetStartData = (CalendarView) findViewById(R.id.meetStart);
        MeetEndData = (CalendarView) findViewById(R.id.meetEnd);

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        myRef = database.getReference("Meets");

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

        MeetEndData.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {

            @Override
            public void onSelectedDayChange(CalendarView view, int year,
                                            int month, int dayOfMonth) {
                int mYear = year;
                int mMonth = month;
                int mDay = dayOfMonth;
                meetEnd = new StringBuilder().append(mDay).append("/").append(mMonth + 1)
                        .append("/").append(mYear)
                        .append(" ").toString();
            }
        });
    }

    public void btnCreate(View view){
        name = Name.getText().toString();
        description = Description.getText().toString();

        meet = new Meet(curUser.getUid(), name, description, meetStart, meetEnd);

        Date date = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss");
        String strDate = formatter.format(date);
        meet.create_meeting = strDate;

        myRef.push().setValue(meet);

        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

}