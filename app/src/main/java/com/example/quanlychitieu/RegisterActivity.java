package com.example.quanlychitieu;

import android.app.AlertDialog;
import android.content.Intent;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;


import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public  class RegisterActivity extends AppCompatActivity {

    Button registerButton;
    TextView textViewLoginLink;
    TextInputEditText editTextEmail;
    TextInputEditText editTextPassword, editTextConfirmPassword;

    FirebaseAuth mAuth;

    public void showSuccessDialog(){
        LayoutInflater inflater = LayoutInflater.from(this);
        View dialogView = inflater.inflate(R.layout.modal_account_created, null);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(dialogView);
        AlertDialog dialog = builder.create();

        Button dialogButton = dialogView.findViewById(R.id.dialog_button);
        dialogButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
            }
        });

        dialog.show();

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        textViewLoginLink = findViewById(R.id.textViewLoginLink);
        registerButton = findViewById(R.id.buttonRegister);
        editTextEmail = findViewById(R.id.editTextEmail);
        editTextPassword = findViewById(R.id.editTextPassword);
        editTextConfirmPassword = findViewById(R.id.editTextConfirmPassword);
        mAuth = FirebaseAuth.getInstance();



        textViewLoginLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent;
                intent = new Intent(RegisterActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email;
                String password;
                String confirmPassword;

                email = editTextEmail.getText().toString();
                password = editTextPassword.getText().toString();
                confirmPassword = editTextConfirmPassword.getText().toString();

                if(TextUtils.isEmpty(email)){
                    Toast.makeText(RegisterActivity.this, "Enter Email", Toast.LENGTH_SHORT).show();
                    return;
                }

                if(TextUtils.isEmpty(password) || TextUtils.isEmpty(confirmPassword)){
                    Toast.makeText(RegisterActivity.this, "Enter and confirm your password", Toast.LENGTH_SHORT).show();
                    return;
                }

                if(!password.equals(confirmPassword)){
                    Toast.makeText(RegisterActivity.this, "Your password does not match", Toast.LENGTH_SHORT).show();
                    return;
                }
                else {
                    //Neu nhu khong co loi thi thuc hien ham luu du lieu
                    registerUser(email, password);
                }
            }
        });
    }
    //Ham thuc hien luu du lieu len Realtime Database
    private void registerUser(String email, final String password) {
        mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    FirebaseUser user = mAuth.getCurrentUser();
                    String username = user.getEmail();
                    String uid = user.getUid();
                    HashMap<String, Object> users = new HashMap<>();
                    users.put("email", username);
                    users.put("uid", uid);
                    users.put("balance", 0); // Set the balance as a number, e.g., 0

                    FirebaseDatabase database = FirebaseDatabase.getInstance();
                    DatabaseReference reference = database.getReference("Users");
                    reference.child(uid).setValue(users);

                    Toast.makeText(RegisterActivity.this, "Registered user " + user.getEmail() + " successfully", Toast.LENGTH_LONG).show();
                    showSuccessDialog();
                } else {
                    Toast.makeText(RegisterActivity.this, "Failed to register!", Toast.LENGTH_LONG).show();
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(RegisterActivity.this, "An error occurred", Toast.LENGTH_LONG).show();
            }
        });
    }

}