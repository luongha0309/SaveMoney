package com.example.quanlychitieu;

import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;


import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public  class SendMailActivity extends AppCompatActivity {
    private EditText _inputEmail,
            _inputSubject,
            _inputMessage;
    private  Button SendMailBtn;


    protected void onCreate(Bundle saveInstanceState){
        super.onCreate(saveInstanceState);
        setContentView(R.layout.activity_send_mail);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.lavender)));

        _inputEmail = findViewById(R.id.inputEmail);
        _inputSubject = findViewById(R.id.inputSubject);
        _inputMessage = findViewById(R.id.inputMessage);

        SendMailBtn = findViewById(R.id.btnSendMail);

        SendMailBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendMail();
            }
        });


    }
    private void sendMail()
    {
        String repicienceList = _inputEmail.getText().toString();
        String[] repiciences = repicienceList.split(",");

        String Subject = _inputSubject.getText().toString();
        String Message = _inputMessage.getText().toString();

        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.putExtra(Intent.EXTRA_EMAIL,repiciences );
        intent.putExtra(Intent.EXTRA_SUBJECT,Subject );
        intent.putExtra(Intent.EXTRA_TEXT,Message );

        intent.setType("message/rfc822");
        startActivity(Intent.createChooser(intent, "Choose an email client"));

    }
    @Override
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


}