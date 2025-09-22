package com.example.gatherlink.model;

import com.google.firebase.firestore.ServerTimestamp;

import java.io.Serializable;
import java.util.Date;

public class GroupModel implements Serializable {
    private String groupId;
    private String groupName;
    private String groupDescription;
    private String ownerEmail;

    @ServerTimestamp
    private Date createdAt;

    // extra
    private int numberOfPosts;
    private String lastPostMessage;
    @ServerTimestamp
    private Date lastPostAt;

    public GroupModel() {}

    // Getters and setters
    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public String getGroupDescription() {
        return groupDescription;
    }

    public void setDescription(String description) {
        this.groupDescription = description;
    }

    public String getOwnerEmail() {
        return ownerEmail;
    }

    public void setOwnerEmail(String ownerEmail) {
        this.ownerEmail = ownerEmail;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public int getNumberOfPosts() {
        return numberOfPosts;
    }

    public void setNumberOfPosts(int numberOfPosts) {
        this.numberOfPosts = numberOfPosts;
    }

    public String getLastPostMessage() {
        return lastPostMessage;
    }

    public void setLastPostMessage(String lastPostMessage) {
        this.lastPostMessage = lastPostMessage;
    }

    public Date getLastPostAt() {
        return lastPostAt;
    }

    public void setLastPostAt(Date lastPostAt) {
        this.lastPostAt = lastPostAt;
    }
}