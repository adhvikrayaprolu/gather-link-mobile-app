package com.example.gatherlink.model;

import com.google.firebase.Timestamp;

import java.io.Serializable;

public class GroupMembershipModel implements Serializable {
    private String userId;
    private String groupId;
    private Timestamp joinedAt;

    public GroupMembershipModel() {}

    public GroupMembershipModel(String userId, String groupId, Timestamp joinedAt) {
        this.userId = userId;
        this.groupId = groupId;
        this.joinedAt = joinedAt;
    }

    public String getUserId() { return userId; }
    public String getGroupId() { return groupId; }
    public Timestamp getJoinedAt() { return joinedAt; }

    public void setUserId(String userId) { this.userId = userId; }
    public void setGroupId(String groupId) { this.groupId = groupId; }
    public void setJoinedAt(Timestamp joinedAt) { this.joinedAt = joinedAt; }
}
