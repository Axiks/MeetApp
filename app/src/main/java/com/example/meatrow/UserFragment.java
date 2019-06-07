package com.example.meatrow;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class UserFragment extends Fragment {
    @Nullable
    public View rootView;
    FirebaseUser user;
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_user, null);
        Button LoginButton = (Button) rootView.findViewById(R.id.loginBtn);
        Button LogOutButton = (Button) rootView.findViewById(R.id.logoutBtn);
        //User now
        TextView uId =  rootView.findViewById(R.id.mainUid);
        user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            uId.setText(user.getUid());
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
        Button NewMeetBtn = (Button) rootView.findViewById(R.id.meetCreateBtn);
        NewMeetBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), MeetCreate.class);
                startActivity(intent);
            }
        });
        return rootView;
    }

    private void renderUserProfile(){
        TextView email = rootView.findViewById(R.id.tvEmail);
        email.setText(user.getEmail());
    }
}
