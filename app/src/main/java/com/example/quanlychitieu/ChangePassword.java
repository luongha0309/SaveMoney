package com.example.quanlychitieu;

import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class ChangePassword extends AppCompatActivity {
    EditText currentPassword, password, confirmPassword;
    Button btnChangePassword;
    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    FirebaseUser mUser = mAuth.getCurrentUser();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);
        btnChangePassword = findViewById(R.id.btnChangePassword);
        currentPassword = findViewById(R.id.currentPassword);
        password = findViewById(R.id.password);
        confirmPassword = findViewById(R.id.confirmPassword);


        //Action bar
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.lavender)));

        btnChangePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String curPassword = currentPassword.getText().toString();
                String newPassword = password.getText().toString();
                String conPassword = confirmPassword.getText().toString();

                if(TextUtils.isEmpty(newPassword) || TextUtils.isEmpty(conPassword)){
                    Toast.makeText(ChangePassword.this, "Enter and confirm your password", Toast.LENGTH_SHORT).show();
                    return;
                }

                if(!newPassword.equals(conPassword)){
                    Toast.makeText(ChangePassword.this, "Your password does not match", Toast.LENGTH_SHORT).show();
                    return;
                }
                else {
                    //Neu nhu khong co loi thi thuc hien ham luu du lieu
                    changePassword(curPassword, newPassword, conPassword);
                }
            }
        });
    }

    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                // Handle back button click here
                finish();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void changePassword(final String currentPassword, final String newPassword, final String confirmPassword) {
        // Xác thực người dùng bằng mật khẩu hiện tại
        AuthCredential credential = EmailAuthProvider.getCredential(mUser.getEmail(), currentPassword);
        mUser.reauthenticate(credential)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            // Xác thực thành công, tiến hành thay đổi mật khẩu mới
                            mUser.updatePassword(newPassword)
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()) {
                                                // Thay đổi mật khẩu thành công
                                                Toast.makeText(ChangePassword.this, "Password changed successfully", Toast.LENGTH_SHORT).show();
                                                finish();
                                            } else {
                                                // Lỗi khi thay đổi mật khẩu
                                                Toast.makeText(ChangePassword.this, "Failed to change password. Please try again.", Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    });
                        } else {
                            // Xác thực không thành công, mật khẩu hiện tại không chính xác
                            Toast.makeText(ChangePassword.this, "Current password is incorrect", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

}
