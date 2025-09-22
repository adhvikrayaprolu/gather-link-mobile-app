package com.example.gatherlink.activity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.gatherlink.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;

public class SignUpActivity extends AppCompatActivity {
    EditText emailInput;
    EditText passwordInput;
    EditText firstNameInput;
    EditText lastNameInput;
    Button signUpButton;

    private FirebaseAuth firebaseAuth;
    private FirebaseFirestore store;
    HashMap<String, Object> userData = new HashMap<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        firebaseAuth = FirebaseAuth.getInstance();
        store = FirebaseFirestore.getInstance();

        initializeFrontEndElements();
        setupOnClickListeners();
    }

    private void initializeFrontEndElements() {
        emailInput = findViewById(R.id.emailInput);
        passwordInput = findViewById(R.id.passwordInput);
        signUpButton = findViewById(R.id.signUpButton);
        firstNameInput = findViewById(R.id.firstNameInput);
        lastNameInput = findViewById(R.id.lastNameInput);
    }

    private void setupOnClickListeners() {
        signUpButton.setOnClickListener(view -> signUp());
    }

    private void signUp() {
        String firstName = firstNameInput.getText().toString().trim();
        String lastName = lastNameInput.getText().toString().trim();
        String email = emailInput.getText().toString().trim();
        String password = passwordInput.getText().toString().trim();

        if (!firstName.isEmpty() && !lastName.isEmpty() && !email.isEmpty() && !password.isEmpty()) {
            firebaseAuth.createUserWithEmailAndPassword(email, password)
                    .addOnSuccessListener(authResult -> {
                        String userId = firebaseAuth.getCurrentUser().getUid();

                        userData.put("userID", userId);
                        userData.put("firstName", firstName);
                        userData.put("lastName", lastName);
                        userData.put("email", email);
                        userData.put("password", password);
                        userData.put("createdAt", com.google.firebase.Timestamp.now());
                        userData.put("updatedAt", com.google.firebase.Timestamp.now());

                        store.collection("Users").document(userId).set(userData)
                                .addOnSuccessListener(newUser -> {
                                    Toast.makeText(this, "Account created!", Toast.LENGTH_SHORT).show();

                                    Intent signUpIntent = new Intent(SignUpActivity.this, LoginActivity.class);
                                    startActivity(signUpIntent);
                                    finish();
                                })
                                .addOnFailureListener(e -> {
                                    Toast.makeText(this, "Error while saving user details.", Toast.LENGTH_SHORT).show();
                                });
                    })
                    .addOnFailureListener(e -> {
                        Toast.makeText(this, "Error while signing up. Please try again!", Toast.LENGTH_SHORT).show();
                    });
        } else {
            Toast.makeText(this, "Please fill all the fields!", Toast.LENGTH_SHORT).show();
        }
    }
}
