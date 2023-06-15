package com.example.teammatefinderxo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.teammatefinderxo.databinding.ActivityMainBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity {
    ActivityMainBinding binding; // creates a binding object for the main activity layout
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());// inflates the main activity layout using the binding object
        setContentView(binding.getRoot()); // sets the content view of the main activity layout to the root view of the binding object
        fragmentChange(new HomeFragment()); // sets the home fragment as the default when the app starts
        binding.bottomNavigationView.setOnItemSelectedListener(item -> {// sets an item selected listener for the bottom navigation view
            switch (item.getItemId()){
                case R.id.home:
                    fragmentChange(new HomeFragment()); // loads the home fragment when the home item is selected
                    break;
                case R.id.profile:
                    fragmentChange(new ProfileFragment()); // loads the profile fragment when the profile item is selected
                    break;
                case R.id.search:
                    fragmentChange(new SearchFragment()); // loads the search fragment when the search item is selected
                    break;
                case R.id.settings:
                    fragmentChange(new SettingsFragment()); // loads the settings fragment when the settings item is selected
                    break;}
            return true; // returns true to indicate that the item selection has been handled
        });
    }
    private void fragmentChange(Fragment fragment){ // a helper method for changing the displayed fragment
        FragmentManager fragmentManager = getSupportFragmentManager();// gets the fragment manager instance
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction(); // begins a new fragment transaction
        fragmentTransaction.replace(R.id.frame_layout,fragment);// replaces the current fragment with the new one
        fragmentTransaction.commit();// commits the transaction
    }
}