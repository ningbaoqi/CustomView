package ningbaoqi.com.customview;

import android.view.View;
import android.view.animation.Animation;
import android.view.animation.Transformation;

/**
 * Created by ningbaoqi on 18-4-18.
 */

public class ScrollAnimation extends Animation{
    private View view;
    private int targetScrollX;
    private int startScrollX;
    private int totleValue;
    public ScrollAnimation(View view , int targetScrollX ) {
        this.view = view;
        this.targetScrollX = targetScrollX;
        this.startScrollX = view.getScrollX();
        totleValue = this.targetScrollX - this.startScrollX;
        int time = Math.abs(totleValue);
        setDuration(time - 100);
    }

    /**
     * 在指定的时间内一直执行该方法，直到动画结束
     * interpolatedTime 0 ～ 1 标识动画执行的进度或者百分比
     * */
    @Override
    protected void applyTransformation(float interpolatedTime, Transformation t) {
        super.applyTransformation(interpolatedTime, t);
        int currentScrollX = (int) (startScrollX + totleValue * interpolatedTime);
        view.scrollTo(currentScrollX , 0);
    }
}
