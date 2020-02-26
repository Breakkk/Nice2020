package com.xilinzhang.ocr.camear;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

public class CustomReferenceLine extends View {

    private Paint mLinePaint;
    private int mMeasureHeight;
    private int mMeasureWidth;

    public CustomReferenceLine(Context context) {
        this(context, null);
    }

    public CustomReferenceLine(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CustomReferenceLine(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        mLinePaint = new Paint();
        mLinePaint.setAntiAlias(true);
        mLinePaint.setColor(Color.parseColor("#ffffffff"));
        mLinePaint.setStrokeWidth(1);
    }


    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mMeasureHeight = getMeasuredHeight();
        mMeasureWidth = getMeasuredWidth();

    }

    @Override
    protected void onDraw(Canvas canvas) {
        int width = mMeasureWidth / 3;
        int height = mMeasureHeight / 3;

        for (int i = width, j = 0; i < mMeasureWidth && j < 2; i += width, j++) {
            canvas.drawLine(i, 0, i, mMeasureHeight, mLinePaint);
        }
        for (int j = height, i = 0; j < mMeasureHeight && i < 2; j += height, i++) {
            canvas.drawLine(0, j, mMeasureWidth, j, mLinePaint);
        }
    }
}
