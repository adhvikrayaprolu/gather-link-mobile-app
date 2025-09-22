package com.example.gatherlink.activity;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.gatherlink.R;
import com.example.gatherlink.model.GroupModel;

import java.text.SimpleDateFormat;
import java.util.Locale;

public class GroupDetailsActivity extends AppCompatActivity {

    private TextView nameText, descText, emailText, dateText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_details);

        GroupModel group = (GroupModel) getIntent().getSerializableExtra("groupData");

        initializeFrontEndElements();

        nameText.setText(group.getGroupName());
        descText.setText(group.getGroupDescription());
        emailText.setText("Created by: " + group.getOwnerEmail());

        if (group.getCreatedAt() != null) {
            String formattedDate = new SimpleDateFormat("dd MMM yyyy", Locale.getDefault()).format(group.getCreatedAt());
            dateText.setText("Created on: " + formattedDate);
        }
    }

    private void initializeFrontEndElements() {
        nameText = findViewById(R.id.detailGroupName);
        descText = findViewById(R.id.detailGroupDescription);
        emailText = findViewById(R.id.detailGroupEmail);
        dateText = findViewById(R.id.detailGroupDate);
    }
}