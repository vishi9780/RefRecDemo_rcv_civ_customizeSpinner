package responsehandling.app.com.refrecdemo.xrecyclerview.progressindicator;


import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Build;
import android.support.annotation.IntDef;
import android.util.AttributeSet;
import android.view.View;

import responsehandling.app.com.refrecdemo.R;
import responsehandling.app.com.refrecdemo.xrecyclerview.progressindicator.indicator.BallClipRotateIndicator;
import responsehandling.app.com.refrecdemo.xrecyclerview.progressindicator.indicator.BallRotateIndicator;
import responsehandling.app.com.refrecdemo.xrecyclerview.progressindicator.indicator.BallSpinFadeLoaderIndicator;
import responsehandling.app.com.refrecdemo.xrecyclerview.progressindicator.indicator.BaseIndicatorController;

public class AVLoadingIndicatorView extends View {
    //indicators

    public static final int BallClipRotate = 2;
    public static final int BallRotate = 7;
    public static final int BallSpinFadeLoader = 22;



    @IntDef(flag = true,
            value = {
                    BallClipRotate,
                    BallRotate,
                    BallSpinFadeLoader
            })
    public @interface Indicator {
    }

    //Sizes (with defaults in DP)
    public static final int DEFAULT_SIZE = 30;

    //attrs
    int mIndicatorId;
    int mIndicatorColor;

    Paint mPaint;

    BaseIndicatorController mIndicatorController;

    private boolean mHasAnimation;

    public AVLoadingIndicatorView(Context context) {
        super(context);
        init(null, 0);
    }

    public AVLoadingIndicatorView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs, 0);
    }

    public AVLoadingIndicatorView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs, defStyleAttr);
    }

    public void destroy() {
        mHasAnimation = true;
        if (mIndicatorController != null) {
            mIndicatorController.destroy();
            mIndicatorController = null;
        }
        mPaint = null;
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public AVLoadingIndicatorView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(attrs, defStyleAttr);
    }

    private void init(AttributeSet attrs, int defStyle) {
        TypedArray a = getContext().obtainStyledAttributes(attrs, R.styleable.AVLoadingIndicatorView);
        mIndicatorId = a.getInt(R.styleable.AVLoadingIndicatorView_indicator, BallClipRotate);
        mIndicatorColor = a.getColor(R.styleable.AVLoadingIndicatorView_indicator_color, Color.WHITE);
        a.recycle();
        mPaint = new Paint();
        mPaint.setColor(mIndicatorColor);
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setAntiAlias(true);
        applyIndicator();
    }

    public void setIndicatorId(int indicatorId) {
        mIndicatorId = indicatorId;
        applyIndicator();
    }

    public void setIndicatorColor(int color) {
        mIndicatorColor = color;
        mPaint.setColor(mIndicatorColor);
        this.invalidate();
    }

    private void applyIndicator() {
        switch (mIndicatorId) {

            case BallClipRotate:
                mIndicatorController = new BallClipRotateIndicator();
                break;
            case BallRotate:
                mIndicatorController = new BallRotateIndicator();
                break;
            case BallSpinFadeLoader:
                mIndicatorController = new BallSpinFadeLoaderIndicator();
                break;

        }
        mIndicatorController.setTarget(this);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int width = measureDimension(dp2px(DEFAULT_SIZE), widthMeasureSpec);
        int height = measureDimension(dp2px(DEFAULT_SIZE), heightMeasureSpec);
        setMeasuredDimension(width, height);
    }

    private int measureDimension(int defaultSize, int measureSpec) {
        int result = defaultSize;
        int specMode = MeasureSpec.getMode(measureSpec);
        int specSize = MeasureSpec.getSize(measureSpec);
        if (specMode == MeasureSpec.EXACTLY) {
            result = specSize;
        } else if (specMode == MeasureSpec.AT_MOST) {
            result = Math.min(defaultSize, specSize);
        } else {
            result = defaultSize;
        }
        return result;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        drawIndicator(canvas);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        if (!mHasAnimation) {
            mHasAnimation = true;
            applyAnimation();
        }
    }

    @Override
    public void setVisibility(int v) {
        if (getVisibility() != v) {
            super.setVisibility(v);
            if (mIndicatorController == null)
                return;
            if (v == GONE || v == INVISIBLE) {
                mIndicatorController.setAnimationStatus(BaseIndicatorController.AnimStatus.END);
            } else {
                mIndicatorController.setAnimationStatus(BaseIndicatorController.AnimStatus.START);
            }
        }
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        if (mIndicatorController == null)
            return;
        mIndicatorController.setAnimationStatus(BaseIndicatorController.AnimStatus.CANCEL);
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        if (mIndicatorController == null)
            return;
        mIndicatorController.setAnimationStatus(BaseIndicatorController.AnimStatus.START);
    }

    void drawIndicator(Canvas canvas) {
        if (mIndicatorController == null)
            return;
        mIndicatorController.draw(canvas, mPaint);
    }

    void applyAnimation() {
        if (mIndicatorController == null)
            return;
        mIndicatorController.initAnimation();
    }

    private int dp2px(int dpValue) {
        return (int) getContext().getResources().getDisplayMetrics().density * dpValue;
    }


}