package com.example.gatherlink.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.gatherlink.R;
import com.example.gatherlink.adapters.GroupAdapter;
import com.example.gatherlink.model.GroupModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

public class MyGroupsActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private ProgressBar progressBar;
    private TextView noGroupsText;

    private final FirebaseFirestore db = FirebaseFirestore.getInstance();
    private final String currentUserId = FirebaseAuth.getInstance().getCurrentUser().getUid();

    private final ArrayList<GroupModel> myGroups = new ArrayList<>();
    private GroupAdapter groupAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_groups);

        recyclerView = findViewById(R.id.myGroupsRecyclerView);
        progressBar = findViewById(R.id.myGroupsProgressBar);
        noGroupsText = findViewById(R.id.noGroupsText);

        groupAdapter = new GroupAdapter(myGroups, true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(groupAdapter);

        fetchUserGroups();
    }

    private void fetchUserGroups() {
        progressBar.setVisibility(View.VISIBLE);
        db.collection("GroupMemberships")
                .whereEqualTo("userId", currentUserId)
                .get()
                .addOnSuccessListener(membershipSnapshot -> {
                    if (membershipSnapshot.isEmpty()) {
                        progressBar.setVisibility(View.GONE);
                        noGroupsText.setVisibility(View.VISIBLE);
                        return;
                    }

                    for (DocumentSnapshot doc : membershipSnapshot) {
                        String groupId = doc.getString("groupId");
                        if (groupId != null) {
                            db.collection("Groups")
                                    .document(groupId)
                                    .get()
                                    .addOnSuccessListener(groupDoc -> {
                                        GroupModel group = groupDoc.toObject(GroupModel.class);
                                        if (group != null) {
                                            group.setGroupId(groupDoc.getId());
                                            myGroups.add(group);
                                            groupAdapter.notifyDataSetChanged();
                                        }
                                        progressBar.setVisibility(View.GONE);
                                    });
                        }
                    }
                })
                .addOnFailureListener(e -> {
                    progressBar.setVisibility(View.GONE);
                    noGroupsText.setText("Failed to load groups.");
                    noGroupsText.setVisibility(View.VISIBLE);
                });
    }
}