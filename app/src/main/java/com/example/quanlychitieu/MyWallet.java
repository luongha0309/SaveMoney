package com.example.quanlychitieu;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;

import adapter.ListViewMyWalletAdapter;

public class MyWallet extends AppCompatActivity {

    String itemList[] = {"Momo", "Moca", "BIDV", "VCB Digital", "Zalo Pay", "ShopeePay", "ViettinBank"};
    String itemText2[] = {"Cash: ", "Cash: ", "Cash: ", "Cash: ", "Cash: ", "Cash: ", "Cash: "};
    int itemImage[] = {R.drawable.transaction_item_icon, R.drawable.transaction_item_icon, R.drawable.transaction_item_icon, R.drawable.transaction_item_icon,
            R.drawable.transaction_item_icon, R.drawable.transaction_item_icon, R.drawable.transaction_item_icon};
    ListView listView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_wallet);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.lavender)));

        listView = (ListView) findViewById(R.id.list_mywallet);
        ListViewMyWalletAdapter lvMyWalletAdapter = new ListViewMyWalletAdapter(getApplicationContext(), itemList, itemText2, itemImage);
        listView.setAdapter(lvMyWalletAdapter);
    }
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.seach, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                // Handle back button click here
                finish();
                return true;
            case R.id.action_search:
                // Handle search icon click here
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}