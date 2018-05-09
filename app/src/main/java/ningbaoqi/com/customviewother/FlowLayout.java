package ningbaoqi.com.customviewother;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

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

public class FlowLayout extends ViewGroup {

    public FlowLayout(Context context) {
        this(context, null);
    }

    public FlowLayout(Context context, AttributeSet attrs) {
        super(context, attrs, 0);
    }

    public FlowLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    /**
     * 测量的是本容器的宽高
     */
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int sizeWidth = MeasureSpec.getSize(widthMeasureSpec);//获取容器的宽度
        int modeWidth = MeasureSpec.getMode(widthMeasureSpec);
        int sizeHeight = MeasureSpec.getSize(heightMeasureSpec);
        int modeHeight = MeasureSpec.getMode(heightMeasureSpec);
        //wrap_content
        int width = 0;
        int height = 0;

        //记录每一行的宽度与高度
        int lineWidth = 0;
        int lineHeight = 0;

        //获取内部元素的个数
        int cCount = getChildCount();
        for (int i = 0; i < cCount; i++) {
            View child = getChildAt(i);
            measureChild(child, widthMeasureSpec, heightMeasureSpec);//测量子view的宽和高
            MarginLayoutParams layoutParams = (MarginLayoutParams) child.getLayoutParams();
            int childWidth = child.getMeasuredWidth() + layoutParams.leftMargin + layoutParams.rightMargin;//子view占据的宽度
            int childHeight = child.getMeasuredHeight() + layoutParams.topMargin + layoutParams.bottomMargin;//子view占据的高度
            if (lineWidth + childWidth + getPaddingLeft() + getPaddingRight() > sizeWidth) {
                width = Math.max(width, lineWidth);//对比得到最大的宽度
                lineWidth = childWidth;
                height += lineHeight;//记录行高
                lineHeight = childHeight;
            } else {//未换行情况
                lineWidth += childWidth;
                lineHeight = Math.max(lineHeight, childHeight);
            }
            if (i == cCount - 1) {//到达最后一个控件
                width = Math.max(lineWidth, width);
                height += lineHeight;
            }
        }
        setMeasuredDimension(modeWidth == MeasureSpec.EXACTLY ? sizeWidth : width + getPaddingRight() + getPaddingLeft(), modeHeight == MeasureSpec.EXACTLY ? sizeHeight : height + getPaddingTop() + getPaddingBottom());
    }

    private List<List<View>> mAllViews = new ArrayList<>();//存储所有的View
    private List<Integer> mLineHeight = new ArrayList<>();//每一行的高度

    /**
     * 设置子View的位置
     */
    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        mAllViews.clear();
        mLineHeight.clear();
        int width = getWidth();//因为已经执行的onMeasure方法，所以可以拿到当前ViewGroup的宽度
        int lineWidth = 0;
        int lineHeight = 0;
        List<View> lineViews = new ArrayList<>();
        int cCount = getChildCount();
        for (int i = 0; i < cCount; i++) {
            View child = getChildAt(i);
            MarginLayoutParams params = (MarginLayoutParams) child.getLayoutParams();
            int childWidth = child.getMeasuredWidth();
            int childHeight = child.getMeasuredHeight();
            if (childWidth + lineWidth + params.leftMargin + params.rightMargin + getPaddingLeft() + getPaddingRight() > width) {//如果需要换行
                mLineHeight.add(lineHeight);
                mAllViews.add(lineViews);

                lineWidth = 0;//重置行宽和行高
                lineHeight = childHeight + params.topMargin + params.bottomMargin;

                lineViews = new ArrayList<>();//重置View集合
            }
            lineWidth += childWidth + params.leftMargin + params.rightMargin;
            lineHeight = Math.max(lineHeight, childHeight + params.topMargin + params.bottomMargin);
            lineViews.add(child);
        }//for end
        //处理最后一行
        mLineHeight.add(lineHeight);
        mAllViews.add(lineViews);
        /**
         * 设置子View的位置
         * */
        int left = getPaddingLeft();
        int top = getPaddingTop();
        int lineNum = mAllViews.size();//行数
        for (int i = 0; i < lineNum; i++) {
            lineViews = mAllViews.get(i);//当前行的所有的View
            lineHeight = mLineHeight.get(i);//获取当前行的高度
            for (int j = 0; j < lineViews.size(); j++) {
                View child = lineViews.get(j);
                if (child.getVisibility() == View.GONE) {
                    continue;
                }
                MarginLayoutParams params = (MarginLayoutParams) child.getLayoutParams();
                int lc = left + params.leftMargin;//该view左边开始的位置
                int tc = top + params.topMargin;//该View上边开始的位置
                int rc = lc + child.getMeasuredWidth();//该View右边开始的位置
                int bc = tc + child.getMeasuredHeight();//该View下边开始的位置
                child.layout(lc, tc, rc, bc);//设置子View的位置
                left += child.getMeasuredWidth() + params.leftMargin + params.rightMargin;
            }
            left = getPaddingLeft();
            top += lineHeight;
        }
    }


    /**
     * 与当前ViewGroup对应的LayoutParams
     */
    @Override
    public LayoutParams generateLayoutParams(AttributeSet attrs) {
        return new MarginLayoutParams(getContext(), attrs);
    }
}
