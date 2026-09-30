package com.example.gatherlink.model;
import org.junit.Test;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Map;
import static org.junit.Assert.*;

public class ProfileDataTest {
    @Test public void profileContainsOnlyIdentityMetadata() {
        Map<String, Object> data = ProfileData.create("uid", " First ", " Last ", " user@example.test ");
        assertEquals(new HashSet<>(Arrays.asList("userID", "firstName", "lastName", "email")), data.keySet());
        assertEquals("First", data.get("firstName"));
        assertFalse(data.containsKey("password"));
    }
    @Test(expected = IllegalArgumentException.class) public void missingIdentityRejected() {
        ProfileData.create(null, "First", "Last", "a@example.test");
    }
    @Test(expected = IllegalArgumentException.class) public void emptyNameRejected() {
        ProfileData.create("uid", " ", "Last", "a@example.test");
    }
}
