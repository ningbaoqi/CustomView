package ningbaoqi.com.customviewother;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;

import ningbaoqi.com.customview.R;

/**
 * =========================================
 * <p/>
 * 版    权: ningxiansheng
 * <p/>
 * 作    者: 宁宝琪
 * <p/>
 * 版    本:1.0
 * <p/>
 * 创建 时间:18-5-10
 * <p/>
 * 描    述:
 * <p/>
 * <p/>
 * 修订 历史:
 * <p/>
 * =========================================
 */

public class RoundProgresBarWithProgress extends HorizontalProgressBarWithProgress {
    private int mRadius = dp2px(30);
    private int mMaxPaintWidth;

    public RoundProgresBarWithProgress(Context context) {
        this(context, null);
    }

    public RoundProgresBarWithProgress(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public RoundProgresBarWithProgress(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mReachHeight = (int) (mUnReachHeight * 2.5f);
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.RoundProgresBarWithProgress);
        mRadius = (int) typedArray.getDimension(R.styleable.RoundProgresBarWithProgress_radius, mRadius);
        typedArray.recycle();
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setAntiAlias(true);
        mPaint.setDither(true);
        mPaint.setStrokeCap(Paint.Cap.ROUND);
    }

    @Override
    protected synchronized void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        mMaxPaintWidth = Math.max(mReachHeight, mUnReachHeight);
        int expect = mRadius * 2 + mMaxPaintWidth + getPaddingRight() + getPaddingLeft();//默认四个padding一致
        int width = resolveSize(expect, widthMeasureSpec);
        int height = resolveSize(expect, heightMeasureSpec);
        int readWidht = Math.min(width, height);//因为用户设置的属性可能宽度和高度不一致，所以需要拿到最小值
        mRadius = (readWidht - getPaddingRight() - getPaddingLeft() - mMaxPaintWidth) / 2;
        setMeasuredDimension(readWidht, readWidht);
    }

    @Override
    protected synchronized void onDraw(Canvas canvas) {
        String text = getProgress() + "%";
        float textWidth = mPaint.measureText(text);
        float textHeight = (mPaint.descent() + mPaint.ascent()) / 2;
        canvas.save();
        canvas.translate(getPaddingLeft() + mMaxPaintWidth / 2, getPaddingTop() + mMaxPaintWidth / 2);
        mPaint.setStyle(Paint.Style.STROKE);
        /**
         * draw unreach bar
         * */
        mPaint.setColor(mUnReachColor);
        mPaint.setStrokeWidth(mUnReachHeight);
        canvas.drawCircle(mRadius, mRadius, mRadius, mPaint);
        /**
         *  draw reach bar
         */
        mPaint.setColor(mReachColor);
        mPaint.setStrokeWidth(mReachHeight);
        float sweepAngle = getProgress() * 1.0f / getMax() * 360;//绘制的度数
        canvas.drawArc(new RectF(0, 0, mRadius * 2, mRadius * 2), 0, sweepAngle, false, mPaint);
        /**
         * draw text
         */
        mPaint.setColor(mTextColor);
        mPaint.setStyle(Paint.Style.FILL);
        canvas.drawText(text, mRadius - textWidth / 2, mRadius - textHeight, mPaint);
        canvas.restore();
    }
}
