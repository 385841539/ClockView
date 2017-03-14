package com.example.clockview;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.View;

import java.util.Calendar;

/**
 * Created by axing on 16/9/26.
 */
public class MyClockView extends View {

    private Paint paint;

    private int viewWidth;
    private int viewHeight;

    private int hour = 9;
    private int minute = 30;
    private int second = 55;

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            Calendar calendar = Calendar.getInstance();

            hour = calendar.get(Calendar.HOUR);
            minute = calendar.get(Calendar.MINUTE);
            second = calendar.get(Calendar.SECOND);

            invalidate();

            sendEmptyMessageDelayed(1, 1000);
        }
    };

    public MyClockView(Context context, AttributeSet attrs) {
        super(context, attrs);

        initPaint();

        handler.sendEmptyMessage(1);
    }

    private void initPaint() {
        paint = new Paint();
        paint.setAntiAlias(true);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        viewWidth = MeasureSpec.getSize(widthMeasureSpec);
        viewHeight = MeasureSpec.getSize(heightMeasureSpec);
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);

        switch (widthMode) {
            case MeasureSpec.EXACTLY:
                viewWidth = viewHeight = Math.min(viewHeight, viewWidth);
                setMeasuredDimension(viewWidth, viewHeight);
                break;
            case MeasureSpec.AT_MOST:
                float density = getResources().getDisplayMetrics().density;

                viewWidth = viewHeight = (int) (100 * density);

                setMeasuredDimension(viewWidth, viewHeight);
                break;
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        //画表针上方的轴心
        paint.setColor(Color.GRAY);
        canvas.drawCircle(getWidth() / 2, getWidth() / 2, 5, paint);

        //画外层表盘
        paint.setStyle(Paint.Style.STROKE);
        paint.setColor(Color.DKGRAY);
        canvas.drawCircle(viewWidth / 2, viewWidth / 2, viewWidth / 2, paint);

        //画内层表盘
        canvas.drawCircle(viewWidth / 2, viewWidth / 2, viewWidth / 2 - 15, paint);

        //画刻度
        for (int i = 0; i < 12; i++) {
            canvas.save();  //位移,透明度，旋转之前先保存状态
            canvas.rotate(i * 30, viewWidth / 2, viewWidth / 2);
            canvas.drawLine(15, viewWidth / 2, 30, viewWidth / 2, paint);
            canvas.restore();  //重置画布状态
        }

        //画时针
        paint.setStrokeWidth(5);
        canvas.save();
        canvas.rotate(360 / 12 * hour + 90 + minute * 0.5f, viewWidth / 2, viewWidth / 2);
        canvas.drawLine(viewHeight / 2 - viewWidth / 5, viewWidth / 2,
                viewWidth / 2, viewWidth / 2, paint);
        canvas.restore();

        //画分针
        paint.setStrokeWidth(3);
        canvas.save();
        canvas.rotate(360 / 60 * minute + 90, viewWidth / 2, viewWidth / 2);
        canvas.drawLine(viewHeight / 2 - viewWidth / 4, viewWidth / 2,
                viewWidth / 2, viewWidth / 2, paint);
        canvas.restore();

        //画秒针
        paint.setStrokeWidth(3);
        canvas.save();
        canvas.rotate(360 / 60 * second + 90, viewWidth / 2, viewWidth / 2);
        canvas.drawLine(viewHeight / 2 - viewWidth / 3, viewWidth / 2,
                viewWidth / 2, viewWidth / 2, paint);
        canvas.restore();
    }
}













