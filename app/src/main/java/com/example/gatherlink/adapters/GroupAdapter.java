package com.example.gatherlink.adapters;

import com.example.gatherlink.activity.GroupPostsActivity;
import com.example.gatherlink.utils.GroupMembershipUtil;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.gatherlink.R;
import com.example.gatherlink.activity.GroupDetailsActivity;
import com.example.gatherlink.model.GroupModel;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class GroupAdapter extends RecyclerView.Adapter<GroupAdapter.GroupViewHolder> {
    private final ArrayList<GroupModel> groupList;
    private final SimpleDateFormat dateFormat = new SimpleDateFormat("dd MMM yyyy", Locale.getDefault());
    private final boolean isMyGroupsContext;

    public GroupAdapter(ArrayList<GroupModel> groups, boolean isMyGroupsContext) {

        this.groupList = groups;
        this.isMyGroupsContext = isMyGroupsContext;
    }

    @NonNull
    @Override
    public GroupViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_group, parent, false);
        return new GroupViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull GroupViewHolder holder, int position) {
        GroupModel group = groupList.get(position);

        holder.groupName.setText(group.getGroupName());
        holder.groupDescription.setText(group.getGroupDescription());
        holder.ownerEmail.setText("Created by: " + group.getOwnerEmail());

        String email = group.getOwnerEmail();
        String initials = "??";
        if (email != null && email.contains("@")) {
            initials = email.substring(0, 2).toUpperCase();
        }
        holder.groupProfilePic.setText(initials);

        if (group.getCreatedAt() != null) {
            holder.groupDate.setText("Created on: " + dateFormat.format(group.getCreatedAt()));
        } else {
            holder.groupDate.setText("Created on: (unknown)");
        }

        String currentUserId = FirebaseAuth.getInstance().getCurrentUser().getUid();

        if (isMyGroupsContext) {
            holder.joinGroupButton.setText("View Posts");
            holder.joinGroupButton.setEnabled(true);
            holder.joinGroupButton.setOnClickListener(v -> {
                Intent intent = new Intent(holder.itemView.getContext(), GroupPostsActivity.class);
                intent.putExtra("groupData", group);
                holder.itemView.getContext().startActivity(intent);
            });

            holder.rowGroup.setOnClickListener(null);
            holder.rowGroup.setClickable(false);
            holder.rowGroup.setFocusable(false);
            holder.rowGroup.setEnabled(false);
        } else {
            holder.joinGroupButton.setEnabled(false);

            GroupMembershipUtil.isUserInGroup(currentUserId, group.getGroupId(), isJoined -> {
                if (isJoined) {
                    holder.joinGroupButton.setText("Joined");
                    holder.joinGroupButton.setEnabled(false);
                } else {
                    holder.joinGroupButton.setText("Join Group");
                    holder.joinGroupButton.setEnabled(true);

                    holder.joinGroupButton.setOnClickListener(v -> {
                        FirebaseFirestore db = FirebaseFirestore.getInstance();
                        Map<String, Object> membership = new HashMap<>();
                        membership.put("userId", currentUserId);
                        membership.put("groupId", group.getGroupId());
                        membership.put("joinedAt", new Timestamp(new Date()));

                        db.collection("GroupMemberships")
                                .add(membership)
                                .addOnSuccessListener(documentReference -> {
                                    Toast.makeText(holder.itemView.getContext(), "Successfully joined group!", Toast.LENGTH_SHORT).show();
                                    holder.joinGroupButton.setText("Joined");
                                    holder.joinGroupButton.setEnabled(false);
                                })
                                .addOnFailureListener(e -> {
                                    Toast.makeText(holder.itemView.getContext(), "Failed to join group.", Toast.LENGTH_SHORT).show();
                                });
                    });
                }
            });
        }

        if (isMyGroupsContext) {
            // Go to GroupPostsActivity if we're in MyGroupsActivity
            holder.rowGroup.setOnClickListener(v -> {
                Intent intent = new Intent(holder.itemView.getContext(), GroupPostsActivity.class);
                intent.putExtra("groupData", group);
                holder.itemView.getContext().startActivity(intent);
            });
        } else {
            // GO to GroupDetailsActivity from ExploreGroups
            holder.rowGroup.setOnClickListener(v -> {
                Intent intent = new Intent(holder.itemView.getContext(), GroupDetailsActivity.class);
                intent.putExtra("groupData", group);
                holder.itemView.getContext().startActivity(intent);
            });
        }
    }

    @Override
    public int getItemCount() {
        return groupList.size();
    }

    public GroupModel getItem(int index) {
        return groupList.get(index);
    }

    public static class GroupViewHolder extends RecyclerView.ViewHolder {
        TextView groupName, groupDescription, ownerEmail, groupDate, groupProfilePic;
        Button joinGroupButton;
        LinearLayout rowGroup;

        public GroupViewHolder(@NonNull View view) {
            super(view);
            groupName = view.findViewById(R.id.groupName);
            groupDescription = view.findViewById(R.id.groupDescription);
            ownerEmail = view.findViewById(R.id.groupOwnerEmail);
            groupDate = view.findViewById(R.id.groupDate);
            groupProfilePic = view.findViewById(R.id.groupOwnerInitials);
            joinGroupButton = itemView.findViewById(R.id.joinGroupButton);
            rowGroup = view.findViewById(R.id.groupRow);
        }
    }
}
