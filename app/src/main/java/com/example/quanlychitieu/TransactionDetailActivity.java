package com.example.quanlychitieu;

import static android.content.ContentValues.TAG;

import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

import bottomnavigation.TransactionFrag;

public class TransactionDetailActivity extends AppCompatActivity {
    DatabaseReference  mDatabase;
    String TransID ;
    Boolean IsPay;
    String Amount;
    Double AmountDouble;
    String Date ;
    String Note ;
    String Type ;
    FirebaseUser currentUser;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transaction_detail);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.lavender)));
        mDatabase = FirebaseDatabase.getInstance().getReference();

        TransID = getIntent().getStringExtra("TransID");
        Amount =  getIntent().getStringExtra("amount");
        AmountDouble = Double.parseDouble(Amount);
        Date =  getIntent().getStringExtra("date");
        Note =  getIntent().getStringExtra("note");
        Type =  getIntent().getStringExtra("GetPAY");
        if(Type.equals("Earn"))
        {IsPay = false;}
        else{IsPay = true;}
        currentUser = FirebaseAuth.getInstance().getCurrentUser();

        TextView AmountTV = findViewById(R.id.Amount);
        TextView DateTV = findViewById(R.id.Date);
        TextView TypeTV = findViewById(R.id.Type);
        TextView NoteTV = findViewById(R.id.Note);
        Button EditBtn = findViewById(R.id.EditBtn);
        Button DeleteBtn = findViewById(R.id.DeleteBtn);

        AmountTV.setText(Amount);
        DateTV.setText(Date);
        TypeTV.setText(Type);
        NoteTV.setText(Note);

        EditBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //DatabaseReference transactionRef = mDatabase.child("Transactions").child(TransID);
                Intent intent = new Intent(TransactionDetailActivity.this, UpdateTransaction.class);
                intent.putExtra("TransID", TransID);
                intent.putExtra("amount", Amount);
                intent.putExtra("date", Date);
                intent.putExtra("note", Note);
                intent.putExtra("GetPAY", Type);
                startActivity(intent);
                finish();
//                startActivity(getIntent());
            }
        });
        DeleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deleteTransaction();
                finish();
            }
        });
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

    public void deleteTransaction()
    {

        currentUser = FirebaseAuth.getInstance().getCurrentUser();
        String userID = currentUser.getUid();

        DatabaseReference transactionRef = mDatabase.child("Transactions").child(TransID);
        transactionRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                // Retrieve the transaction amount
                Double transAmount = dataSnapshot.child("Amount").getValue(Double.class);

                // Delete the transaction from the database
                dataSnapshot.getRef().removeValue();

                // Update the user's balance
                DatabaseReference userRef = mDatabase.child("Users").child(userID);
                userRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        // Retrieve the current balance value
                        Double currentBalance = dataSnapshot.child("balance").getValue(Double.class);
                        if (currentBalance != null) {
                            double newBalance = currentBalance - transAmount;
                            userRef.child("balance").setValue(newBalance);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        // Handle the error condition, if any
                        Toast.makeText(TransactionDetailActivity.this, "Failed to update balance: " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });

                // Show a success message
                Toast.makeText(TransactionDetailActivity.this, "Transaction deleted successfully", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle the error condition, if any
                Toast.makeText(TransactionDetailActivity.this, "Failed to delete transaction: " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}