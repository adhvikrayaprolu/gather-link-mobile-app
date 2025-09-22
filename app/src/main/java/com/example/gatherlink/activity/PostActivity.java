package com.example.gatherlink.activity;

import static com.example.gatherlink.adapters.PostAdapter.KEY_ID;
import static com.example.gatherlink.adapters.PostAdapter.KEY_POST;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.gatherlink.R;
import com.example.gatherlink.model.PostsModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class PostActivity extends AppCompatActivity {
    private EditText messageInput;
    private Button postMessageButton;
    private Spinner groupSpinner;

    private FirebaseUser currentUser;
    private FirebaseFirestore store;

    private List<String> groupNames = new ArrayList<>();
    private List<String> groupIds = new ArrayList<>();
    private String selectedGroupId = null;

    private String operationCode;
    private String postId;
    private PostsModel postDetails;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post);

        currentUser = FirebaseAuth.getInstance().getCurrentUser();
        store = FirebaseFirestore.getInstance();

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            operationCode = bundle.getString("OperationCode", "create");
            postId = bundle.getString(KEY_ID);
            postDetails = (PostsModel) bundle.getSerializable(KEY_POST);
        } else {
            operationCode = "create";
        }

        initializeFrontEndElements();
        loadGroupsFromFirestore();
        setupOnClickListeners();
    }

    private void initializeFrontEndElements() {
        messageInput = findViewById(R.id.messageInput);
        postMessageButton = findViewById(R.id.submitButton);
        groupSpinner = findViewById(R.id.groupSpinner);

        if (operationCode.equalsIgnoreCase("edit") && postDetails != null) {
            postMessageButton.setText("Update Post");
            messageInput.setText(postDetails.getMessage());
        }
    }

    private void loadGroupsFromFirestore() {
        store.collection("Groups")
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {

                    for (QueryDocumentSnapshot doc : queryDocumentSnapshots) {
                        String groupName = doc.getString("groupName");
                        String groupId = doc.getId();
                        groupNames.add(groupName);
                        groupIds.add(groupId);
                    }

                    ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                            android.R.layout.simple_spinner_dropdown_item, groupNames);
                    groupSpinner.setAdapter(adapter);

                    if (operationCode.equalsIgnoreCase("edit") && postDetails != null) {
                        int index = groupIds.indexOf(postDetails.getGroupId());
                        if (index >= 0) {
                            groupSpinner.setSelection(index);
                            groupSpinner.setEnabled(false);
                        }
                    }

                })
                .addOnFailureListener(e -> {
                    Toast.makeText(this, "Failed to load groups!", Toast.LENGTH_SHORT).show();
                });
    }

    private void setupOnClickListeners() {
        postMessageButton.setOnClickListener(v -> {
            String message = messageInput.getText().toString().trim();

            if (!message.isEmpty()) {
                if (operationCode.equalsIgnoreCase("edit")) {
                    updatePost(message);
                    return;
                }
                createPost(message);
            } else {
                Toast.makeText(this, "Please enter a message.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void createPost(String message) {

        // checking if there is a user signed in or not
        if (currentUser == null) {
            Toast.makeText(this, "User not logged in", Toast.LENGTH_SHORT).show();
            return;
        }

        // retreive the groupID
        int selectedPosition = groupSpinner.getSelectedItemPosition();
        if (selectedPosition < 0 || selectedPosition >= groupIds.size()) {
            Toast.makeText(this, "Please select a group.", Toast.LENGTH_SHORT).show();
            return;
        }
        selectedGroupId = groupIds.get(selectedPosition);

        String userId = currentUser.getUid();

        store.collection("Users").document(userId).get()
                .addOnSuccessListener(userDoc -> {
                    if(userDoc.exists()) {
                        String firstName = userDoc.getString("firstName");
                        String lastName = userDoc.getString("lastName");
                        String email = userDoc.getString("email");

                        PostsModel newPost = new PostsModel();
                        newPost.setUserId(userId);
                        newPost.setFirstName(firstName);
                        newPost.setLastName(lastName);
                        newPost.setEmail(email);
                        newPost.setGroupId(selectedGroupId);
                        newPost.setMessage(message);
                        newPost.setLikes(0);

                        // add the post to the Groups collection

                        store.collection("Groups")
                                .document(selectedGroupId)
                                .collection("Posts")
                                .add(newPost)
                                .addOnSuccessListener(documentReference -> {
                                    String postId = documentReference.getId();
                                    documentReference.update("postId", postId);

                                    Toast.makeText(this, "Post created!", Toast.LENGTH_SHORT).show();
                                    Intent postIntent = new Intent(PostActivity.this, HomeActivity.class);
                                    startActivity(postIntent);
                                    finish();
                                })
                                .addOnFailureListener(e -> {
                                    Toast.makeText(this, "Failed to post your message!", Toast.LENGTH_SHORT).show();
                                });
                    } else {
                        Toast.makeText(this, "User data not found", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(this, "Failed to retrieve user info", Toast.LENGTH_SHORT).show();
                });
    }

    private void updatePost(String message) {
        if (postDetails == null || postId == null || postDetails.getGroupId() == null) {
            Toast.makeText(this, "Post information is missing.", Toast.LENGTH_SHORT).show();
            return;
        }

        HashMap<String, Object> updatePost = new HashMap<>();
        updatePost.put("message", message);

        store.collection("Groups")
                .document(postDetails.getGroupId())
                .collection("Posts")
                .document(postId)
                .update(updatePost)
                .addOnSuccessListener(authResult -> {
                    postDetails.setMessage(message);

                    Intent updatePostIntent = new Intent();
                    Bundle bundle = new Bundle();
                    bundle.putSerializable(KEY_POST, postDetails);
                    updatePostIntent.putExtras(bundle);

                    setResult(1, updatePostIntent);
                    Toast.makeText(this, "Post updated successfully!", Toast.LENGTH_SHORT).show();
                    finish();
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(this, "Failed to update your message!", Toast.LENGTH_SHORT).show();
                });
    }
}