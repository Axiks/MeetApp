package com.example.meatrow;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment {
    @Nullable
    private static final String TAG = "Neko";
    public RecyclerView recyclerView;
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_home, null);
        // 1. get a reference to recyclerView
        recyclerView = rootView.findViewById(R.id.recView);
        // 2. set layoutManger
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        // this is data fro recycler view
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("Meets");
        ValueEventListener postListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                // Get Post object and use the values to update the UI
                List<Meet> meets = new ArrayList<>();
                Log.d(TAG ,"Count meets"+snapshot.getChildrenCount());
                for (DataSnapshot postSnapshot: snapshot.getChildren()) {
                    Meet meet = postSnapshot.getValue(Meet.class);
                    meet.id = postSnapshot.getKey();
                    meets.add(meet);
                    Log.d(TAG, "Key: " + postSnapshot.getKey());
                    Log.d(TAG, "Value: " + postSnapshot.getValue(Meet.class).name);
                }
                // 3. create an adapter
                Log.d(TAG, "Create Adapter");
                MeetAdapter meetAdapter = new MeetAdapter();
                // 4. set adapter
                Log.d(TAG, "Set Adapter");
                recyclerView.setAdapter(meetAdapter);
                // 5. set item animator to DefaultAnimator
                Log.d(TAG, "Set item animator to DefaultAnimator");
                meetAdapter.setItems(meets);
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Getting Post failed, log a message
                Log.w(TAG, "loadMeets:onCancelled", databaseError.toException());
            }
        };
        myRef.addValueEventListener(postListener);
        return rootView;
    }
}
