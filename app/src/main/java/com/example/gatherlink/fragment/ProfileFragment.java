package com.example.gatherlink.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.text.method.PasswordTransformationMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.gatherlink.R;
import com.example.gatherlink.activity.LoginActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class ProfileFragment extends Fragment {

    TextView welcomeText;
    EditText editFirstName, editLastName, editEmail, editPassword;
    Button updateProfileButton, logoutButton;

    private FirebaseAuth firebaseAuth;
    private FirebaseFirestore firestore;
    private String userId;

    private FirebaseUser user;

    public ProfileFragment() {}

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_fragment_profile, container, false);
        firebaseAuth = FirebaseAuth.getInstance();
        firestore = FirebaseFirestore.getInstance();
        userId = firebaseAuth.getCurrentUser().getUid();

        initializeUI(view);
        fetchUserDetails();
        setUpOnClickListeners();

        return view;
    }

    private void initializeUI(View view) {
        logoutButton = view.findViewById(R.id.logoutButton);
        welcomeText = view.findViewById(R.id.welcomeText);
        editFirstName = view.findViewById(R.id.editFirstName);
        editLastName = view.findViewById(R.id.editLastName);
        editEmail = view.findViewById(R.id.editEmail);

        editPassword = view.findViewById(R.id.editPassword);
        editPassword.setVisibility(View.GONE);

        updateProfileButton = view.findViewById(R.id.updateProfileButton);

        editEmail.setEnabled(false);
        editPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
    }

    private void fetchUserDetails() {
        firestore.collection("Users")
                .document(userId)
                .get()
                .addOnSuccessListener(doc -> {
                    if (doc.exists()) {
                        String firstName = doc.getString("firstName");
                        String lastName = doc.getString("lastName");
                        String email = doc.getString("email");
                        String password = doc.getString("password");

                        welcomeText.setText("Welcome, " + firstName + "!");
                        editFirstName.setText(firstName);
                        editLastName.setText(lastName);
                        editEmail.setText(email);
//                        editPassword.setText(password); // shows as ****
                    }
                });
    }

    private void setUpOnClickListeners() {
        updateProfileButton.setOnClickListener(view -> updateUserDetails());
        logoutButton.setOnClickListener(view -> logout());
    }

    private void updateUserDetails() {
        String newFirstName = editFirstName.getText().toString().trim();
        String newLastName = editLastName.getText().toString().trim();
//        String newPassword = editPassword.getText().toString().trim();

        Map<String, Object> updates = new HashMap<>();
        updates.put("firstName", newFirstName);
        updates.put("lastName", newLastName);
//        updates.put("password", newPassword);
        updates.put("updatedAt", FieldValue.serverTimestamp());

        firestore.collection("Users")
                .document(userId)
                .update(updates)
                .addOnSuccessListener(unused -> {
                    Toast.makeText(getActivity(), "Profile updated!", Toast.LENGTH_SHORT).show();

                    if (getActivity() != null) {
                        getActivity().getSupportFragmentManager()
                                .beginTransaction()
                                .replace(R.id.fragment, new HomeFragment())
                                .addToBackStack(null)
                                .commit();
                    }
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(getActivity(), "Update failed.", Toast.LENGTH_SHORT).show();
                });
    }

    private void logout() {
        firebaseAuth.signOut();
        Intent logoutIntent = new Intent(getActivity(), LoginActivity.class);
        startActivity(logoutIntent);
        getActivity().finish();
    }
}
