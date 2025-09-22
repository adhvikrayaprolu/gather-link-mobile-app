package com.example.gatherlink.activity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.gatherlink.R;
import com.google.firebase.auth.FirebaseAuth;

public class LoginPage extends AppCompatActivity {

    TextView emailLabel;
    TextView passwordLabel;
    EditText emailInput;
    EditText passwordInput;
    Button loginButton;

    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        emailLabel = findViewById(R.id.emailLabel);
        passwordLabel = findViewById(R.id.passwordLabel);
        emailInput = findViewById(R.id.emailInput);
        passwordInput = findViewById(R.id.passwordInput);
        loginButton = findViewById(R.id.loginButton);

        firebaseAuth =  FirebaseAuth.getInstance();

        loginButton.setOnClickListener(v -> {
            if (!emailInput.getText().toString().isEmpty()
                    && !passwordInput.getText().toString().isEmpty()) {
                firebaseAuth.signInWithEmailAndPassword(emailInput, passwordInput);
            }

        });

    }
}
