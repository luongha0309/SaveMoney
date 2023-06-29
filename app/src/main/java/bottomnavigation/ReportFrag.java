package bottomnavigation;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.quanlychitieu.R;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.formatter.ValueFormatter;
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


public class ReportFrag extends Fragment {

    TextView testTxt;
    Button btnBar, btnPie;
    BarChart barChart;

    PieChart pieChart;

    FirebaseAuth mAuth = FirebaseAuth.getInstance();

    FirebaseUser currentUser = mAuth.getCurrentUser();

    DatabaseReference reference = FirebaseDatabase.getInstance().getReference()
            .child("Transactions");

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_report, container, false);
        barChart = view.findViewById(R.id.bar_chart);
        XAxis xAxis = barChart.getXAxis();
        ArrayList<BarEntry> barEntries = new ArrayList<>();
        ArrayList<String> labels = new ArrayList<>();
        labels.add("Income");
        labels.add("Outcome");

        // Retrieve data from Firebase using the user ID
        Query query = reference.orderByChild("UserID").equalTo(currentUser.getUid());
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                barEntries.clear();
                float incomeTotal = 0;
                float outcomeTotal = 0;
                // Loop through the retrieved data and add it to the arrays
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    // Retrieve the value from the snapshot
                    String amount = snapshot.child("Amount").getValue().toString();
                    Boolean pay = snapshot.child("IsPay").getValue(Boolean.class);
                    // Convert the value to float
                    float floatValue = Float.parseFloat(amount);

                    if (pay == false) {
                        incomeTotal += floatValue;
                    } else {
                        outcomeTotal += floatValue;
                    }
                }
                barEntries.add(new BarEntry(0, incomeTotal));
                barEntries.add(new BarEntry(1, outcomeTotal));

                // Update the chart data with the retrieved entries
                BarDataSet barDataSet = new BarDataSet(barEntries, "");
                int greenColor = Color.rgb(0, 255, 0);
                int redColor = Color.rgb(255, 0, 0);
                float textSize = 30f;
                barDataSet.setColors(greenColor, redColor);
                YAxis leftAxis = barChart.getAxisLeft();
                barDataSet.setDrawValues(true);


                // Update the chart data and refresh the chart
                BarData barData = new BarData(barDataSet);
                barChart.setData(barData);
                barChart.getDescription().setEnabled(false);
                barChart.animateXY(1000, 1000);

                XAxis xAxis = barChart.getXAxis();
                xAxis.setValueFormatter(new IndexAxisValueFormatter(labels));
                xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
                xAxis.setGranularity(1f);

                barChart.setFitBars(true);
                barChart.invalidate(); // Refresh the chart

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                System.out.println("Failed to read data: " + databaseError.getMessage());
            }
        });


        return view;
    }

    private class CustomValueFormatter extends ValueFormatter implements com.example.quanlychitieu.CustomValueFormatter {
        private float textSize;
        private DecimalFormat decimalFormat;
        public CustomValueFormatter(float textSize) {
            decimalFormat = new DecimalFormat("#.##");
            this.textSize = textSize;
        }

        @Override
        public String getFormattedValue(float value) {
            return decimalFormat.format(value);
        }

        @Override
        public String getAxisLabel(float value, AxisBase axis) {
            return decimalFormat.format(value);
        }

        @Override
        public void drawValue(Canvas c, String valueText, float x, float y, int color) {

        }

        @Override
        public Paint.Align getAxisLabelAlign() {
            return Paint.Align.LEFT;
        }

        @Override
        public Paint.Align getDrawLabelAlign() {
            return Paint.Align.LEFT;
        }

        @Override
        public int getDecimalDigits() {
            return 2;
        }

        public float getTextSize() {
            return textSize;
        }
    }
}