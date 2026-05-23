package com.example.myapplication.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;

public class SideBar extends View {
    private OnTouchingLetterChangedListener onTouchingLetterChangedListener;
    public static String[] b = {"A", "B", "C", "D", "E", "F", "G", "H", "I",
            "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V",
            "W", "X", "Y", "Z", "#"};
    private int choose = -1;
    private final Paint paint = new Paint();
    private TextView mTextDialog;

    public void setTextView(TextView mTextDialog) {
        this.mTextDialog = mTextDialog;
    }

    public SideBar(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public SideBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        // Removed setAlpha(0f) to make it always visible
    }

    public SideBar(Context context) {
        super(context);
    }

    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        int height = getHeight();
        int width = getWidth();
        int singleHeight = height / b.length;
        float density = getResources().getDisplayMetrics().density;

        for (int i = 0; i < b.length; i++) {
            paint.setAntiAlias(true);
            paint.setTypeface(Typeface.DEFAULT_BOLD);
            paint.setColor(Color.BLACK); // Use Black or dark grey for better visibility on light background
            
            float textSize;
            int alpha;
            
            if (choose != -1) {
                paint.setColor(Color.parseColor("#3399ff")); // Blue when selected
                int distance = Math.abs(i - choose);
                switch (distance) {
                    case 0:
                        textSize = 32f * density;
                        alpha = 255;
                        break;
                    case 1:
                        textSize = 22f * density;
                        alpha = 180;
                        break;
                    case 2:
                        textSize = 16f * density;
                        alpha = 100;
                        break;
                    default:
                        textSize = 12f * density;
                        alpha = 50;
                        break;
                }
            } else {
                textSize = 12f * density;
                alpha = 150; // Semi-transparent
            }

            paint.setTextSize(textSize);
            paint.setAlpha(alpha);
            
            float xPos = width / 2f - paint.measureText(b[i]) / 2f;
            float yPos = singleHeight * (i + 1);
            canvas.drawText(b[i], xPos, yPos, paint);
        }
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        final int action = event.getAction();
        final float y = event.getY();
        final int oldChoose = choose;
        final OnTouchingLetterChangedListener listener = onTouchingLetterChangedListener;
        final int c = (int) (y / getHeight() * b.length);

        switch (action) {
            case MotionEvent.ACTION_UP:
                setBackgroundColor(Color.TRANSPARENT);
                choose = -1;
                invalidate();
                if (mTextDialog != null) {
                    mTextDialog.setVisibility(View.INVISIBLE);
                }
                break;

            case MotionEvent.ACTION_DOWN:
                setBackgroundColor(Color.parseColor("#40000000"));
            default:
                if (oldChoose != c) {
                    if (c >= 0 && c < b.length) {
                        if (listener != null) {
                            listener.onTouchingLetterChanged(b[c]);
                        }
                        if (mTextDialog != null) {
                            mTextDialog.setText(b[c]);
                            mTextDialog.setVisibility(View.VISIBLE);
                        }
                        choose = c;
                        invalidate();
                    }
                }
                break;
        }
        return true;
    }

    public void setOnTouchingLetterChangedListener(
            OnTouchingLetterChangedListener onTouchingLetterChangedListener) {
        this.onTouchingLetterChangedListener = onTouchingLetterChangedListener;
    }

    public interface OnTouchingLetterChangedListener {
        void onTouchingLetterChanged(String s);
    }
}
