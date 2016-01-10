package com.cmbb.app.views.widget;

import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;

import com.cmbb.app.R;

/**
 * Created by liman on 2015/12/15.
 */
public class LoadingAnimationView extends View {
    private Context context;
    private Paint borderPaint;
    //变量
    private float boderWidth;
    private int borderColor;
    private int backGoundColor;
    private float centerWidth;
    private int centerColor;
    private boolean needAnimation;
    private final int MAX_WIDTH = 100;
    private final int MAX_HEIGHT = 100;
    private float radius;
    private int clipX = 0;

    public static final int STATUS_LOADING_ANIMATION = 1;//加载中
    public static final int STATUS_SUCESSED_ANIMATION = 2; //加载成功
    public static final int STATUS_FAILED_ANIMATION = 3; //加载失败
    private int ANIMATION_STATUS = STATUS_LOADING_ANIMATION;

    private float sweepAngle = 0;
    private ValueAnimator animator;


    public LoadingAnimationView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        initAttrs(attrs);
        init();
    }

    public void setLoadingStatus(int status) {
        this.ANIMATION_STATUS = status;
        sweepAngle = 0;
        clipX = 0;
        invalidate();
    }

    private void initAttrs(AttributeSet attrs) {
        TypedArray mTypedArray = context.obtainStyledAttributes(attrs, R.styleable.resultView);
        boderWidth = mTypedArray.getDimension(R.styleable.resultView_circleBorderWidth, 2);
        borderColor = mTypedArray.getColor(R.styleable.resultView_borderColor, Color.WHITE);
        centerWidth = mTypedArray.getDimension(R.styleable.resultView_centerWidth, 2);
        centerColor = mTypedArray.getInt(R.styleable.resultView_centerColor, Color.WHITE);
        needAnimation = mTypedArray.getBoolean(R.styleable.resultView_needAnimation, true);
        radius = mTypedArray.getDimension(R.styleable.resultView_radius, 20);
    }

    private void init() {
        borderPaint = new Paint();
        borderPaint.setColor(Color.WHITE);
        borderPaint.setAntiAlias(true);
        borderPaint.setStrokeWidth(boderWidth);
        borderPaint.setStyle(Paint.Style.STROKE);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int width = MeasureSpec.getSize(widthMeasureSpec);
        int height = MeasureSpec.getSize(heightMeasureSpec);
        setMeasuredDimension(width, height);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        int w = getWidth();
        int h = getHeight();
        int t_r = w > h ? h : w;
        if (t_r < radius * 2) {
            radius = t_r / 2 - boderWidth;
            if (radius < 0) {
                radius = 0;
            }
        }
        switch (ANIMATION_STATUS) {
            case STATUS_LOADING_ANIMATION:
                drawLoading(canvas);
                break;
            case STATUS_SUCESSED_ANIMATION:
                drawSucess(canvas);
                break;
            case STATUS_FAILED_ANIMATION:
                drawFailed(canvas);
                break;
        }
    }

    private void drawLoading(Canvas canvas) {
        int w = getWidth();
        int h = getHeight();
        borderPaint.setColor(borderColor);
        canvas.drawCircle(w / 2, h / 2, radius, borderPaint);

        float paddingLeft = (w - radius * 2) / 2;
        float paddingTop = (h - radius * 2) / 2;

        borderPaint.setColor(0xFF00AEFF);
        canvas.save();
        canvas.rotate(sweepAngle, w / 2, h / 2);
        canvas.drawArc(new RectF(paddingLeft, paddingTop, paddingLeft + radius * 2, paddingTop + radius * 2), 0, 60, false, borderPaint);
        canvas.restore();
        invalidate();
        sweepAngle += 5;
//        startAnimation();
    }

    private void startAnimation() {
        if (animator == null) {
            animator = new ValueAnimator();
            animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    float fraction = animation.getAnimatedFraction();
                    sweepAngle += fraction;
                    if (sweepAngle >= 360) {
                        sweepAngle = 0;
                    }
                    invalidate();
                }
            });
            animator.setInterpolator(new AccelerateDecelerateInterpolator());
            animator.setDuration(2000);
            animator.start();
        }
    }

    private void drawFailed(Canvas canvas) {
        borderPaint.setColor(borderColor);
        int w = getWidth();
        int h = getHeight();
        canvas.drawCircle(w / 2, h / 2, radius, borderPaint);

        borderPaint.setColor(centerColor);
        //找拐角点
        float paddingLeft = (w - radius * 2) / 2;
        float r_w = radius / 2;

        float l_t_x = paddingLeft + r_w;

        canvas.save();
        canvas.rotate(45, w / 2, h / 2);
        canvas.drawLine(l_t_x, h / 2, l_t_x + radius, h / 2, borderPaint);
        canvas.restore();
        canvas.save();
        canvas.rotate(-45, w / 2, h / 2);
        canvas.drawLine(l_t_x, h / 2, l_t_x + radius, h / 2, borderPaint);
        canvas.restore();
    }

    /**
     * 画对勾
     *
     * @param canvas
     */
    private void drawSucess(Canvas canvas) {
        borderPaint.setColor(borderColor);
        int w = getWidth();
        int h = getHeight();
        canvas.drawCircle(w / 2, h / 2, radius, borderPaint);

        borderPaint.setColor(centerColor);

        //找拐角点
        float paddingLeft = (w - radius * 2) / 2;
        float paddingTop = (h - radius * 2) / 2;
        float c_x = paddingLeft + (radius * 2 / 5) * 2;
        float c_y = paddingTop + (radius * 2 / 3) * 2;
//        canvas.drawCircle(c_x, c_y, 2, borderPaint);

        float l_x = paddingLeft + (radius * 2 / 5) * 1;
        float l_y = paddingLeft + (radius * 2 / 2);
//        canvas.drawCircle(l_x, l_y, 3, borderPaint);

        float r_x = paddingLeft + (radius * 2 / 5) * 4;
        float r_y = paddingLeft + (radius * 2 / 3);
//        canvas.drawCircle(r_x, r_y, 3, borderPaint);

        canvas.save();
        canvas.clipRect(new Rect((int) l_x - 5, 0, clipX += 5, h));
        canvas.drawLine(l_x, l_y, c_x, c_y, borderPaint);
        canvas.drawLine(c_x, c_y, r_x, r_y, borderPaint);
        canvas.drawCircle(c_x, c_y, 0.5f, borderPaint);
        canvas.restore();
        if (clipX >= r_x) {
            clipX = 0;
        } else {
            invalidate();
        }
    }
}
