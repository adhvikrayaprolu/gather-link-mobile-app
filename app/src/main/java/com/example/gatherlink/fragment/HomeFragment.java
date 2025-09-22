package com.example.gatherlink.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.gatherlink.R;
import com.example.gatherlink.activity.ExploreGroupsActivity;
import com.example.gatherlink.activity.MyGroupsActivity;
import com.example.gatherlink.activity.MyPostsActivity;
import com.example.gatherlink.activity.PostActivity;

public class HomeFragment extends Fragment {
    Button exploreInterestGroupsButton;
    Button myGroupsButton;
    Button createPostButton;
    Button myPostsButton;

    public HomeFragment() {}

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_fragment_home, container, false);

        initializeUI(view);
        setUpOnClickListeners();

        return view;
    }

    private void initializeUI(View view) {
        exploreInterestGroupsButton = view.findViewById(R.id.exploreInterestGroupsButton);
        myGroupsButton = view.findViewById(R.id.myGroupsButton);
        createPostButton = view.findViewById(R.id.createPostButton);
        myPostsButton = view.findViewById(R.id.myPostButton);
    }

    private void setUpOnClickListeners() {
        exploreInterestGroupsButton.setOnClickListener(v -> {
            Intent exploreGroupsIntent = new Intent(getActivity(), ExploreGroupsActivity.class);
            startActivity(exploreGroupsIntent);
        });

        myGroupsButton.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), MyGroupsActivity.class);
            startActivity(intent);
        });

        createPostButton.setOnClickListener(v -> {
            Intent postIntent = new Intent(getActivity(), PostActivity.class);
            startActivity(postIntent);
        });

        myPostsButton.setOnClickListener(v -> {
            Intent myPostsIntent = new Intent(getActivity(), MyPostsActivity.class);
            startActivity(myPostsIntent);
        });

    }
}
