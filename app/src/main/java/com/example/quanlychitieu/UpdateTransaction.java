package com.example.quanlychitieu;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;

import bottomnavigation.TransactionFrag;
import objects.Transaction;
import objects.User;

public class UpdateTransaction extends AppCompatActivity {

    String TransIDEdit;
    EditText amountEdit;
    EditText dateEdit;
    EditText noteEdit;
    EditText groupOfTransEdit;
    Calendar calendar;
    String[] options = {
            "Pay",
            "Earn"
    };
    User user;
    FirebaseUser currentUser;
    DatabaseReference mDatabase;

    Transaction transaction;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_transaction);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.lavender)));

        calendar = Calendar.getInstance();

        amountEdit = findViewById(R.id.transactionAmountEdit);
        dateEdit = findViewById(R.id.dateOfTransactionEdit);
        noteEdit = findViewById(R.id.transNoteEdit);
        groupOfTransEdit = findViewById(R.id.groupOfTransactionEdit);

        mDatabase = FirebaseDatabase.getInstance().getReference();

        String TransID = getIntent().getStringExtra("TransID");
        Double Amount = Double.valueOf(getIntent().getStringExtra("amount"));
        String Date =  getIntent().getStringExtra("date");
        String Note =  getIntent().getStringExtra("note");
        Boolean isPay = Boolean.valueOf(getIntent().getStringExtra("isPay"));

        currentUser = FirebaseAuth.getInstance().getCurrentUser();
        String email = Objects.requireNonNull(currentUser). getEmail();
        String userID = currentUser.getUid();
        user = new User(email, userID);

        transaction = new Transaction(TransID, Amount, isPay, Note, Date, user.userID);

        amountEdit.setText(String.valueOf(transaction.getTransAmount()));
        dateEdit.setText(transaction.getTransDate());
        noteEdit.setText(transaction.getTransNote());
        if (String.valueOf(transaction.getPay()) == "false"){
            groupOfTransEdit.setText("Earn");
        }
        else {
            groupOfTransEdit.setText("Pay");
        }


        dateEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDateTimePicker();
            }
        });

        groupOfTransEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showOptionsDialog();
            }
        });

    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.save, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                // Handle back button click here
                finish();
                return true;
            case R.id.action_save:
                updateTransaction();
                finish();
//                showUserTransactionData();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }


    public void showOptionsDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Select group of transaction")
                .setItems(options, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String selectedOption = options[which];
                        groupOfTransEdit.setText(selectedOption);
                        dialog.dismiss();
                    }
                });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    public void showDateTimePicker(){
        DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                calendar.set(Calendar.YEAR, year);
                calendar.set(Calendar.MONTH, month);
                calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

                SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
                String selectedDate = dateFormat.format(calendar.getTime());
                dateEdit.setText(selectedDate);
            }
        };

        DatePickerDialog datePickerDialog = new DatePickerDialog(
                this,
                dateSetListener,
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
        );
        datePickerDialog.show();
    }

    private void updateTransaction() {
        String transAmountString = amountEdit.getText().toString();
        double transAmount = Double.parseDouble(transAmountString);
        boolean isPay = groupOfTransEdit.getText().toString().equals("Pay");
        String transNote = noteEdit.getText().toString();
        String transDate = dateEdit.getText().toString();
        String transID = transaction.transID;
        String userID = user.userID;

        DatabaseReference transactionRef = mDatabase.child("Transactions").child(transID);
        transactionRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                // Retrieve the previous transaction amount
                Double previousAmount = dataSnapshot.child("Amount").getValue(Double.class);

                // Calculate the difference between the new and previous amounts
                double amountDifference = transAmount - previousAmount;

                // Update the transaction data
                dataSnapshot.getRef().child("Amount").setValue(transAmount);
                dataSnapshot.getRef().child("IsPay").setValue(isPay);
                dataSnapshot.getRef().child("Note").setValue(transNote);
                dataSnapshot.getRef().child("Date").setValue(transDate);

                // Update the user's balance
                DatabaseReference userRef = mDatabase.child("Users").child(userID);
                userRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        // Retrieve the current balance value
                        Double currentBalance = dataSnapshot.child("balance").getValue(Double.class);
                        if (currentBalance != null) {
                            double newBalance = currentBalance + amountDifference;
                            userRef.child("balance").setValue(newBalance);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        // Handle the error condition, if any
                        Toast.makeText(UpdateTransaction.this, "Failed to update balance: " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });

                // Show a success message
                Toast.makeText(UpdateTransaction.this, "Transaction updated successfully", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle the error condition, if any
                Toast.makeText(UpdateTransaction.this, "Failed to update transaction: " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }


}