package bottomnavigation;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.quanlychitieu.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import adapter.TransactionAdapter;
import objects.Transaction;
import objects.User;

public class TransactionFrag extends Fragment {

    List<Transaction>  listTrans;

    TransactionAdapter transactionAdapter;

    RecyclerView recyclerView;

    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    FirebaseUser currentUser = mAuth.getCurrentUser();



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View view = inflater.inflate(R.layout.fragment_transaction, container, false);
        recyclerView = view.findViewById(R.id.list_trans);
        listTrans = new ArrayList<>();
        loadRecyclerView();

        DatabaseReference databaseReference = FirebaseDatabase.getInstance()
                .getReference().child("Transactions");
        Query query = databaseReference.orderByChild("UserID").equalTo(currentUser.getUid());
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                    double amount = dataSnapshot1.child("Amount").getValue(Double.class);
                    String date = dataSnapshot1.child("Date").getValue().toString();
                    String note = dataSnapshot1.child("Note").getValue().toString();
                    Boolean isPay = dataSnapshot1.child("IsPay").getValue(Boolean.class);
                    String TransID = dataSnapshot1.getKey();

                    DecimalFormat f = new DecimalFormat("#,###");

                    Transaction transaction = new Transaction();
                    transaction.setTransAmount(amount);
                    transaction.setTransDate(date);
                    transaction.setTransNote(note);
                    transaction.setPay(isPay);
                    transaction.setTransID(TransID);
                    listTrans.add(transaction);

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                System.out.println("Failed to read data: " + databaseError.getMessage());
            }
        });

        return view;
    }

    private void loadRecyclerView() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setReverseLayout(true);
        layoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(layoutManager);

        DatabaseReference databaseReference = FirebaseDatabase.getInstance()
                .getReference().child("Transactions");
        Query query = databaseReference.orderByChild("UserID").equalTo(currentUser.getUid());
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                listTrans.clear();
                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                    Transaction transaction = dataSnapshot1.getValue(Transaction.class);
//                    listTrans.add(transaction);
                    transactionAdapter = new TransactionAdapter(getActivity(), listTrans);
                    recyclerView.setAdapter(transactionAdapter);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
    }

}