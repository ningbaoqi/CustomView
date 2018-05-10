package ningbaoqi.com.customviewother;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.widget.ProgressBar;

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
 * 创建 时间:18-5-9
 * <p/>
 * 描    述:
 * <p/>
 * <p/>
 * 修订 历史:
 * <p/>
 * =========================================
 */

public class HorizontalProgressBarWithProgress extends ProgressBar {
    private static final int DEFAULT_TEXT_SIZE = 10;//sp
    private static final int DEFAULT_TEXT_COLOR = 0XFFFC00D1;
    private static final int DEFAULT_COLOR_UNREACH = 0XFFD3D6DA;
    private static final int DEFAULT_HEIGTH_UNREACH = 2;//dp
    private static final int DEFAULT_COLOR_REACH = DEFAULT_TEXT_COLOR;
    private static final int DEFAULT_HEIGTH_REACH = 2;//dp
    private static final int DEFAULT_TEXT_OFFSET = 10;//dp
    protected int mTextSize = sp2px(DEFAULT_TEXT_SIZE);
    protected int mTextColor = DEFAULT_TEXT_COLOR;
    protected int mUnReachColor = DEFAULT_COLOR_UNREACH;
    protected int mUnReachHeight = dp2px(DEFAULT_HEIGTH_UNREACH);
    protected int mReachColor = DEFAULT_COLOR_REACH;
    protected int mReachHeight = dp2px(DEFAULT_HEIGTH_REACH);
    protected int mTextOffset = dp2px(DEFAULT_TEXT_OFFSET);
    protected Paint mPaint = new Paint();
    protected int mRealWidth;

    public HorizontalProgressBarWithProgress(Context context) {
        this(context, null, 0);
    }

    public HorizontalProgressBarWithProgress(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public HorizontalProgressBarWithProgress(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        obtianStyledAttrs(attrs);
    }

    /**
     * 获取自定义属性
     *
     * @param attrs
     */
    private void obtianStyledAttrs(AttributeSet attrs) {
        TypedArray typedArray = getContext().obtainStyledAttributes(attrs, R.styleable.HorizontalProgressBarWithProgress);
        mTextSize = (int) typedArray.getDimension(R.styleable.HorizontalProgressBarWithProgress_progress_text_size, mTextSize);
        mTextColor = typedArray.getColor(R.styleable.HorizontalProgressBarWithProgress_progress_text_color, mTextColor);
        mTextOffset = (int) typedArray.getDimension(R.styleable.HorizontalProgressBarWithProgress_progress_text_offset, mTextOffset);
        mUnReachColor = typedArray.getColor(R.styleable.HorizontalProgressBarWithProgress_progress_unreach_color, mUnReachColor);
        mUnReachHeight = (int) typedArray.getDimension(R.styleable.HorizontalProgressBarWithProgress_progress_unreach_height, mUnReachHeight);
        mReachColor = typedArray.getColor(R.styleable.HorizontalProgressBarWithProgress_progress_reach_color, mReachColor);
        mReachHeight = (int) typedArray.getDimension(R.styleable.HorizontalProgressBarWithProgress_progress_reach_height, mReachHeight);
        typedArray.recycle();
        mPaint.setTextSize(mTextSize);
    }

    /**
     * 测量
     */
    @Override
    protected synchronized void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int widthVal = MeasureSpec.getSize(widthMeasureSpec);//因为是水平进度跳，所以用户必须要给我设定一个值
        int height = measureHeight(heightMeasureSpec);
        setMeasuredDimension(widthVal, height);//设置宽高，表示确定该View的宽高
        mRealWidth = getMeasuredWidth() - getPaddingLeft() - getPaddingRight();//实际上绘制区域的宽度
    }

    private int measureHeight(int heightMeasureSpec) {
        int result = 0;
        int mode = MeasureSpec.getMode(heightMeasureSpec);
        int size = MeasureSpec.getSize(heightMeasureSpec);
        if (mode == MeasureSpec.EXACTLY) {//精确值
            result = size;
        } else {
            int textHeight = (int) (mPaint.descent() - mPaint.ascent());//字的高度
            result = getPaddingBottom() + getPaddingTop() + Math.max(Math.max(mReachHeight, mUnReachHeight), Math.abs(textHeight));
            if (mode == MeasureSpec.AT_MOST) {//表示测量的值不能超过给定的size值
                result = Math.min(result, size);
            }
        }
        return result;
    }

    protected int dp2px(int dpVal) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dpVal, getResources().getDisplayMetrics());
    }

    private int sp2px(int spVal) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, spVal, getResources().getDisplayMetrics());
    }

    @Override
    protected synchronized void onDraw(Canvas canvas) {
        canvas.save();
        canvas.translate(getPaddingLeft(), getHeight() / 2);//设置开始绘制的位置
        boolean noNeedUnReach = false;
        String text = getProgress() + "%";
        int textWidth = (int) mPaint.measureText(text);//拿到文本的宽度
        float radio = getProgress() * 1.0f / getMax();
        float progressX = radio * mRealWidth;
        if (progressX + textWidth > mRealWidth) {
            progressX = mRealWidth - textWidth;
            noNeedUnReach = true;
        }
        float endX = progressX - mTextOffset / 2;
        if (endX > 0) {
            mPaint.setColor(mReachColor);
            mPaint.setStrokeWidth(mReachHeight);
            canvas.drawLine(0, 0, endX, 0, mPaint);
        }

        mPaint.setColor(mTextColor);
        int y = (int) (-(mPaint.descent() + mPaint.ascent()) / 2);//获取文本的基线
        canvas.drawText(text, progressX, y, mPaint);

        if (!noNeedUnReach) {
            float start = progressX + mTextOffset / 2 + textWidth;
            mPaint.setColor(mUnReachColor);
            mPaint.setStrokeWidth(mUnReachHeight);
            canvas.drawLine(start, 0, mRealWidth, 0, mPaint);
        }
        canvas.restore();
    }
}

