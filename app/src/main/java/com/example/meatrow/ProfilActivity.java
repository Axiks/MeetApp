package com.example.meatrow;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class ProfilActivity extends AppCompatActivity {
    private static final String TAG = "Neko";
    private FirebaseAuth mAuth;

    public TextView email;
    public TextView name;
    public TextView surname;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profil);

        email = (TextView) findViewById(R.id.emailUserView);
        name = (TextView) findViewById(R.id.userNameView);
        surname = (TextView) findViewById(R.id.surnameUserView);

        mAuth = FirebaseAuth.getInstance();
        userInfo();
    }

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
    }

    public void userInfo(){
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {

            //Uri photoUrl = user.getPhotoUrl();

            // Check if user's email is verified
            boolean emailVerified = user.isEmailVerified();

            // The user's ID, unique to the Firebase project. Do NOT use this value to
            // authenticate with your backend server, if you have one. Use
            // FirebaseUser.getIdToken() instead.

            // Name, email address, and profile photo Url
            String Name = user.getDisplayName();
            String Email = user.getEmail();
            String uid = user.getUid();

            email.setText(Email);
            name.setText(Name);
            surname.setText(uid);

            Log.d(TAG, "UId: " + uid);
        }
    }
}
