package com.example.gatherlink.utils;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;

public class GroupMembershipUtil {
    public static void isUserInGroup(String userId, String groupId,
                                     OnSuccessListener<Boolean> callback) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        db.collection("GroupMemberships")
                .whereEqualTo("userId", userId)
                .whereEqualTo("groupId", groupId)
                .get()
                .addOnSuccessListener(querySnapshot -> {
                    callback.onSuccess(!querySnapshot.isEmpty());
                });
    }
}