package com.example.meatrow;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class UserFragment extends Fragment {
    private static final String TAG = "Neko";
    @Nullable
    public View rootView;
    FirebaseUser user;
    User userData;
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_user, null);
        final EditText nameText = rootView.findViewById(R.id.etName);
        final EditText surnameText = rootView.findViewById(R.id.etSurname);
        final EditText descriptionText = rootView.findViewById(R.id.etDen);
        Button LoginButton = (Button) rootView.findViewById(R.id.loginBtn);
        Button LogOutButton = (Button) rootView.findViewById(R.id.logoutBtn);
        Button EditUserButton = rootView.findViewById(R.id.btnUserEdit);
        //User now
        user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            LoginButton.setVisibility(View.GONE);
            LogOutButton.setVisibility(View.VISIBLE);
            renderUserProfile();
            //startActivity(new Intent(getActivity(), MainActivity.class));
        }
        else {
            LoginButton.setVisibility(View.VISIBLE);
            LogOutButton.setVisibility(View.GONE);
//            Intent intent = new Intent(getActivity(), LoginRegistrateActivity.class);
//            startActivity(intent);
        }
        //Button
        LoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), LoginRegistrateActivity.class);
                startActivity(intent);
            }
        });
        LogOutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth.getInstance().signOut();
                //Intent intent = new Intent(getActivity(), LoginRegistrateActivity.class);
                //startActivity(intent);
            }
        });
        EditUserButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseDatabase  database = FirebaseDatabase.getInstance();
                Query userQ = database.getReference("Users").orderByChild("id").equalTo(user.getUid());
                userQ.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for(DataSnapshot edtData: dataSnapshot.getChildren()){

                            edtData.getRef().child("name").setValue(nameText.getText().toString());
                            edtData.getRef().child("surname").setValue(surnameText.getText().toString());
                            edtData.getRef().child("description").setValue(descriptionText.getText().toString());
                        }
                        Toast.makeText(getActivity(),"Data Edited", Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

            }
        });

        return rootView;
    }

    private void renderUserProfile(){
        Log.d(TAG, "renderUserProfile");

        TextView email = rootView.findViewById(R.id.tvEmail);
        email.setText(user.getEmail());

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        Query query = database.getReference("Users").orderByChild("id").equalTo(user.getUid());

        ValueEventListener userListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Log.d(TAG ,"Count "+dataSnapshot.getChildrenCount());

                for (DataSnapshot userSnapshot: dataSnapshot.getChildren()) {
                    userData = userSnapshot.getValue(User.class);
                    Log.d(TAG, "userObj: " + userSnapshot.getValue(User.class).getId());

                    EditText name = rootView.findViewById(R.id.etName);
                    name.setText(userData.getName());

                    EditText surname = rootView.findViewById(R.id.etSurname);
                    surname.setText(userData.getSurname());

//                    EditText age = rootView.findViewById(R.id.etAge);
//                    age.setText(userData.getBirthday());

                    EditText den = rootView.findViewById(R.id.etDen);
                    den.setText(userData.getDescription());
                }

                Log.d(TAG, "user Data Read");
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Getting Post failed, log a message
                Log.d(TAG, "loadPost:onCancelled", databaseError.toException());
                // ...
            }
        };
        query.addValueEventListener(userListener);
    }

}
