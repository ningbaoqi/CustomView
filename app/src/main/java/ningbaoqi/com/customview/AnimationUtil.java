package ningbaoqi.com.customview;

import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.RelativeLayout;

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

class AnimationUtil {
    public static int animationCount = 0;//记录当前开启动画的数量

    public static void closeMenu(RelativeLayout layout, int delayTime) {
        for (int i = 0; i < layout.getChildCount(); i++) {//遍历布局中的子View并且将所有的子View全部设置为不可用
            layout.getChildAt(i).setEnabled(false);
        }
        RotateAnimation rotationAnimation = new RotateAnimation(0, -180, RotateAnimation.RELATIVE_TO_SELF, 0.5F, RotateAnimation.RELATIVE_TO_SELF, 1f);
        rotationAnimation.setStartOffset(delayTime);//设置动画延迟播放
        rotationAnimation.setDuration(500);
        rotationAnimation.setFillAfter(true);//动画结束后保持原来的状态
        rotationAnimation.setAnimationListener(new MyAnimationListener());
        layout.startAnimation(rotationAnimation);
    }

    public static void showMenu(RelativeLayout layout, int delayTime) {
        for (int i = 0; i < layout.getChildCount(); i++) {
            layout.getChildAt(i).setEnabled(true);
        }
        RotateAnimation rotationAnimation = new RotateAnimation(-180, 0, RotateAnimation.RELATIVE_TO_SELF, 0.5f, RotateAnimation.RELATIVE_TO_SELF, 1F);
        rotationAnimation.setStartOffset(delayTime);
        rotationAnimation.setDuration(500);
        rotationAnimation.setFillAfter(true);
        rotationAnimation.setAnimationListener(new MyAnimationListener());
        layout.startAnimation(rotationAnimation);
    }

    static class MyAnimationListener implements Animation.AnimationListener {

        @Override
        public void onAnimationStart(Animation animation) {
            animationCount++;
        }

        @Override
        public void onAnimationEnd(Animation animation) {
            animationCount--;
        }

        @Override
        public void onAnimationRepeat(Animation animation) {

        }
    }
}
