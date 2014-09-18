package com.droidsoft.pnrtracker.ui.views;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

import com.droidsoft.pnrtracker.R;

/**
 * Created by mitesh.patel on 18-09-2014.
 */
public class PieChartView extends View {

    Paint waitingPaint;
    Paint racPaint;
    Paint confirmedPaint;
    RectF rect;
    private float passengerCount = 0;
    private float racCount = 0;
    private float waitingCount = 0;

    public PieChartView(Context context) {
        super(context);
        init();
    }

    public PieChartView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public PieChartView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    private void init() {
        waitingPaint = new Paint();
        waitingPaint.setColor(getContext().getResources().getColor(R.color.red));
        waitingPaint.setAntiAlias(true);
        waitingPaint.setStyle(Paint.Style.FILL);

        racPaint = new Paint();
        racPaint.setColor(getContext().getResources().getColor(R.color.orange));
        racPaint.setAntiAlias(true);
        racPaint.setStyle(Paint.Style.FILL);

        confirmedPaint = new Paint();
        confirmedPaint.setColor(getContext().getResources().getColor(R.color.green));
        confirmedPaint.setAntiAlias(true);
        confirmedPaint.setStyle(Paint.Style.FILL);
        rect = new RectF();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        //draw background circle anyway
        int left = 0;
        int width = getWidth();
        int top = 0;
        rect.set(left, top, left + width, top + width);
        if (passengerCount != 0) {
            canvas.drawArc(rect, -90, 360, true, confirmedPaint);
            float waitingAngle = 0.0f;

            if (waitingCount != 0) {
                waitingAngle = (360 * (waitingCount / passengerCount));
                canvas.drawArc(rect, -90, (360 * (waitingCount / passengerCount)), true, waitingPaint);
            }

            if (racCount != 0) {
                canvas.drawArc(rect, -90 + waitingAngle, (360 * (racCount / passengerCount)), true, racPaint);
            }
        }
    }

    public void setPassengerStatus(int passengerCount, int racCount, int waitingCount) {
        this.passengerCount = passengerCount;
        this.racCount = racCount;
        this.waitingCount = waitingCount;
        invalidate();
    }
}
