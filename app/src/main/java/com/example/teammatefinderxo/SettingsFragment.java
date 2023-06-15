package com.example.teammatefinderxo;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;

public class SettingsFragment extends Fragment {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_settings, container, false);
        // Find the logout button using its id
        Button Logout = view.findViewById(R.id.logoutButton);
        // Set an on click listener on the logout button
        Logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Sign out the current user
                FirebaseAuth.getInstance().signOut();
                // Create an Intent to launch the sign-in activity
                Intent intent = new Intent(getActivity(), SignInActivity.class);
                // Start the activity
                startActivity(intent);
            }
        });
        // Return the inflated view
        return view;
    }
}
