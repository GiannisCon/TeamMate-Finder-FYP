package com.example.teammatefinderxo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class SignInActivity extends AppCompatActivity implements View.OnClickListener {
    private TextView register, forgotPassword;
    private EditText Email, Password;
    private Button signIn;

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signin);

        register =  findViewById(R.id.registartion_button);
        register.setOnClickListener(this);

        signIn =  findViewById(R.id.signinbutton);
        signIn.setOnClickListener(this);

        Email =  findViewById(R.id.email_input);
        Password =  findViewById(R.id.password_input);

        mAuth = FirebaseAuth.getInstance();

        forgotPassword =  findViewById(R.id.ForgotPass);
        forgotPassword.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch(view.getId()){
            case R.id.registartion_button:
                startActivity(new Intent(this, RegistrationActivity.class));
                break;
            case R.id.signinbutton:
                userLogin();
                break;
            case R.id.ForgotPass:
                startActivity(new Intent(this,ForgotPasswordActivity.class));
                break;
        }
    }

    private void userLogin() {
        String email = Email.getText().toString().trim();// Get email input from user and trim whitespace
        String password = Password.getText().toString().trim();// Get password input from user and trim whitespace
        if (email.isEmpty()){ // Validate email input
            Email.setError("Email is required");
            Email.requestFocus();
            return;
        }if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()){ // make sure an email is entered
            Email.setError("Please enter a valid email!");
            Email.requestFocus();
            return;
        }if (password.isEmpty()){ // Validate password input
            Password.setError("Password is required");
            Password.requestFocus();
            return;
        }if (password.length()< 6){ // Validate password input
            Password.setError("Password minimum length is 6 characters");
            Password.requestFocus();
            return;
        }mAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() { // Authenticate user with Firebase Authentication
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()){ // Check if authentication was successful
                    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser(); // Get current user
                    if (user.isEmailVerified()) { // Check if user's email is verified
                        startActivity(new Intent(SignInActivity.this, MainActivity.class)); // Start MainActivity
                    }else {
                        user.sendEmailVerification(); // Send email verification to user
                        Toast.makeText(SignInActivity.this, "Check your email fo verification link", Toast.LENGTH_SHORT).show();
                    }
                }else{
                    Toast.makeText(SignInActivity.this, "Failed to login! Please check your credentials", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}