package com.vlist.holaenhanced;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.button.MaterialButton;
import com.vlist.holaenhanced.Matches.MatchesFragment;
import com.vlist.holaenhanced.Recommend.RecommendFragment;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link PlaceholderFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PlaceholderFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public PlaceholderFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment PlaceholderFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static PlaceholderFragment newInstance(String param1, String param2) {
        PlaceholderFragment fragment = new PlaceholderFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_placeholder, container, false);

        MaterialButton setting = view.findViewById(R.id.setting_button);
        MaterialButton matches = view.findViewById(R.id.matches_button);
        MaterialButton recommend = view.findViewById(R.id.recommend_button);

        setting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getParentFragmentManager().beginTransaction()
                        .replace(R.id.main_content,new SettingsFragment())
                        .commit();
            }
        });

        matches.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getParentFragmentManager().beginTransaction()
                        .replace(R.id.main_content, new MatchesFragment())
                        .commit();
            }
        });

        recommend.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                getParentFragmentManager().beginTransaction()
                        .replace(R.id.main_content, new RecommendFragment())
                        .commit();
            }
        });
        return view;
    }
}