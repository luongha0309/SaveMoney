package com.example.quanlychitieu;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import android.view.View.OnClickListener;

public class AboutActivity extends AppCompatActivity{

    public TextView contactFb, contactTw, contactIns;

    @SuppressLint("MissingInflatedId")
    protected void onCreate(Bundle saveInstanceState) {
        super.onCreate(saveInstanceState);
        setContentView(R.layout.about_activity);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.lavender)));

//  Make button Contact Facebook clickable
        contactFb = findViewById(R.id.contact_fb);
        contactFb.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Uri uri = Uri.parse("https://www.facebook.com");
                Intent intent = new Intent(Intent.ACTION_VIEW,uri);
                startActivity(intent);
            }
        });

//    Make button Contact Twitter clickable
        contactTw = findViewById(R.id.contact_tw);
        contactTw.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Uri uri = Uri.parse("https://twitter.com");
                Intent intent = new Intent(Intent.ACTION_VIEW,uri);
                startActivity(intent);
            }
        });


//    Make button Contact Instagram clickable
        contactIns = findViewById(R.id.contact_ins);
        contactIns.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Uri uri = Uri.parse("https://instagram.com");
                Intent intent = new Intent(Intent.ACTION_VIEW,uri);
                startActivity(intent);
            }
        });
    }

    //    public boolean onCreateOptionsMenu(Menu menu) {
//        getMenuInflater().inflate(R.menu.seach, menu);
//        return true;
//    }
//
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                // Handle back button click here
                finish();
                return true;
//          case R.id.action_search:
//                // Handle search icon click here
//                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
