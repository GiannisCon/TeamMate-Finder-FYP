package com.example.teammatefinderxo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;


public class UsersDetails extends AppCompatActivity {

    private Spinner UserGender, UserContinent, UserFifaRanking, UserPubgRanking, UserLolRanking, UserAvatar;

    private Button finishedButton;

    private EditText UserDiscordUsername;

    private FirebaseUser user;

    private String userID;

    DatabaseReference userDbReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_users_details);

        UserGender =  findViewById(R.id.searchGender);
        UserContinent = findViewById(R.id.searchContinent);
        UserFifaRanking =  findViewById(R.id.FifaRanking);
        UserPubgRanking =  findViewById(R.id.PubgRanking);
        UserLolRanking =  findViewById(R.id.LolRanking);
        UserDiscordUsername = findViewById(R.id.detailsDiscord);
        UserAvatar = findViewById(R.id.avatarPicker);

        finishedButton = findViewById(R.id.insertGamesButton);

        finishedButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                storeUserDetails();
            }
        });

    }

    private void storeUserDetails(){
        // Obtain information from interface
        String Gender = UserGender.getSelectedItem().toString();
        String Continent = UserContinent.getSelectedItem().toString();
        String FifaRanking = UserFifaRanking.getSelectedItem().toString();
        String PubgRanking = UserPubgRanking.getSelectedItem().toString();
        String LolRanking = UserLolRanking.getSelectedItem().toString();
        String DiscordUsername = UserDiscordUsername.getText().toString().trim();
        String Avatar = UserAvatar.getSelectedItem().toString();
        // Store user information as key-value pairs in a HashMap
        HashMap userGames = new HashMap();
        userGames.put("FifaRanking",FifaRanking);
        userGames.put("Gender",Gender);
        userGames.put("LolRanking",LolRanking);
        userGames.put("PubgRanking",PubgRanking);
        userGames.put("continent",Continent);
        userGames.put("DiscordName",DiscordUsername);
        userGames.put("Avatar",Avatar);
        user = FirebaseAuth.getInstance().getCurrentUser(); // get current signed in user
        userID = user.getUid(); // get current users id
        userDbReference = FirebaseDatabase.getInstance().getReference("Users");// get a reference to the current user with the database
        userDbReference.child(userID).updateChildren(userGames).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()){ // Check if the data was successfully uploaded to the database
                    Toast.makeText(UsersDetails.this, "Details updated", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(UsersDetails.this, MainActivity.class));
                }else {
                    Toast.makeText(UsersDetails.this, "Upload failed", Toast.LENGTH_SHORT).show();
                }
            }
        });





    }

}