package ningbaoqi.com.customview;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.RotateAnimation;
import android.widget.AbsListView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Date;

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

public class RefreshListView extends ListView implements AbsListView.OnScrollListener {
    private View headerView;
    private ImageView iv_arrow;
    private ProgressBar pb_rotate;
    private TextView tv_state, tv_time;
    private int headViewHeight;
    private int downY;
    private final int PULL_REFRESH = 0;
    private final int RELEASE_PREFERSH = 1;
    private final int REFRESHING = 2;
    private int currentState = PULL_REFRESH;
    private RotateAnimation upAnimation, downAnimation;
    private View footerView;
    private int footerViewHeight;
    private boolean isLoadingMode = false;//当前是是否处理加载更多

    public RefreshListView(Context context) {
        super(context);
        init();
    }

    public RefreshListView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        setOnScrollListener(this);
        initHeadView();
        initRotateAnimation();
        initFooterView();
    }

    private void initHeadView() {
        headerView = View.inflate(getContext(), R.layout.header, null);
        iv_arrow = headerView.findViewById(R.id.iv_arrow);
        pb_rotate = headerView.findViewById(R.id.pb_rotate);
        tv_state = headerView.findViewById(R.id.tv_state);
        tv_time = headerView.findViewById(R.id.tv_time);
        headerView.measure(0, 0);
        headViewHeight = headerView.getMeasuredHeight();
        headerView.setPadding(0, -headViewHeight, 0, 0);
        addHeaderView(headerView);
    }

    /**
     * 根据currentState来更新headView
     */
    private void regreshHeaderView() {
        switch (currentState) {
            case PULL_REFRESH:
                tv_state.setText("下拉刷新");
                iv_arrow.startAnimation(downAnimation);
                break;
            case RELEASE_PREFERSH:
                tv_state.setText("松开刷新");
                iv_arrow.startAnimation(upAnimation);
                break;
            case REFRESHING:
                iv_arrow.clearAnimation();
                iv_arrow.setVisibility(View.INVISIBLE);
                pb_rotate.setVisibility(View.VISIBLE);
                tv_state.setText("正在刷新...");
                break;
        }
    }

    private void initRotateAnimation() {
        upAnimation = new RotateAnimation(0, -180, RotateAnimation.RELATIVE_TO_SELF, 0.5F,
                RotateAnimation.RELATIVE_TO_SELF, 0.5F);
        upAnimation.setDuration(300);
        upAnimation.setFillAfter(true);

        downAnimation = new RotateAnimation(-180, -360, RotateAnimation.RELATIVE_TO_SELF, 0.5F,
                RotateAnimation.RELATIVE_TO_SELF, 0.5F);
        downAnimation.setDuration(300);
        downAnimation.setFillAfter(true);
    }

    private void initFooterView() {
        footerView = View.inflate(getContext(), R.layout.footer, null);
        footerView.measure(0, 0);//主动通知系统去测量
        footerViewHeight = footerView.getMeasuredHeight();//获得测量完的高度，只要在onMeasure()方法执行完，就可以用该方法获取到宽高，在自定义控件中多使用这个，使用view.measure(0 , 0)可以主动通知系统去测量，然后就可以直接使用它获取宽高;getHeight():必须在onLayout()方法执行完后，才能获取宽高,但是没有生效，使用第一个方法
        footerView.setPadding(0, -footerViewHeight, 0, 0);
        addFooterView(footerView);
    }

    private OnRefreshListener listener;

    /**
     * SCROLL_STATE_IDLE = 0 ：闲置状态，就是手指松开
     * SCROLL_STATE_TOUCH_SCROLL = 1 ：手指触摸滑动就是按着滑动
     * SCROLL_STATE_FLING = 2 ： 快速滑动后松开，惯性
     */
    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {
        if (scrollState == OnScrollListener.SCROLL_STATE_IDLE && getLastVisiblePosition() == getCount() - 1 && !isLoadingMode) {
            isLoadingMode = true;
            footerView.setPadding(0, 0, 0, 0);
            setSelection(getCount());
            if (listener != null) {
                listener.onLoadingMore();
            }
        }
    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                downY = (int) ev.getY();
                break;
            case MotionEvent.ACTION_MOVE:
                if (currentState == REFRESHING) {
                    break;
                }
                int deltaY = (int) (ev.getY() - downY);//下拉距离
                int paddingTop = -headViewHeight + deltaY;
                if (paddingTop > -headViewHeight && this.getFirstVisiblePosition() == 0) {
                    headerView.setPadding(0, paddingTop, 0, 0);
                    if (paddingTop >= 0 && currentState == PULL_REFRESH) {
                        currentState = RELEASE_PREFERSH;
                        regreshHeaderView();
                    } else if (paddingTop < 0 && currentState == RELEASE_PREFERSH) {
                        currentState = PULL_REFRESH;
                        regreshHeaderView();
                    }
                    return true;//为了拦截TouchMove不让listview处理该次move事件,会造成listview无法滑动
                }

            case MotionEvent.ACTION_UP:
                if (currentState == PULL_REFRESH) {
                    headerView.setPadding(0, -headViewHeight, 0, 0);
                } else if (currentState == RELEASE_PREFERSH) {
                    headerView.setPadding(0, 0, 0, 0);
                    currentState = REFRESHING;
                    regreshHeaderView();
                    if (listener != null) {
                        listener.onPullPrefresh();
                    }
                    /*new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            completeRefresh();
                        }
                    } , 3000);*/
                }
                break;
        }
        return super.onTouchEvent(ev);
    }

    public void completeRefresh() {
        if (isLoadingMode) {
            footerView.setPadding(0, -footerViewHeight, 0, 0);
            isLoadingMode = false;
        } else {
            headerView.setPadding(0, -headViewHeight, 0, 0);
            currentState = PULL_REFRESH;
            pb_rotate.setVisibility(View.INVISIBLE);
            iv_arrow.setVisibility(VISIBLE);
            tv_state.setText("下拉刷新");
            tv_time.setText("最后刷新:" + currentTime());
        }
    }

    private String currentTime() {
        SimpleDateFormat format = new SimpleDateFormat("yy-MM-dd HH:mm:ss");
        return format.format(new Date());
    }

    public interface OnRefreshListener {
        void onPullPrefresh();

        void onLoadingMore();
    }

    public void setOnRefreshListener(OnRefreshListener listener) {
        this.listener = listener;
    }
}
