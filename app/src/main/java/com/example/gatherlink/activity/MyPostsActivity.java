package com.example.gatherlink.activity;

import static com.example.gatherlink.adapters.PostAdapter.KEY_INDEX;
import static com.example.gatherlink.adapters.PostAdapter.KEY_POST;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.gatherlink.R;
import com.example.gatherlink.adapters.PostAdapter;
import com.example.gatherlink.model.PostsModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;

public class MyPostsActivity extends AppCompatActivity {

    public static final int REQUEST_CODE_VIEW_DETAILS = 10;

    private RecyclerView recyclerView;
    private PostAdapter postAdapter;

    private final ArrayList<PostsModel> myPosts = new ArrayList<>();
    private final HashMap<PostsModel, String> postIdMap = new HashMap<>();

    private FirebaseFirestore store;
    private FirebaseUser currentUser;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_posts);

        initializeFirebase();
        setUpRecyclerView();
        fetchUserPosts();
    }

    private void initializeFirebase() {
        currentUser = FirebaseAuth.getInstance().getCurrentUser();
        store = FirebaseFirestore.getInstance();
    }

    private void setUpRecyclerView() {
        recyclerView = findViewById(R.id.myPostsRecyclerView);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));

        postAdapter = new PostAdapter(this, myPosts, postIdMap);
        recyclerView.setAdapter(postAdapter);
    }

    private void fetchUserPosts() {
        store.collection("Groups")
                .get()
                .addOnSuccessListener(groupsSnapshot -> {
                    for (DocumentSnapshot groupDoc : groupsSnapshot.getDocuments()) {
                        String groupId = groupDoc.getId();
                        String groupName = groupDoc.getString("groupName");
                        String groupDescription = groupDoc.getString("groupDescription");

                        store.collection("Groups")
                                .document(groupId)
                                .collection("Posts")
                                .whereEqualTo("email", currentUser.getEmail())
                                .get()
                                .addOnSuccessListener(postsSnapshot -> {
                                    for (DocumentSnapshot postDoc : postsSnapshot.getDocuments()) {
                                        PostsModel post = postDoc.toObject(PostsModel.class);
                                        if (post != null) {
                                            post.setPostId(postDoc.getId());
                                            post.setGroupId(groupId);

                                            post.setGroupName(groupName);
                                            post.setGroupDescription(groupDescription);

                                            myPosts.add(post);
                                            postIdMap.put(post, post.getPostId());
                                        }
                                    }
                                    postAdapter.notifyDataSetChanged();
                                });
                    }
                });
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if ((requestCode == REQUEST_CODE_VIEW_DETAILS) && (resultCode == 1) && (data != null)) {
            Bundle bundle = data.getExtras();
            int postIndex = bundle.getInt(KEY_INDEX);

            if (bundle.containsKey(KEY_POST)) {
                PostsModel updatedPost = (PostsModel) bundle.getSerializable(KEY_POST);
                myPosts.set(postIndex, updatedPost);
                postIdMap.put(updatedPost, updatedPost.getPostId());
                postAdapter.notifyDataSetChanged();
            } else {
                PostsModel deletedPost = postAdapter.getItem(postIndex);
                postIdMap.remove(deletedPost);
                myPosts.remove(postIndex);
                postAdapter.notifyDataSetChanged();
            }
        }
    }
}
