package com.example.gatherlink.activity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.gatherlink.R;
import com.example.gatherlink.model.GroupModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Date;

public class CreateGroupActivity extends AppCompatActivity {
    private EditText groupNameInput, groupDescriptionInput;
    private Button createGroupButton;
    private FirebaseFirestore store;
    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_group);

        store = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();

        initializeFrontEndElements();
        createGroupButton.setOnClickListener(v -> createGroup());
    }

    private void initializeFrontEndElements() {
        groupNameInput = findViewById(R.id.groupNameInput);
        groupDescriptionInput = findViewById(R.id.groupDescriptionInput);
        createGroupButton = findViewById(R.id.createGroupButton);
    }

    private void createGroup() {
        String name = groupNameInput.getText().toString().trim();
        String desc = groupDescriptionInput.getText().toString().trim();
        String email = auth.getCurrentUser().getEmail();

        if (name.isEmpty() || desc.isEmpty()) {
            Toast.makeText(this, "Please fill in all fields!", Toast.LENGTH_SHORT).show();
            return;
        }

        GroupModel newGroup = new GroupModel();

        newGroup.setGroupName(name);
        newGroup.setDescription(desc);
        newGroup.setOwnerEmail(email);
        newGroup.setCreatedAt(new Date());

        store.collection("Groups").add(newGroup)
                .addOnSuccessListener(documentReference -> {
                    String groupId = documentReference.getId();
                    documentReference.update("groupId", groupId)
                            .addOnSuccessListener(unused -> {
                                Toast.makeText(this, "Group created successfully!", Toast.LENGTH_SHORT).show();
                                Intent createGroupIntent = new Intent(CreateGroupActivity.this, ExploreGroupsActivity.class);
                                startActivity(createGroupIntent);
                                finish();
                            })
                            .addOnFailureListener(e -> {
                                Toast.makeText(this, "Group created, but could not set Group ID!", Toast.LENGTH_SHORT).show();
                            });
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(this, "Failed to create group!", Toast.LENGTH_SHORT).show();
                });
    }
}
