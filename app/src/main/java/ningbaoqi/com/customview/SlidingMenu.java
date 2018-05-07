package ningbaoqi.com.customview;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.Scroller;

/**
 * Created by ningbaoqi on 18-4-17.
 *
 * 在ViewGroup中让自己内容移动有以下几个方法：
 *
 *     一、layout( l , t , r , b)
 *     二、offsetTopAndButtom( offset) 和 offsetLeftAndRight(offset)
 *     三、scrollTo()方法和scrollBy()方法：   注意：滚动的并不是 viewgroup 内容本身，而是它的矩形边框 ， 局限： 它是瞬间移动
 *
 * 在自定义 ViewGroup 中一般不需要实现 onMeasure 方法，我们会去实现系统已有的 ViewGroup 如 ：FrameLayout 它会帮我们实现 onMeasure 方法
 *
 * 让 view 在一段时间内移动到某个位置的两种办法：
 *
 *     一、使用自定义动画
 *     二、使用 Scroller ： 模拟一个执行的流程
 *
 * 触发事件传递有三种方法：
 *
 *     一、dispatchTouchEvent()方法  用来分发事件 ， 如果你拦截则交给 onTouchEvent()处理，否则，传给子 view
 *     二、onInterceptTouchEvent()方法  返回 true 表示拦截， 返回 false 表示不拦截
 *     三、onTouchEvent() 方法  处理触摸事件
 *   事件处理将不会向下传递，事件不处理将会向下传递 ，如果没有人处理这个事件，将会往回依次传递回去，最后丢失
 */

public class SlidingMenu extends FrameLayout {
    private View menuView , mainView;
    private int menuWidth;
    private Scroller scroller;


    public SlidingMenu(Context context) {
        super(context);
        init();
    }

    public SlidingMenu(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        scroller = new Scroller(getContext());
    }
    /**
     * 当直接的子view全部加载完毕时候调用，可以用来初始化子view的引用 ， 注意：这里无法获取子view的宽高
     * */
    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        menuView = getChildAt(0);
        mainView = getChildAt(1);
        menuWidth = menuView.getLayoutParams().width;
    }

//    @Override
//    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
//        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
//        int measureSpec = MeasureSpec.makeMeasureSpec(menuWidth , MeasureSpec.EXACTLY);
//        /**
//         * 测量所有子view的宽高
//         * */
//        menuView.measure(measureSpec , heightMeasureSpec);
//        mainView.measure(widthMeasureSpec , heightMeasureSpec);
//    }

    /**
     * menuView.layout()的参数介绍
     * 第一个参数：当前子view左边在父view的坐标系中的x坐标
     * 第二个参数：当前子view顶边在父view的坐标系中的y坐标
     * 第三个参数：当前子view右边在父view的坐标系中的x坐标
     * 第四个参数：当前子view底边在父view的坐标系中的y坐标
     * */
    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        menuView.layout(-menuWidth , 0 , 0 , b);
        mainView.layout(l , t , r , b);
    }


    private int downX;
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                downX = (int) event.getX();
                break;
            case MotionEvent.ACTION_MOVE:
                int moveX = (int) event.getX();
                int deltaX = (int) (moveX - downX);
                Log.d("nbq" , deltaX + "");
                /**
                 * getScrollX()是为了让再次移动的时候会继续移动，而不是从布局开始的位置移动
                 * */
                int newScrollX = getScrollX() - deltaX;
                if (newScrollX < -menuWidth) {
                    newScrollX = -menuWidth;
                }
                if (newScrollX > 0) {
                    newScrollX = 0;
                }
                Log.d("nbq" , "------------" + newScrollX + "-------getScrollX()--------" + getScrollX());
                scrollTo(newScrollX , 0);
                downX = moveX;
                break;
            case MotionEvent.ACTION_UP:
                //使用scroller方法
                if (getScrollX() > -menuWidth / 2) {
                    closeMenu();
                } else {
                    openMenu();
                }
                invalidate();

                /*//使用自定义动画
                ScrollAnimation animation ;
                if (getScrollX() > -menuWidth / 2) {
                    //关闭菜单
                    animation = new ScrollAnimation(this , 0);
                } else {
                    //打开菜单
                    animation = new ScrollAnimation(this , -menuWidth);
                }
                startAnimation(animation);*/
                break;
        }
        return true;
    }

    /**
     *  Scroller 不主动去调用这个方法 ，使用 invalidate 可以调用该方法
     * */
    @Override
    public void computeScroll() {
        super.computeScroll();
        if (scroller.computeScrollOffset()) {//返回true表示动画没有结束
            scrollTo(scroller.getCurrX() , 0);
            invalidate();
        }
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                downX = (int) ev.getX();
                break;
            case MotionEvent.ACTION_MOVE:
                int deltaX = (int) (ev.getX() - downX);
                if (Math.abs(deltaX) > 8) {
                    return true;
                }
                break;
        }
        return super.onInterceptTouchEvent(ev);
    }

    private void closeMenu() {
        scroller.startScroll(getScrollX(), 0, 0 - getScrollX() , 0 , 250);
    }
    private void openMenu() {
        scroller.startScroll(getScrollX() , 0 , -menuWidth - getScrollX() , 250);
    }

    /**
     * 切换菜单的开关
     * */
    public void switchMenu() {
        if (getScrollX() == 0) {
            //需要打开
            openMenu();
            invalidate();
        } else {
            closeMenu();
            invalidate();
        }
    }
}
