package com.example.gatherlink.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.gatherlink.R;
import com.example.gatherlink.adapters.PostAdapter;
import com.example.gatherlink.model.GroupModel;
import com.example.gatherlink.model.PostsModel;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;

public class GroupPostsActivity extends AppCompatActivity {

    private RecyclerView postsRecyclerView;
    private ProgressBar progressBar;
    private TextView groupTitle, noPostsText;

    private FirebaseFirestore db;
    private GroupModel group;
    private ArrayList<PostsModel> groupPosts;
    private PostAdapter postAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_posts);

        group = (GroupModel) getIntent().getSerializableExtra("groupData");
        db = FirebaseFirestore.getInstance();

        postsRecyclerView = findViewById(R.id.groupPostsRecyclerView);
        progressBar = findViewById(R.id.groupPostsProgressBar);
        groupTitle = findViewById(R.id.groupPostsTitle);
        noPostsText = findViewById(R.id.noPostsText);

        groupTitle.setText("Posts in: " + group.getGroupName());

        groupPosts = new ArrayList<>();
        HashMap<PostsModel, String> postIdMap = new HashMap<>();

        postAdapter = new PostAdapter(this, groupPosts, postIdMap);
        postsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        postsRecyclerView.setAdapter(postAdapter);

        fetchGroupPosts();
    }

    private void fetchGroupPosts() {
        progressBar.setVisibility(View.VISIBLE);

        db.collection("Groups")
                .document(group.getGroupId())
                .collection("Posts")
                .get()
                .addOnSuccessListener(querySnapshot -> {
                    if (querySnapshot.isEmpty()) {
                        noPostsText.setVisibility(View.VISIBLE);
                        progressBar.setVisibility(View.GONE);
                        return;
                    }

                    for (var doc : querySnapshot.getDocuments()) {
                        PostsModel post = doc.toObject(PostsModel.class);
                        groupPosts.add(post);
                    }

                    postAdapter.notifyDataSetChanged();
                    progressBar.setVisibility(View.GONE);
                })
                .addOnFailureListener(e -> {
                    progressBar.setVisibility(View.GONE);
                    noPostsText.setText("Failed to load posts.");
                    noPostsText.setVisibility(View.VISIBLE);
                });
    }
}