package com.example.meatrow;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;

import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.IgnoreExtraProperties;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import static android.Manifest.permission.READ_CONTACTS;

public class LoginRegistrateActivity extends AppCompatActivity{
    private static final String TAG = "Neko";

    private FirebaseAuth mAuth;

    public AutoCompleteTextView email;
    public EditText password;
    public Button login;
    public Button register;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_registrate);

        mAuth = FirebaseAuth.getInstance();

        email = (AutoCompleteTextView) findViewById(R.id.email);
        password = (EditText) findViewById(R.id.password);
        register = (Button) findViewById(R.id.email_sign_in_button);
        login = (Button) findViewById(R.id.sign_in);

        // создаем обработчик нажатия
        OnClickListener oclBtnRegister = new OnClickListener() {
            @Override
            public void onClick(View v) {
                // Меняем текст в TextView (tvOut)
                String Email = email.getText().toString();
                String Password = password.getText().toString();
                btnSigNew(Email, Password);
                Log.d(TAG ,"btn Register press");
            }
        };

        OnClickListener oclBtnLogin = new OnClickListener() {
            @Override
            public void onClick(View v) {
                // Меняем текст в TextView (tvOut)
                String Email = email.getText().toString();
                String Password = password.getText().toString();
                signExist(Email, Password);
                Log.d(TAG ,"btn Sign IN press");
            }
        };
        register.setOnClickListener(oclBtnRegister);
        login.setOnClickListener(oclBtnLogin);
    }

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        // updateUI(currentUser);
    }

    public void btnSigNew(String email, String password){

        final Toast toastSuc = Toast.makeText(getApplicationContext(),
                "Реєстрація пройшла успішно", Toast.LENGTH_SHORT);
        final Toast toastErr = Toast.makeText(getApplicationContext(),
                "Помилка реєстрації", Toast.LENGTH_SHORT);

        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "createUserWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();

                            FirebaseDatabase database = FirebaseDatabase.getInstance();
                            DatabaseReference usersDB = database.getReference("Users");
                            User userData = new User(user.getUid(), null, null, null, null);
                            usersDB.push().setValue(userData);
                            Log.d(TAG, "Create help user table");

                            toastSuc.show();
                            userInfo();
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "createUserWithEmail:failure", task.getException());
                            toastErr.show();
                        }

                        // ...
                    }
                });
    }

    public void signExist(String email, String password){
        final Toast toastSuc = Toast.makeText(getApplicationContext(),
                "Вхід виконано", Toast.LENGTH_SHORT);
        final Toast toastErr = Toast.makeText(getApplicationContext(),
                "Помилка входу", Toast.LENGTH_SHORT);
        final Intent intent = new Intent(this, ProfilActivity.class);


        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithEmail:success");
                            //userInfo();
                            FirebaseUser user = mAuth.getCurrentUser();
                            toastSuc.show();
                            startActivity(intent);

                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithEmail:failure", task.getException());
                            toastErr.show();
                        }
                        // ...
                    }
                });
    }

    public void userInfo(){
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            // Name, email address, and profile photo Url
            String name = user.getDisplayName();
            String email = user.getEmail();
            //Uri photoUrl = user.getPhotoUrl();

            // Check if user's email is verified
            boolean emailVerified = user.isEmailVerified();

            // The user's ID, unique to the Firebase project. Do NOT use this value to
            // authenticate with your backend server, if you have one. Use
            // FirebaseUser.getIdToken() instead.
            String uid = user.getUid();
            Log.d(TAG, "UId: " + uid);
        }
    }
}

