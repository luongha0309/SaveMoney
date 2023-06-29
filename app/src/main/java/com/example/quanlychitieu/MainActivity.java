package com.example.quanlychitieu;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.annotation.SuppressLint;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.quanlychitieu.databinding.ActivityMainBinding;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Objects;

import bottomnavigation.HomeFrag;
import bottomnavigation.ProfileFrag;
import bottomnavigation.ReportFrag;
import bottomnavigation.TransactionFrag;
import objects.User;

public class MainActivity extends AppCompatActivity {

    ActivityMainBinding binding;
    FloatingActionButton addTransactionBtn;

    FirebaseAuth myAuth;
    FirebaseUser currentUser;

    @SuppressLint("NonConstantResourceId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        myAuth = FirebaseAuth.getInstance();
        currentUser = FirebaseAuth.getInstance().getCurrentUser();

        if (myAuth != null) {
            String email = Objects.requireNonNull(currentUser). getEmail();
            String userID = currentUser.getUid();
            User u = new User(email, userID);
        }

        addTransactionBtn = findViewById(R.id.addTransactionBtn);

        replaceFrags(new HomeFrag());

        binding.bottomNavigationView.setBackground(null);

        binding.bottomNavigationView.setOnItemSelectedListener(item -> {
            switch (item.getItemId()){
                case R.id.home:
                    replaceFrags(new HomeFrag());
                    break;

                case R.id.transaction:
                    replaceFrags(new TransactionFrag());
                    break;

                case R.id.report:
                    replaceFrags(new ReportFrag());
                    break;

                case R.id.profile:
                    replaceFrags(new ProfileFrag());
                    break;
            }
            return true;
        });


        addTransactionBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent;
                intent = new Intent(MainActivity.this, AddTransaction.class);
                startActivity(intent);
//                finish();
            }
        });

    }

    private void replaceFrags(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frame_layout, fragment);
        fragmentTransaction.commit();
    }

}