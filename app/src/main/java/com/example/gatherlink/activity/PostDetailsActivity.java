package com.example.gatherlink.activity;

import static com.example.gatherlink.adapters.PostAdapter.KEY_ID;
import static com.example.gatherlink.adapters.PostAdapter.KEY_INDEX;
import static com.example.gatherlink.adapters.PostAdapter.KEY_POST;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.gatherlink.R;
import com.example.gatherlink.model.PostsModel;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.util.Locale;

public class PostDetailsActivity extends AppCompatActivity {

    public static final int requestCodeForUpdatePost = 10;

    private TextView postMessage;
    private TextView postEmail;
    private TextView postDate;
    private TextView postLikes;
    private TextView deletePost;
    private Button updatePost;
    private TextView groupName;

    private PostsModel postDetails;
    private String postId;
    private int postIndex;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_details);

        Bundle bundle = getIntent().getExtras();

        if (bundle != null) {
            postDetails = (PostsModel) bundle.getSerializable(KEY_POST);
            postId = bundle.getString(KEY_ID);
            postIndex = bundle.getInt(KEY_INDEX);
        }

        initializeFrontEndElements();
        populateTextViewFields(postDetails);
        setupListeners();
    }

    private void initializeFrontEndElements() {
        postMessage = findViewById(R.id.postMessage);
        postEmail = findViewById(R.id.postEmail);
        postDate = findViewById(R.id.postDate);
        postLikes = findViewById(R.id.postLikes);
        updatePost = findViewById(R.id.updatePost);
        deletePost = findViewById(R.id.deletePost);
        groupName = findViewById(R.id.groupName);
    }

    private void populateTextViewFields(PostsModel postDetails) {
        if (postDetails != null) {
            postMessage.setText(postDetails.getMessage());
            postEmail.setText(postDetails.getEmail());
            postLikes.setText(String.valueOf(postDetails.getLikes()));

            if (postDetails.getCreatedAt() != null) {
                String formattedDate = new SimpleDateFormat("dd MMM yyyy", Locale.getDefault())
                        .format(postDetails.getCreatedAt());
                postDate.setText(formattedDate);
            } else {
                postDate.setText("Unknown");
            }

            if (postDetails.getGroupId() != null) {
                fetchAndDisplayGroupInfo(postDetails.getGroupId());
            } else {
                groupName.setText("(No group)");
            }
        }
    }

    private void fetchAndDisplayGroupInfo(String groupId) {
        FirebaseFirestore.getInstance()
                .collection("Groups")
                .document(groupId)
                .get()
                .addOnSuccessListener(groupDoc -> {
                    if (groupDoc.exists()) {
                        String name = groupDoc.getString("groupName");

                        groupName.setText(name != null ? name : "(Unknown Group)");
                    }
                })
                .addOnFailureListener(e -> {
                    groupName.setText("(Group load failed)");
                });
    }


    private void setupListeners() {
        updatePost.setOnClickListener(view -> {
            updatePost();
        });

        deletePost.setOnClickListener(view -> {
            deletePost();
        });
    }

    private void updatePost() {
        Intent updatePostIntent = new Intent(PostDetailsActivity.this, PostActivity.class);
        Bundle bundle = new Bundle();

        bundle.putString("OperationCode", "edit");
        bundle.putString(KEY_ID, postId);
        bundle.putSerializable(KEY_POST, postDetails);

        updatePostIntent.putExtras(bundle);
        startActivityForResult(updatePostIntent, requestCodeForUpdatePost);
    }

    private void deletePost() {
        if (postDetails == null || postDetails.getGroupId() == null || postId == null) {
            Toast.makeText(this, "Missing post or group ID!", Toast.LENGTH_SHORT).show();
            return;
        }

        FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();

        firebaseFirestore.collection("Groups")
                .document(postDetails.getGroupId())
                .collection("Posts")
                .document(postId)
                .delete()
                .addOnSuccessListener(success -> {
                    Intent deletePostIntent = new Intent();
                    Bundle bundle = new Bundle();
                    bundle.putInt("PostIndex", postIndex);

                    deletePostIntent.putExtras(bundle);
                    setResult(1, deletePostIntent);

                    Toast.makeText(this, "Post deleted successfully!", Toast.LENGTH_SHORT).show();
                    finish();
                })
                .addOnFailureListener(error -> {
                    Toast.makeText(this, "Failed to delete your post!", Toast.LENGTH_SHORT).show();
                });

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if ((requestCode == requestCodeForUpdatePost) && (resultCode == 1) && (data != null)) {
            Bundle bundle = data.getExtras();
            PostsModel updatedPostDetails = (PostsModel) bundle.getSerializable(KEY_POST);

            postDetails = updatedPostDetails;
            postMessage.setText(postDetails.getMessage());
            postLikes.setText(String.valueOf(postDetails.getLikes()));

            Intent backIntent = new Intent();
            Bundle backBundle = new Bundle();
            backBundle.putSerializable(KEY_POST, updatedPostDetails);
            backBundle.putInt(KEY_INDEX, postIndex);
            backIntent.putExtras(backBundle);
            setResult(1, backIntent);
        }
    }

}
