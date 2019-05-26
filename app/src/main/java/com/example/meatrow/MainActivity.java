package com.example.meatrow;

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

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.jar.Attributes;

public class MainActivity extends AppCompatActivity {
    private  NumbersAdapter numbersAdapter;
    private MeetAdapter meetAdapter;

    TextView TvId;
    TextView TvName;
    TextView TvSurname;
    EditText EtName;
    EditText EtSurname;
    ListView clubsList;
    RecyclerView numbersList;

    String ids;
    String names;
    String surnames;

    private  zapros Zapros1;
    private static final String TAG = "Neko";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //TvId = (TextView) findViewById(R.id.textView2);
        TvName = (TextView) findViewById(R.id.textView);
        TvSurname = (TextView) findViewById(R.id.textView3);
        //EtName = (EditText) findViewById(R.id.editText);
        //EtSurname = (EditText) findViewById(R.id.editText2);

        //Rec View
        numbersList = (RecyclerView) findViewById(R.id.recView);//<=
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);//Виводимо у вигляді списку
        numbersList.setLayoutManager(layoutManager);
        numbersList.setHasFixedSize(true);

        numbersAdapter = new NumbersAdapter(100);
        //numbersList.setAdapter(numbersAdapter);

        //Rec View 2
        Log.d(TAG, "MA create new Adapter");
        meetAdapter = new MeetAdapter();
        Log.d(TAG, "MA set Adapter");
        numbersList.setAdapter(meetAdapter);
        Log.d(TAG, "MA loadMeet");
        meetAdapter.setItems(loadMeet());

        Log.d(TAG, "Program Launch");
    }

    private Collection<Meet> loadMeet(){
        Meet m1 = new Meet("", "Na picu", "Jdemo na picu", "2019,07,01,16,00,00","16:00", 5);
        Meet m2 = new Meet("", "Na shslyk", "Jdemo na shaslyk", "2019,07,04,16,00,00","16:00", 8);
        Meet m3 = new Meet("", "Na grecku", "Jdemo grecku", "2019,07,10,16,00,00","16:00", 5);
        Collection<Meet> meets = Arrays.asList(m1, m2, m3);
        return meets;
    }

    public void onClick(View view){
        Log.d(TAG, "The button is pressed");

        //ids = TvName.getText().toString();
        //names = TvSurname.getText().toString();
        //surnames = TvSurname.getText().toString();

        //Виконуємо запрос на сервер
        Zapros1 = new zapros();
        Zapros1.start(ids);
        try {
            Zapros1.join();
            Log.d(TAG, "Query Success");
        }catch (InterruptedException ie){
            Log.d(TAG, "Query Error");
            Log.e(TAG, ie.getMessage());
        }

        names = Zapros1.resname();
        surnames = Zapros1.ressurname();

        TvName.setText(names);
        TvSurname.setText(surnames);

        //clubsList = (ListView) findViewById(R.id.meatsList);

//        List<String> list = Zapros1.getClubs();
//        String[] datas = list.toArray(new String[0]);
//
//        //String[] datas = {"choto1", "choto2"};
//        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, datas);
//        clubsList.setAdapter(adapter);
    }
}
