package ningbaoqi.com.customview;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

/**
 * =========================================
 * <p/>
 * 版    权: ningxiansheng
 * <p/>
 * 作    者: 宁宝琪
 * <p/>
 * 版    本:1.0
 * <p/>
 * 创建 时间:18-5-7
 * <p/>
 * 描    述:
 * <p/>
 * <p/>
 * 修订 历史:
 * <p/>
 * =========================================
 */

public class Switcher extends View {
    private Bitmap switchBg;
    private Bitmap slideBg;
    private ToggleState state = ToggleState.OPEN;
    private boolean isSliding = false;
    private int currentX;

    public enum ToggleState {
        OPEN, CLOSE,
    }

    public Switcher(Context context) {//在java代码中动态new出来使用这个方法
        super(context);
    }

    public Switcher(Context context, @Nullable AttributeSet attrs) {//在布局文件中使用，只需要重写这个方法
        super(context, attrs);
    }

    public void setSlideBackgroundResource(int slideBackgroundResource) {//设置滑动块背景图片
        slideBg = BitmapFactory.decodeResource(getResources(), slideBackgroundResource);
    }

    public void setSwitchBackgroundResource(int switchBackgroundResource) {//设置滑动开关的背景图片
        switchBg = BitmapFactory.decodeResource(getResources(), switchBackgroundResource);
    }

    public void setToggleState(ToggleState toggleState) {//设置开关状态
        state = toggleState;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {//设置当前控件显示在屏幕上的宽高
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        setMeasuredDimension(switchBg.getWidth(), slideBg.getHeight());
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawBitmap(switchBg, 0, slideBg.getWidth() / 2, null);
        if (isSliding) {
            int left = currentX = slideBg.getWidth() / 2;
            if (left < 0) {
                left = 0;
            }
            if (left > switchBg.getWidth() - slideBg.getWidth()) {
                left = switchBg.getWidth() - slideBg.getWidth();
            }
            canvas.drawBitmap(slideBg, left - 7, 7, null);
        } else {
            if (state == ToggleState.OPEN) {
                canvas.drawBitmap(slideBg, switchBg.getWidth() - slideBg.getWidth() + 7, 7, null);
            } else {
                canvas.drawBitmap(slideBg, -7, 7, null);
            }
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        currentX = (int) event.getX();
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                isSliding = true;
                break;
            case MotionEvent.ACTION_MOVE:
                break;
            case MotionEvent.ACTION_UP:
                isSliding = false;
                int centerX = switchBg.getWidth() / 2;
                if (currentX > centerX) {
                    if (state != ToggleState.OPEN) {
                        state = ToggleState.OPEN;
                        switchBg = BitmapFactory.decodeResource(getResources(), R.mipmap.button_onoff_indicator_on);
                        if (listener != null) {
                            listener.OnToggleStateChange(state);
                        }
                    }
                } else {
                    if (state != ToggleState.CLOSE) {
                        state = ToggleState.CLOSE;
                        switchBg = BitmapFactory.decodeResource(getResources(), R.mipmap.button_onoff_indicator_off);
                        if (listener != null) {
                            listener.OnToggleStateChange(state);
                        }
                    }
                }
                break;
        }
        invalidate();
        return true;
    }

    private OnToggleStateChangeListener listener;

    public interface OnToggleStateChangeListener {
        void OnToggleStateChange(ToggleState state);
    }
    public void setOnToggleStateChangeLisner(OnToggleStateChangeListener listener) {
        this.listener = listener;
    }
}
