package com.example.quanlychitieu;

import android.graphics.Canvas;
import android.graphics.Paint;

public interface CustomValueFormatter {
    void drawValue(Canvas c, String valueText, float x, float y, int color);

    Paint.Align getAxisLabelAlign();

    Paint.Align getDrawLabelAlign();

    int getDecimalDigits();
}
