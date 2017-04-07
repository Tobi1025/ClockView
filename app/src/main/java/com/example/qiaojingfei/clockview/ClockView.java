package com.example.qiaojingfei.clockview;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

/**
 * description: 自定义表盘
 * autour: qiaojingfei
 * date: 2017/4/6 下午3:21
 */
public class ClockView extends View {
    private int mWidth;
    private int mHeight;
    private Context mContext;
    private Paint dialPaint;//表盘画笔
    private Paint scalePaint;//表盘刻度画笔
    private Paint needlePaint;//指针画笔
    private int radius;//表盘半径
    private int dialColor = Color.YELLOW;//表盘外圈颜色
    private int scaleColor = Color.WHITE;//刻度颜色
    private int needleColor = Color.RED;//指针颜色
    private int scaleLength;//刻度长度
    private int max = 60;
    private float curTime;//当前时间
    private float textSize;
    private float cx;//表盘中心x坐标
    private float cy;//表盘中心y坐标

    public ClockView(Context context) {
        this(context, null);
    }

    public ClockView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ClockView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
        init(context, attrs);
        initDialPaint();
        initScalePaint();
        initNeedlePaint();
    }

    private void initAttrs(TypedArray typedArray) {
        int defaultRadius = DimensionUtils.getSizeInPixels(80, mContext);
        int attrRadius = typedArray.getInteger(R.styleable.ClockView_clock_radius, defaultRadius);
        radius = DimensionUtils.getSizeInPixels(attrRadius, mContext);
        dialColor = typedArray.getColor(R.styleable.ClockView_dial_color, dialColor);
        scaleColor = typedArray.getColor(R.styleable.ClockView_scale_color, scaleColor);
        needleColor = typedArray.getColor(R.styleable.ClockView_needle_color, needleColor);
    }

    private void init(Context context, AttributeSet attrs) {
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.ClockView);
        initAttrs(typedArray);
        typedArray.recycle();
    }

    private void initNeedlePaint() {
        needlePaint = new Paint();
        needlePaint.setAntiAlias(true);
        needlePaint.setColor(needleColor);
        needlePaint.setStyle(Paint.Style.STROKE);
        int needleWidth = DimensionUtils.getSizeInPixels(2, mContext);
        needlePaint.setStrokeWidth(needleWidth);
    }

    private void initDialPaint() {
        dialPaint = new Paint();
        dialPaint.setAntiAlias(true);
        dialPaint.setColor(dialColor);
        dialPaint.setStyle(Paint.Style.STROKE);
        dialPaint.setStrokeWidth(3);
    }

    private void initScalePaint() {
        scalePaint = new Paint();
        scalePaint.setStyle(Paint.Style.STROKE);
        scalePaint.setColor(scaleColor);
        scalePaint.setAntiAlias(true);
        textSize = DimensionUtils.pixelsToSp(mContext, 12);
        scalePaint.setTextSize(textSize);
        scaleLength = DimensionUtils.getSizeInPixels(10, mContext);
        int strokeWidth = DimensionUtils.getSizeInPixels(2, mContext);
        scalePaint.setStrokeWidth(strokeWidth);
    }

    public void setTime(int progress) {
        curTime = progress;
        invalidate();
        Log.e("curTime===", curTime + "");
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);
        if (widthMode == MeasureSpec.EXACTLY) {
            mWidth = widthSize;
        } else {
            mWidth = DimensionUtils.getSizeInPixels(300, mContext);
        }

        if (heightMode == MeasureSpec.EXACTLY) {
            mHeight = heightSize;
        } else {
            mHeight = DimensionUtils.getSizeInPixels(300, mContext);
        }
        cx = mWidth / 2;
        cy = mHeight / 2;
        setMeasuredDimension(mWidth, mHeight);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        drawDial(canvas);
        canvas.save();
        drawScaleNeedle(canvas);
        canvas.restore();
        drawScaleText(canvas);
        drawNeedle(canvas);
    }

    /**
     * 画指针
     *
     * @param canvas
     */
    private void drawNeedle(Canvas canvas) {
        canvas.rotate(-90, cx, cy);
        canvas.drawLine(cx, cy,
                cx + (float) Math.cos(Math.toRadians(360 * (curTime / max))) * (radius - scaleLength - 10),
                cy + (float) Math.sin(Math.toRadians(360 * (curTime / max))) * (radius - scaleLength - 10),
                needlePaint);
    }

    /**
     * 画刻度数字(分为4段，分别是(12、1、2),(3、4、5),(6、7、8),(9、10、11) )
     *
     * @param canvas
     */
    private void drawScaleText(Canvas canvas) {
        canvas.save();
        for (int i = 12; i > 9; i--) {
            String scaleText = String.valueOf(i == 12 ? 12 : Math.abs(i - 12));
            float textLength = scalePaint.measureText(scaleText);
            canvas.drawText(scaleText, cx - textLength / 2, cy - radius - 10, scalePaint);
            canvas.rotate(360 / 12, cx, cy);
        }
        canvas.restore();

        canvas.save();
        for (int i = 9; i > 6; i--) {
            String scaleText = String.valueOf(Math.abs(i - 12));
            float textLength = scalePaint.measureText(scaleText);
            canvas.drawText(scaleText, cx + radius + 10, cy + textSize / 2, scalePaint);
            canvas.rotate(360 / 12, cx, cy);
        }
        canvas.restore();

        canvas.save();
        for (int i = 6; i > 3; i--) {
            String scaleText = String.valueOf(Math.abs(i - 12));
            float textLength = scalePaint.measureText(scaleText);
            canvas.drawText(scaleText, cx - textLength / 2, cy + radius + textSize + 10, scalePaint);
            canvas.rotate(360 / 12, cx, cy);
        }
        canvas.restore();

        canvas.save();
        for (int i = 3; i > 0; i--) {
            String scaleText = String.valueOf(Math.abs(i - 12));
            float textLength = scalePaint.measureText(scaleText);
            canvas.drawText(scaleText, cx - radius - textLength - 10, cy + textSize / 2, scalePaint);
            canvas.rotate(360 / 12, cx, cy);
        }
        canvas.restore();
    }

    /**
     * 画刻度指针
     *
     * @param canvas
     */
    private void drawScaleNeedle(Canvas canvas) {
        for (int i = 0; i < 12; i++) {
            canvas.drawLine(cx, cy + radius, cx, cy + radius - scaleLength, scalePaint);
            canvas.rotate(360 / 12, cx, cy);
        }
    }

    /**
     * 画表盘
     *
     * @param canvas
     */
    private void drawDial(Canvas canvas) {
        canvas.drawCircle(cx, cy, radius, dialPaint);
    }
}
