package com.example.clockview;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.os.Handler;
import android.os.Message;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import java.util.Calendar;

/**
 * Created by ruedy on 2017/3/14.
 */

public class ClockView extends View {

    private String name = "OMEGA";
    private String namepai = "Ω";
    private String love = "I'll love you";
    private String love2 = "forever";
    private Paint panPaint;
    private TextPaint textPaint;


    private int viewWidth;
    private int viewHeight;

    private int hour = 9;
    private int minute = 30;
    private int second = 55;

    private int textsize = 0;
    private Rect rect;
    private float textwidth;
    private int textheight;
    private int paiwidth;
    private int paiheight;
    private int love1width;
    private int love2width;
    private int data1;

    private Handler handler = new Handler() {


        @Override
        public void handleMessage(Message msg) {
            Calendar calendar = Calendar.getInstance();

            hour = calendar.get(Calendar.HOUR);
            minute = calendar.get(Calendar.MINUTE);
            second = calendar.get(Calendar.SECOND);
            data1 = calendar.get(Calendar.DAY_OF_MONTH);

            invalidate();

            sendEmptyMessageDelayed(1, 1000);

        }
    };

    public ClockView(Context context) {
        this(context, null);
    }

    public ClockView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ClockView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        handler.sendEmptyMessage(1);

    }

    private void initpaint() {
        panPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        textPaint = new TextPaint(Paint.ANTI_ALIAS_FLAG);
        textPaint.setColor(Color.WHITE);
        textPaint.setTextSize(textsize);

        rect = new Rect();

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
        textsize = viewWidth / 32;
        initpaint();

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);


        //画出蓝绿色表盘内测
        panPaint.setColor(getResources().getColor(R.color.biaopan));
        canvas.drawCircle(getWidth() / 2, getHeight() / 2, (float) (getWidth() / 2 - textsize * 0.8), panPaint);
        Log.e("textsize", "onDraw: " + textsize);
        //画出蓝绿色表盘外侧
        panPaint.setColor(getResources().getColor(R.color.biaopanwai));
        panPaint.setStrokeWidth((float) (textsize * 1.4));
        panPaint.setStyle(Paint.Style.STROKE);//属性空心
        canvas.drawCircle(getWidth() / 2, getHeight() / 2, (float) (getWidth() / 2 - textsize * 0.8), panPaint);


        //画出omega文字
        textPaint.setTextSize(textsize * 3);
        textPaint.getTextBounds(namepai, 0, 1, rect);
        paiwidth = rect.width();
        paiheight = rect.height();
        textPaint.getTextBounds(name, 0, name.length(), rect);
        textwidth = rect.width();
        textheight = rect.height();

        Log.e("textwidth", "textwidth: " + textwidth + ",viewWidth:" + viewWidth);
//        canvas.drawText(namepai, viewWidth / 2 - paiwidth / 2, (float) (textsize * 8), textPaint);
// 感觉画上去不好看
        canvas.drawText(name, viewWidth / 2 - textwidth / 2, (float) (textsize * 11), textPaint);

//写上爱你一万年
        textPaint.setTextSize((float) (textsize * 1.1));
        textPaint.getTextBounds(love, 0, love.length(), rect);
        love1width = rect.width();

        textPaint.getTextBounds(love2, 0, love2.length(), rect);
        love2width = rect.width();
        Typeface font = Typeface.create(Typeface.SANS_SERIF, Typeface.ITALIC);

        textPaint.setTypeface(font);
        canvas.drawText(love, viewWidth / 2 - love1width / 2, viewHeight / 2 + textsize * 5, textPaint);
        canvas.drawText(love2, viewWidth / 2 - love2width / 2, viewHeight / 2 + textsize * 6
                + rect.height() / 2, textPaint);

        //画日期

        rect.set(viewWidth - textsize * 8, viewWidth / 2 - textsize, viewWidth - textsize * 5, viewWidth / 2 + textsize);
        panPaint.setColor(Color.BLACK);
        panPaint.setStyle(Paint.Style.FILL);
        canvas.drawRect(rect, panPaint);
        String s = String.valueOf(data1);
        textPaint.getTextBounds(s, 0, s.length(), rect);
        canvas.drawText(s, (float) (viewWidth - textsize * 6.5 - (rect.width() / 1.9)), viewWidth / 2 + rect.height() / 2, textPaint);
        textPaint.setTypeface(Typeface.DEFAULT);
        textPaint.setTextSize(textsize);


        //画刻度
        panPaint.setColor(Color.WHITE);
        panPaint.setStrokeWidth(textsize / 8);
        panPaint.setStyle(Paint.Style.FILL);
        for (int i = 0; i < 60; i++) {
            if (i % 5 != 0) {
                panPaint.setStrokeWidth(textsize / 8);
                Log.e("i", "onDraw: " + i);
                canvas.save();  //位移,透明度，旋转之前先保存状态
                canvas.rotate(i * 6, getWidth() / 2, getHeight() / 2);
                canvas.drawLine((float) (textsize * 0.25), getHeight() / 2, (float) (textsize * 1.2), getHeight() / 2, panPaint);
                canvas.restore();  //重置画布状态
            } else {
                int j = 0;
                if (i == 0) {
                    j = 60;
                } else {
                    j = i;
                }
                canvas.save();  //位移,透明度，旋转之前先保存状态
                canvas.rotate((i / 5) * 30, getWidth() / 2, getHeight() / 2);
                if (j == 5) {
                    canvas.drawText("0" + j, (float) (getWidth() / 2 - textsize * 0.5), (float) (textsize * 1.1), textPaint);
                } else
                    canvas.drawText("" + j, (float) (getWidth() / 2 - textsize * 0.5), (float) (textsize * 1.1), textPaint);


                //里面粗指针,就不画三角形了

                panPaint.setStrokeWidth((float) (textsize * 0.5));
                canvas.drawLine((float) (textsize * 0.8) * 2, getHeight() / 2, (float) (textsize * 0.8 * 2 + (float) (textsize * 2)), getHeight() / 2, panPaint);

                canvas.restore();//重置画布状态


            }
        }


        //画时针
        panPaint.setStrokeWidth((float) (textsize * 0.25));
        canvas.save();
        canvas.rotate(360 / 12 * hour + 90 + minute * 0.5f, viewWidth / 2, viewWidth / 2);
        canvas.drawLine(viewHeight / 2 - viewWidth / 5, viewWidth / 2,
                viewWidth / 2, viewWidth / 2, panPaint);
        canvas.restore();

        //画分针
        panPaint.setStrokeWidth(3);
        canvas.save();
        canvas.rotate(360 / 60 * minute + 90, viewWidth / 2, viewWidth / 2);
        canvas.drawLine(viewHeight / 2 - viewWidth / 4, viewWidth / 2,
                viewWidth / 2, viewWidth / 2, panPaint);
        canvas.restore();

        //画秒针
        panPaint.setStrokeWidth(3);
        canvas.save();
        canvas.rotate(360 / 60 * second + 90, viewWidth / 2, viewWidth / 2);
        canvas.drawLine(viewHeight / 2 - viewWidth / 3, viewWidth / 2,
                viewWidth / 2, viewWidth / 2, panPaint);
        canvas.restore();

    }


}
