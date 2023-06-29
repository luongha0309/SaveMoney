package com.example.quanlychitieu;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;

import javax.sql.StatementEvent;

public class ForgotPassword extends AppCompatActivity {

    TextInputEditText editTextEmail;
    Button resetPassWordBtn;
    TextView backToLoginLink;
    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        editTextEmail = findViewById(R.id.userEmail);
        resetPassWordBtn = findViewById(R.id.buttonResetPassword);
        backToLoginLink = findViewById(R.id.backToLogin);
        mAuth = FirebaseAuth.getInstance();


        backToLoginLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent;
                intent = new Intent(ForgotPassword.this, LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });

        resetPassWordBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = editTextEmail.getText().toString().trim();

                if (TextUtils.isEmpty(email)) {
                    Toast.makeText(getApplication(), "Enter your registered email id", Toast.LENGTH_SHORT).show();
                    return;
                }
                mAuth.sendPasswordResetEmail(email)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    Toast.makeText(getApplication(), "A reset password email has been sent to you", Toast.LENGTH_SHORT).show();
                                    return;
                                }
                            }
                        });
            }
        });

    }
}