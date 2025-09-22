package com.example.gatherlink.activity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.gatherlink.R;
import com.example.gatherlink.adapters.GroupAdapter;
import com.example.gatherlink.model.GroupModel;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;

public class ExploreGroupsActivity extends AppCompatActivity {

    private RecyclerView groupsRecyclerView;
    private GroupAdapter groupAdapter;
    private ArrayList<GroupModel> groupList = new ArrayList<>();
    private final HashMap<GroupModel, String> groupIdMap = new HashMap<>();

    private FirebaseFirestore store;
    private Button createGroupButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_explore_groups);

        store = FirebaseFirestore.getInstance();

        groupsRecyclerView = findViewById(R.id.groupsRecyclerView);
        createGroupButton = findViewById(R.id.createGroupButton);

        groupsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        groupAdapter = new GroupAdapter(groupList, false);
        groupsRecyclerView.setAdapter(groupAdapter);

        createGroupButton.setOnClickListener(v -> {
            Intent intent = new Intent(ExploreGroupsActivity.this, CreateGroupActivity.class);
            startActivity(intent);
        });

        fetchGroups();
    }

    private void fetchGroups() {
        store.collection("Groups")
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    groupList.clear();
                    queryDocumentSnapshots.forEach(doc -> {
                        GroupModel group = doc.toObject(GroupModel.class);
                        groupList.add(group);
                    });
                    groupAdapter.notifyDataSetChanged();
                })
                .addOnFailureListener(e -> Toast.makeText(this, "Failed to load groups!", Toast.LENGTH_SHORT).show());
    }
}