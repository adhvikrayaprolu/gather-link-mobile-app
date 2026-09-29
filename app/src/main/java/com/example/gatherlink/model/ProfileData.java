package com.example.gatherlink.model;

import java.util.HashMap;
import java.util.Map;

/** Firestore profile metadata only. Authentication credentials never belong here. */
public final class ProfileData {
    private ProfileData() {}

    public static Map<String, Object> create(String uid, String firstName, String lastName, String email) {
        if (uid == null || uid.isEmpty() || firstName == null || firstName.trim().isEmpty()
                || lastName == null || lastName.trim().isEmpty() || email == null || email.trim().isEmpty()) {
            throw new IllegalArgumentException("Profile identity and names are required");
        }
        Map<String, Object> profile = new HashMap<>();
        profile.put("userID", uid);
        profile.put("firstName", firstName.trim());
        profile.put("lastName", lastName.trim());
        profile.put("email", email.trim());
        return profile;
    }
}
