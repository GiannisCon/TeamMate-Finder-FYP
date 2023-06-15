package com.example.teammatefinderxo;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


public class ProfileFragment extends Fragment {
    public ProfileFragment(){
        //required empty constructor
    }
    private FirebaseUser user;
    private static int[] avatarDrawables = {R.drawable.avatar1, R.drawable.avatar2, R.drawable.avatar3, R.drawable.avatar4, R.drawable.avatar5, R.drawable.avatar6};
    TextView userFullName, userEmailAddress, userGender, userContinent, userFifaRanking, userLolRanking, userPubgRanking, userDiscordName;
    ImageView profilePicture;
    Button rankingChangeBtn;
    private DatabaseReference reference;
    private String userID;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) { // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_profile, container, false); // Get current user and user ID
        user = FirebaseAuth.getInstance().getCurrentUser();
        userID = user.getUid(); // Get reference to Users data in Firebase database
        reference = FirebaseDatabase.getInstance().getReference("Users").child(userID);
        // Add a ValueEventListener to the reference to update UI whenever the data changes
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                // get references to the TextViews, ImageView, and Button in the layout
                userFullName =  view.findViewById(R.id.ProfileUsername);
                userEmailAddress =  view.findViewById(R.id.ProfileEmail);
                userGender =  view.findViewById(R.id.profileGender);
                userContinent =  view.findViewById(R.id.profileContinent);
                userFifaRanking =  view.findViewById(R.id.ProfileFifa);
                userLolRanking =  view.findViewById(R.id.ProfileLol);
                userPubgRanking =  view.findViewById(R.id.ProfilePubg);
                rankingChangeBtn =  view.findViewById(R.id.changeRankingBtn);
                userDiscordName = view.findViewById(R.id.ProfileDiscord);
                profilePicture = view.findViewById(R.id.usersProfPic);
                // retrieve user data from the database
                String name = snapshot.child("UserFullName").getValue().toString();
                String email = snapshot.child("UserEmail").getValue().toString();
                String gender = snapshot.child("Gender").getValue().toString();
                String continent = snapshot.child("continent").getValue().toString();
                String Fifa = snapshot.child("FifaRanking").getValue().toString();
                String Lol = snapshot.child("LolRanking").getValue().toString();
                String Pubg = snapshot.child("PubgRanking").getValue().toString();
                String Discord = snapshot.child("DiscordName").getValue().toString();
                int Avatar = Integer.parseInt(snapshot.child("Avatar").getValue().toString());
                // set the TextViews, ImageView, and Button to display the retrieved user data
                userFullName.setText(name);
                userEmailAddress.setText(email);
                userGender.setText(gender);
                userContinent.setText(continent);
                userFifaRanking.setText(Fifa);
                userLolRanking.setText(Lol);
                userPubgRanking.setText(Pubg);
                userDiscordName.setText(Discord);
                profilePicture.setImageResource(avatarDrawables[Avatar-1]); // Subtract 1 because array index starts at 0
                // set OnClickListener on the rankingChangeBtn to start UsersDetails activity
                rankingChangeBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        startActivity(new Intent(getActivity(), UsersDetails.class));
                    }
                });
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getActivity(), "Something went wrong", Toast.LENGTH_SHORT).show();
            }
        });
        return view;
    }
}