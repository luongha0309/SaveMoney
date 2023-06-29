package adapter;

import static androidx.core.content.ContextCompat.startActivity;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.quanlychitieu.R;
import com.example.quanlychitieu.TransactionDetailActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.DecimalFormat;
import java.util.List;

import bottomnavigation.TransactionFrag;
import objects.Transaction;

public class TransactionAdapter extends RecyclerView.Adapter<TransactionAdapter.MyHolder> {
    Context context;
    String userID;
    List<Transaction> transactions;

    public TransactionAdapter(Context context, List<Transaction> transactions) {
        this.context = context;
        this.transactions = transactions;
        userID = FirebaseAuth.getInstance().getCurrentUser().getUid();
    }

    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_transaction, parent, false);
        return new MyHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyHolder holder, int position) {

        DecimalFormat f = new DecimalFormat("#,###");

        String amount = String.valueOf(transactions.get(position).getTransAmount());
        String formattedAmount = f.format(Double.parseDouble(amount));
        String date = transactions.get(position).getTransDate();
        String note = transactions.get(position).getTransNote();
        String TransID = transactions.get(position).getTransID();
        String GetPAY;
        holder.transAmount.setText(formattedAmount);
        holder.transDate.setText(date);
        holder.transNote.setText(note);
        if (transactions.get(position).getPay() == false) {
            holder.transImg.setImageResource(R.drawable.income);
            GetPAY = "Earn";
            holder.transAmount.setTextColor(ContextCompat.getColor(context, R.color.green)); // Đặt màu xanh

        } else {
            holder.transImg.setImageResource(R.drawable.outcome);
            GetPAY = "Pay";
            holder.transAmount.setTextColor(ContextCompat.getColor(context, R.color.red)); // Đặt màu đỏ

        }

        holder.transItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Detail(TransID, amount, date, note,GetPAY);
            }
        });
    }

    private void Detail(String TransID, String amount, String date, String note, String GetPAY){
        Intent intent = new Intent(context, TransactionDetailActivity.class );
        intent.putExtra("TransID", TransID);
        intent.putExtra("amount", amount);
        intent.putExtra("date", date);
        intent.putExtra("note", note);
        intent.putExtra("GetPAY", GetPAY);

        context.startActivity(intent);
    }

    @Override
    public int getItemCount() {
        return transactions.size();
    }

    class MyHolder extends RecyclerView.ViewHolder {
        ImageView transImg;
        TextView transAmount, transDate, transNote;
        LinearLayout transItem;

        public MyHolder(@NonNull View itemView) {
            super(itemView);
            transImg = itemView.findViewById(R.id.item_image_trans);
            transAmount = itemView.findViewById(R.id.item_text_trans);
            transDate = itemView.findViewById(R.id.item_text_trans_2);
            transNote = itemView.findViewById(R.id.item_text_trans_3);
            transItem = itemView.findViewById(R.id.trans_linearlayout);
        }
    }
}
