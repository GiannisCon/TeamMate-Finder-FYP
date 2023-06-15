package com.example.teammatefinderxo;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

public class SearchFragment extends Fragment {
    Spinner matchGender, matchContinent, matchGame, matchRank;
    Button SearchBtn;
    public SearchFragment() {}// Required empty public constructor
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_search, container, false); // Inflate the layout for this fragment
        // Get references to the Spinner views
        matchGender = (Spinner) view.findViewById(R.id.searchGender);
        matchContinent = (Spinner) view.findViewById(R.id.searchContinent);
        matchGame = (Spinner) view.findViewById(R.id.searchGame);
        matchRank = (Spinner) view.findViewById(R.id.searchGameRank);
        SearchBtn = (Button) view.findViewById(R.id.searchBtn);
        matchGame.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {// Set an OnItemSelectedListener for game spinner
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedItem = (String) parent.getItemAtPosition(position); // Get the selected item from game spinner
                // Update the data source for rank spinner based on the selected item in spinner1
                if (selectedItem.equals("Fifa")) {
                    ArrayAdapter<CharSequence> rankAdapter1 = ArrayAdapter.createFromResource(requireContext(),
                            R.array.FifaRankings, android.R.layout.simple_spinner_dropdown_item);
                    matchRank.setAdapter(rankAdapter1);
                } else if (selectedItem.equals("League Of Legends")) {
                    ArrayAdapter<CharSequence> rankAdapter2 = ArrayAdapter.createFromResource(requireContext(),
                            R.array.LOLRankings, android.R.layout.simple_spinner_dropdown_item);
                    matchRank.setAdapter(rankAdapter2);
                } else if (selectedItem.equals("PUBG")) {
                    ArrayAdapter<CharSequence> rankAdapter3 = ArrayAdapter.createFromResource(requireContext(),
                            R.array.PUBGRankings, android.R.layout.simple_spinner_dropdown_item);
                    matchRank.setAdapter(rankAdapter3);
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Do nothing
            }
        });
        SearchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), SwipeManager.class);// create an intent to start the SwipeManager Activity
                // send search criteria to SwipeManager activity once its started
                intent.putExtra("gender", matchGender.getSelectedItem().toString());
                intent.putExtra("region", matchContinent.getSelectedItem().toString());
                intent.putExtra("game", matchGame.getSelectedItem().toString());
                intent.putExtra("rank", matchRank.getSelectedItem().toString());
                startActivity(intent); // start the SwipeManager with the intent
            }
        });
        return view;
    }
}