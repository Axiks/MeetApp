package com.example.meatrow;

import android.content.ClipData;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.IgnoreExtraProperties;

import java.io.Serializable;

public class Activity2 extends AppCompatActivity {
    @IgnoreExtraProperties
    static class Item implements Serializable {
        public String surname;
        public String email;
        public String password;

        public Item() {

        }

        Item(String name, String email, String password){
            this.surname = name;
            this.email = email;
            this.password = password;
        }
    }

    private static final String TAG = "Neko";
    private FirebaseAuth mAuth;
    private FirebaseDatabase database;
    private DatabaseReference myRef;

    EditText email;
    EditText password;
    EditText name;
    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_2);

        //FirebaseApp.initializeApp(this);
        database = FirebaseDatabase.getInstance();
        myRef = database.getReference("Users");
        email = (EditText) findViewById(R.id.editText);
        password = (EditText) findViewById(R.id.editText2);
        name = (EditText) findViewById(R.id.editText3);

        mAuth = FirebaseAuth.getInstance();
    }


    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        // updateUI(currentUser);
    }

    public void btn (View view){
        Log.d(TAG, "btn press");
        Item item = new Item(name.getText().toString(), email.getText().toString(), password.getText().toString());
        Log.d(TAG, item.email );
        myRef.push().setValue(item);
    }

    public void btnSigNew(View view){
        String email = "Art@gmail.com";
        String password = "Password";
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "createUserWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "createUserWithEmail:failure", task.getException());
                            Toast.makeText(Activity2.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }

                        // ...
                    }
                });
    }
}
