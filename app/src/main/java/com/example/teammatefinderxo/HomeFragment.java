package com.example.teammatefinderxo;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;


public class HomeFragment extends Fragment {

    public HomeFragment() {
        // Required empty public constructor
    }

    ListView matchesList;
    private FirebaseUser user;

    private DatabaseReference reference;

    // gets user's database id so that i can locate the usermatches
    private static String userID;

    private static ArrayList<UserMatches> matches = new ArrayList<>();

    private static ArrayList<String> matchesUuid = new ArrayList<>();

    private static int[] avatarDrawables = {R.drawable.avatar1, R.drawable.avatar2, R.drawable.avatar3, R.drawable.avatar4, R.drawable.avatar5, R.drawable.avatar6};


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        user = FirebaseAuth.getInstance().getCurrentUser();
        userID = user.getUid();
        reference = FirebaseDatabase.getInstance().getReference("Users").child(userID).child("userMatches");
        reference.get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (task.isSuccessful()) {
                    matchesUuid.clear(); // Clear the existing data from the matchesUuid ArrayList to avoid data duplication
                    for (DataSnapshot ds : task.getResult().getChildren()) {
                        String Uuid = ds.getKey();
                        matchesUuid.add(Uuid);
                    }
                    DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference();
                    DatabaseReference usersRef = rootRef.child("Users");
                    usersRef.get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DataSnapshot> task) {
                            if (task.isSuccessful()) {
                                matches.clear(); // Clear the existing data from the matches ArrayList to avoid data duplication
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
                                    for (String id : matchesUuid) {
                                        if (Uuid.equals(id)) {
                                            matches.add(new UserMatches(FullName, email, gender, continent, Fifa, Pubg, Lol, Uuid, Discord, profilePic));
                                        }
                                    }
                                }

                                matchesList = view.findViewById(R.id.matchesList);
                                MyAdapter adapter = new MyAdapter(getActivity(), matches);
                                matchesList.setAdapter(adapter);
                            }
                        }
                    });

                } else {
                    Toast.makeText(getActivity(), "Failed,try again", Toast.LENGTH_SHORT).show();
                }
            }
        });
        return view;
    }

    private static class MyAdapter extends ArrayAdapter<UserMatches> {
        public MyAdapter(Context context, ArrayList<UserMatches> matches) {
            super(context, R.layout.matches, matches);
        }
        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            if (convertView == null) {
                convertView = LayoutInflater.from(getContext()).inflate(R.layout.matches, parent, false);
            }
            TextView name = convertView.findViewById(R.id.matchName);
            ImageView image = convertView.findViewById(R.id.profilePic);
            Button deleteButton = convertView.findViewById(R.id.deleteButton);
            TextView desc = convertView.findViewById(R.id.matchDescription);
            UserMatches currentMatch = matches.get(position);
            name.setText(currentMatch.UserFullName);
            String description = "Gender: " + currentMatch.Gender + "\n" +
                    "Region: " + currentMatch.continent + "\n" +
                    "Discord Username: " + currentMatch.DiscordName + "\n" +
                    "Email: " + currentMatch.UserEmail + "\n" +
                    "Fifa Ranking: " + currentMatch.FifaRanking + "\n" +
                    "Pubg Ranking: " + currentMatch.PubgRanking + "\n" +
                    "Lol Ranking: " + currentMatch.LolRanking;
            desc.setText(description);
            // Set the avatar image based on the profilePic value
            int avatarIndex = Integer.parseInt(currentMatch.Avatar) - 1; // Subtract 1 because array index starts at 0
            image.setImageResource(avatarDrawables[avatarIndex]);
            // Handle delete button click
            deleteButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    DatabaseReference matchRef = FirebaseDatabase.getInstance().getReference("Users")
                            .child(userID).child("userMatches").child(currentMatch.Uuid);
                    matchRef.removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                Toast.makeText(getContext(), "Match deleted successfully", Toast.LENGTH_SHORT).show();
                                // Remove the deleted match from the ArrayList and notify the adapter
                                matches.remove(currentMatch);
                                notifyDataSetChanged();
                            } else {
                                Toast.makeText(getContext(), "Failed to delete match", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            });

            return convertView;
        }
    }

}