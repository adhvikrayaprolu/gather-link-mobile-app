package com.example.gatherlink.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.gatherlink.R;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity {

    TextView emailLabel;
    TextView passwordLabel;
    EditText emailInput;
    EditText passwordInput;
    Button loginButton;
    Button signUpButton;

    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        FirebaseApp.initializeApp(this);
        firebaseAuth =  FirebaseAuth.getInstance();

        initializeFrontEndElements();
        setupOnClickListeners();
    }

    private void initializeFrontEndElements() {
        emailLabel = findViewById(R.id.emailLabel);
        passwordLabel = findViewById(R.id.passwordLabel);
        emailInput = findViewById(R.id.emailInput);
        passwordInput = findViewById(R.id.passwordInput);
        loginButton = findViewById(R.id.loginButton);
        signUpButton = findViewById(R.id.signUpButton);
    }

    private void setupOnClickListeners() {
        signUpButton.setOnClickListener(view -> {
            Intent signUpIntent = new Intent(LoginActivity.this, SignUpActivity.class);
            startActivity(signUpIntent);
        });

        loginButton.setOnClickListener(view -> login());
    }

    private void login() {
        String email = emailInput.getText().toString().trim();
        String password = passwordInput.getText().toString().trim();

        if (!email.isEmpty() && !password.isEmpty()) {
            firebaseAuth.signInWithEmailAndPassword(email, password)
                    .addOnSuccessListener(authResult -> {
                        Intent homePageIntent = new Intent(LoginActivity.this, HomeActivity.class);
                        startActivity(homePageIntent);
                        finish();
                    })
                    .addOnFailureListener(e -> {
                        Log.e("login error", e.toString());
                        Toast.makeText(this, "Error while logging in, check email or password!", Toast.LENGTH_SHORT).show();
                    });
        } else {
            Toast.makeText(this, "Please enter both email and password.", Toast.LENGTH_SHORT).show();
        }
    }
}
