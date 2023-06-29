package bottomnavigation;

import android.app.DownloadManager;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.provider.ContactsContract;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

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
import java.util.Calendar;


public class HomeFrag extends Fragment {

    TextView balanceTextView, showUserTxt;
    ImageView imageView;

    DatabaseReference transactionRef = FirebaseDatabase.getInstance().getReference("Users");
    FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        balanceTextView = view.findViewById(R.id.balanceTxt);
        showUserTxt = view.findViewById(R.id.showUserTxt);
        imageView = view.findViewById(R.id.greetImg);

        // Hide the app title bar
        AppCompatActivity activity = (AppCompatActivity) getActivity();
        if (activity != null && activity.getSupportActionBar() != null) {
            activity.getSupportActionBar().hide();
        }

        Query query = transactionRef.orderByChild("uid").equalTo(currentUser.getUid());
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot transactionSnapshot : snapshot.getChildren()) {
                    double userBalance = transactionSnapshot.child("balance").getValue(Double.class);
                    String email = transactionSnapshot.child("email").getValue().toString();
                    DecimalFormat f = new DecimalFormat("#,###");
                    String formattedBalance = f.format(userBalance);
                    balanceTextView.setText(formattedBalance);
                    Calendar calendar = Calendar.getInstance();
                    int hour = calendar.get(Calendar.HOUR_OF_DAY);
                    int backgroundImg;

                    String greeting;
                    if (hour >= 6 && hour < 11) {
                        greeting = "Good Morning! ";
                        backgroundImg = R.drawable.morning_background;
                    } else if (hour >= 12 && hour < 17) {
                        greeting = "Good Afternoon! ";
                        backgroundImg = R.drawable.afternoon_background;
                    } else{
                        greeting = "Good Evening! ";
                        backgroundImg = R.drawable.evening_background;
                    }

                    view.setBackgroundResource(backgroundImg);
                    showUserTxt.setText(greeting  + email);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        return view;
    }
}