package com.example.teammatefinderxo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;

public class RegistrationActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText fullName, regEmail, password1, password2;

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
        mAuth = FirebaseAuth.getInstance();

        Button register = findViewById(R.id.registerButton);
        register.setOnClickListener(this);

        fullName = findViewById(R.id.UserFullName);
        regEmail = findViewById(R.id.RegEmail);
        password1 = findViewById(R.id.RegPassword1);
        password2 = findViewById(R.id.RegPassword2);

    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.registerButton) {
            register();
        }

    }

    private void register() {
        String email = regEmail.getText().toString().trim();
        String Password1 = password1.getText().toString().trim();
        String Password2 = password2.getText().toString().trim();
        String FullName = fullName.getText().toString().trim();

        if (FullName.isEmpty()){
            fullName.setError("Full name is required");
            fullName.requestFocus();
            return;
        }
        if (email.isEmpty()){
            regEmail.setError("Email is required");
            regEmail.requestFocus();
            return;
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            regEmail.setError("Please provide valid email");
            regEmail.requestFocus();
            return;
        }
        if (Password1.isEmpty()){
            password1.setError("password is required");
            password1.requestFocus();
            return;
        }
        if (Password2.isEmpty()) {
            password2.setError("password is required");
            password2.requestFocus();
            return;
        }
        if (!Password1.equals(Password2)){
            password2.setError("passwords don't match");
            password2.requestFocus();
            return;
        }

        if (Password1.length()< 6 ||Password2.length() < 6 ){
            password1.setError("Password minimum length is 6 characters");
            password1.requestFocus();
            password2.setError("Password minimum length is 6 characters");
            password2.requestFocus();
            return;
        }
        mAuth.createUserWithEmailAndPassword(email,Password1) //create a new user account with the given email and password
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() { // add a listener to check if the task is completed
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) { // check if the task is successful
                        if (task.isSuccessful()){
                            // create a new user object with the given FullName and email
                            User user = new User(FullName,email,"","","","","","","");
                            FirebaseDatabase.getInstance().getReference("Users") // get a reference to the "Users" node in the Firebase Realtime Database
                                    // set the value of the child node with the current user's UID to the user object created earlier
                                    .child(FirebaseAuth.getInstance().getCurrentUser().getUid()).setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) { // add a listener to check if the data is saved successfully
                                            if (task.isSuccessful()){
                                                Toast.makeText(RegistrationActivity.this, "User has been registered successfully", Toast.LENGTH_SHORT).show();
                                                // sign in the newly registered user
                                                mAuth.signInWithEmailAndPassword(email,password1.getText().toString()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<AuthResult> task) { // add a listener to check if the task is completed

                                                        if (task.isSuccessful()){
                                                            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                                                            startActivity(new Intent(RegistrationActivity.this, UsersDetails.class)); // start the UsersDetails activity
                                                        }
                                                    }
                                                });
                                            }else {
                                                Toast.makeText(RegistrationActivity.this, "Failed to register", Toast.LENGTH_SHORT).show(); // display an error message
                                            }
                                        }
                                    });
                        }else {
                            Toast.makeText(RegistrationActivity.this, "Failed to register", Toast.LENGTH_SHORT).show(); // display an error message
                        }
                    }
                });

    }
}