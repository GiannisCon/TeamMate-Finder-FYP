package com.example.teammatefinderxo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


import java.util.ArrayList;
import java.util.Random;

public class SwipeManager extends AppCompatActivity {

    TextView NameTextView, EmailTextView, GameTextView, RankTextView;

    ImageView UsersAvatar;
    FloatingActionButton yesHandler, noHandler;

    static int userCounter = 0;

    private static int[] avatarDrawables = {R.drawable.avatar1, R.drawable.avatar2, R.drawable.avatar3, R.drawable.avatar4, R.drawable.avatar5, R.drawable.avatar6};

    ArrayList<UserMatches> usersList = new ArrayList<>();

    static Boolean userFound = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_swipe_manager);
        // retrieve the search criteria data from the intent
        Intent intent = getIntent();
        String selectedGender = intent.getStringExtra("gender");
        String selectedRegion = intent.getStringExtra("region");
        String selectedGame = intent.getStringExtra("game");
        String selectedRank = intent.getStringExtra("rank");
        NameTextView = findViewById(R.id.SName);
        EmailTextView = findViewById(R.id.SEmail);
        GameTextView = findViewById(R.id.SGame);
        RankTextView = findViewById(R.id.SRank);
        yesHandler = findViewById(R.id.likeBtn);
        noHandler = findViewById(R.id.dislikeBtn);
        UsersAvatar = findViewById(R.id.searchAvatar);
        // currentUserID is the ID of the current user
        String currentUserId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        // getting current users email to avoid showing him in the search
        String currentUserEmail = FirebaseAuth.getInstance().getCurrentUser().getEmail();
        // placing all users and their data into the usersList
        DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference();
        DatabaseReference usersRef = rootRef.child("Users");
        usersRef.get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (task.isSuccessful()) {
                    for (DataSnapshot ds : task.getResult().getChildren()) {
                        String email = ds.child("UserEmail").getValue(String.class);
                        String FullName = ds.child("UserFullName").getValue(String.class);
                        String gender = ds.child("Gender").getValue(String.class);
                        String continent = ds.child("continent").getValue(String.class);
                        String Fifa = ds.child("FifaRanking").getValue(String.class);
                        String Pubg = ds.child("PubgRanking").getValue(String.class);
                        String Lol = ds.child("LolRanking").getValue(String.class);
                        String Uuid = ds.getKey();
                        String Discord = ds.child("DiscordName").getValue(String.class);
                        String profilePic = ds.child("Avatar").getValue(String.class);
                        usersList.add(new UserMatches(FullName, email, gender, continent, Fifa, Pubg, Lol, Uuid, Discord, profilePic));
                    }
                    while (!userFound) {
                        if (userCounter>=usersList.size()){
                            Toast.makeText(SwipeManager.this, "No more users", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(SwipeManager.this, MainActivity.class));
                            userCounter=0;
                            break;
                        }
                        else if (usersList.get(userCounter).continent.equals(selectedRegion) && usersList.get(userCounter).Gender.equals(selectedGender) &&
                                selectedGame.equals("Fifa") && selectedRank.equals(usersList.get(userCounter).FifaRanking) && !usersList.get(userCounter).UserEmail.equals(currentUserEmail)) {
                            NameTextView.setText(usersList.get(userCounter).UserFullName);
                            EmailTextView.setText(usersList.get(userCounter).UserEmail);
                            GameTextView.setText(selectedGame);
                            RankTextView.setText(usersList.get(userCounter).FifaRanking);
                            UsersAvatar.setImageResource(avatarDrawables[Integer.parseInt(usersList.get(userCounter).Avatar)-1]);
                            userFound = true;
                        } else if (usersList.get(userCounter).continent.equals(selectedRegion) && usersList.get(userCounter).Gender.equals(selectedGender) &&
                                selectedGame.equals("League Of Legends") && selectedRank.equals(usersList.get(userCounter).LolRanking)  && !usersList.get(userCounter).UserEmail.equals(currentUserEmail)) {
                            NameTextView.setText(usersList.get(userCounter).UserFullName);
                            EmailTextView.setText(usersList.get(userCounter).UserEmail);
                            GameTextView.setText(selectedGame);
                            RankTextView.setText(usersList.get(userCounter).LolRanking);
                            UsersAvatar.setImageResource(avatarDrawables[Integer.parseInt(usersList.get(userCounter).Avatar)-1]);
                            userFound = true;
                        } else if (usersList.get(userCounter).continent.equals(selectedRegion) && usersList.get(userCounter).Gender.equals(selectedGender) &&
                                selectedGame.equals("PUBG") && selectedRank.equals(usersList.get(userCounter).PubgRanking) && !usersList.get(userCounter).UserEmail.equals(currentUserEmail)) {
                            NameTextView.setText(usersList.get(userCounter).UserFullName);
                            EmailTextView.setText(usersList.get(userCounter).UserEmail);
                            GameTextView.setText(selectedGame);
                            RankTextView.setText(usersList.get(userCounter).PubgRanking);
                            UsersAvatar.setImageResource(avatarDrawables[Integer.parseInt(usersList.get(userCounter).Avatar)-1]);
                            userFound = true;
                        } else {
                            userCounter++;
                        }
                    }
                    yesHandler.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            String matchId = usersList.get(userCounter).Uuid;
                            if (userFound=true) {
                                // Get a reference to the userMatches path within the current user
                                DatabaseReference userMatchesRef = usersRef.child(currentUserId).child("userMatches").child(matchId);
                                // adds the match into the logged In User database
                                userMatchesRef.setValue(matchId).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        // getting a reference to the matched users database
                                        DatabaseReference OtherUserMatchRef = usersRef.child(matchId).child("userMatches").child(currentUserId);
                                        OtherUserMatchRef.setValue(currentUserId);
                                    }
                                });
                                userFound=false;
                                userCounter++;}
                            // function that finds the next user matching the criteria
                            findNextUser();
                        }});
                    noHandler.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            if(userFound=true){
                                userFound=false;
                                userCounter++;
                            }
                            findNextUser();
                        }
                    });
                } else {
                    Toast.makeText(SwipeManager.this, "Failed,try again", Toast.LENGTH_SHORT).show();
                }
            }
            //This method finds the next user in the list that matches the selected criteria
            private void findNextUser() {
                // Check if we have reached the end of the user list
                if (userCounter >= usersList.size()) {
                    Toast.makeText(SwipeManager.this, "No more users", Toast.LENGTH_SHORT).show();
                    userCounter=0;
                    startActivity(new Intent(SwipeManager.this, MainActivity.class));
                }
                // Check if the current user matches the selected criteria for FIFA
                else if (usersList.get(userCounter).continent.equals(selectedRegion) && usersList.get(userCounter).Gender.equals(selectedGender) && selectedGame.equals("Fifa") && selectedRank.equals(usersList.get(userCounter).FifaRanking) && !usersList.get(userCounter).UserEmail.equals(currentUserEmail)) {
                    // Display the user's information
                    NameTextView.setText(usersList.get(userCounter).UserFullName);
                    EmailTextView.setText(usersList.get(userCounter).UserEmail);
                    GameTextView.setText(selectedGame);
                    RankTextView.setText(usersList.get(userCounter).FifaRanking);
                    UsersAvatar.setImageResource(avatarDrawables[Integer.parseInt(usersList.get(userCounter).Avatar)-1]);
                    userFound=true;
                }
                // Check if the current user matches the selected criteria for League of Legends
                else if (usersList.get(userCounter).continent.equals(selectedRegion) && usersList.get(userCounter).Gender.equals(selectedGender) && selectedGame.equals("League Of Legends") && selectedRank.equals(usersList.get(userCounter).LolRanking) && !usersList.get(userCounter).UserEmail.equals(currentUserEmail)) {
                    // Display the user's information
                    NameTextView.setText(usersList.get(userCounter).UserFullName);
                    EmailTextView.setText(usersList.get(userCounter).UserEmail);
                    GameTextView.setText(selectedGame);
                    RankTextView.setText(usersList.get(userCounter).LolRanking);
                    UsersAvatar.setImageResource(avatarDrawables[Integer.parseInt(usersList.get(userCounter).Avatar)-1]);
                    userFound=true;
                }
                // Check if the current user matches the selected criteria for PUBG
                else if (usersList.get(userCounter).continent.equals(selectedRegion) && usersList.get(userCounter).Gender.equals(selectedGender) && selectedGame.equals("PUBG") && selectedRank.equals(usersList.get(userCounter).PubgRanking) && !usersList.get(userCounter).UserEmail.equals(currentUserEmail)) {
                    // Display the user's information
                    NameTextView.setText(usersList.get(userCounter).UserFullName);
                    EmailTextView.setText(usersList.get(userCounter).UserEmail);
                    GameTextView.setText(selectedGame);
                    RankTextView.setText(usersList.get(userCounter).PubgRanking);
                    UsersAvatar.setImageResource(avatarDrawables[Integer.parseInt(usersList.get(userCounter).Avatar)-1]);
                    userFound=true;
                }
                // If the user doesn't match any of the criteria, keep looking for the next user that matches
                else  { userFound=false;
                    //keep looking for a until you find one or you run out of users
                    while (!userFound) {
                        //check if we've gone through all the users in the list, show a message and go back to the main activity
                        if (userCounter>=usersList.size()){
                            Toast.makeText(SwipeManager.this, "No more users", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(SwipeManager.this, MainActivity.class));
                            userCounter=0;
                            break;
                        }
                        // Check if the current user matches the selected criteria for FIFA
                        else if (usersList.get(userCounter).continent.equals(selectedRegion) && usersList.get(userCounter).Gender.equals(selectedGender) && selectedGame.equals("Fifa") && selectedRank.equals(usersList.get(userCounter).FifaRanking) && !usersList.get(userCounter).UserEmail.equals(currentUserEmail)) {
                            // Display the user's information
                            NameTextView.setText(usersList.get(userCounter).UserFullName);
                            EmailTextView.setText(usersList.get(userCounter).UserEmail);
                            GameTextView.setText(selectedGame);
                            RankTextView.setText(usersList.get(userCounter).FifaRanking);
                            UsersAvatar.setImageResource(avatarDrawables[Integer.parseInt(usersList.get(userCounter).Avatar)-1]);
                            userFound = true;
                        }
                        // Check if the current user matches the selected criteria for League of Legends
                        else if (usersList.get(userCounter).continent.equals(selectedRegion) && usersList.get(userCounter).Gender.equals(selectedGender) && selectedGame.equals("League Of Legends") && selectedRank.equals(usersList.get(userCounter).LolRanking) && !usersList.get(userCounter).UserEmail.equals(currentUserEmail)) {
                            // Display the user's information
                            NameTextView.setText(usersList.get(userCounter).UserFullName);
                            EmailTextView.setText(usersList.get(userCounter).UserEmail);
                            GameTextView.setText(selectedGame);
                            RankTextView.setText(usersList.get(userCounter).LolRanking);
                            UsersAvatar.setImageResource(avatarDrawables[Integer.parseInt(usersList.get(userCounter).Avatar)-1]);
                            userFound = true;
                        }
                        // Check if the current user matches the selected criteria for PUBG
                        else if (usersList.get(userCounter).continent.equals(selectedRegion) && usersList.get(userCounter).Gender.equals(selectedGender) && selectedGame.equals("PUBG") && selectedRank.equals(usersList.get(userCounter).PubgRanking) && !usersList.get(userCounter).UserEmail.equals(currentUserEmail)) {
                            // Display the user's information
                            NameTextView.setText(usersList.get(userCounter).UserFullName);
                            EmailTextView.setText(usersList.get(userCounter).UserEmail);
                            GameTextView.setText(selectedGame);
                            RankTextView.setText(usersList.get(userCounter).PubgRanking);
                            UsersAvatar.setImageResource(avatarDrawables[Integer.parseInt(usersList.get(userCounter).Avatar)-1]);
                            userFound = true;
                        }
                        // if the current user doesn't match the criteria go to the next one
                        else {
                            userCounter++;
                        }
                    }
                }
            }
        });
    }
}
