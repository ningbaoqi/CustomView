package ningbaoqi.com.customview;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

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

public class SowingMapActivity extends AppCompatActivity {

    private ViewPager viewPager;
    private TextView intro;
    private LinearLayout dot_linearlayout;
    private ArrayList<Ad> ads = new ArrayList<>();
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            viewPager.setCurrentItem(viewPager.getCurrentItem() + 1);
            handler.sendEmptyMessageDelayed(0, 2000);
        }
    };
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
        initListener();
        initData();
    }

    private void initView() {
        setContentView(R.layout.sowingmap);
        viewPager = (ViewPager) findViewById(R.id.viewPager);
        intro = (TextView) findViewById(R.id.intro);
        dot_linearlayout = (LinearLayout) findViewById(R.id.dot_layout);
    }

    private void initListener() {
        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                updateInfoAndDot();
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    private void updateInfoAndDot() {
        int currentPage = viewPager.getCurrentItem() % ads.size();
        intro.setText(ads.get(currentPage).getIntro());
        for (int i = 0; i < dot_linearlayout.getChildCount(); i++) {
            dot_linearlayout.getChildAt(i).setEnabled(currentPage == i);
        }
    }

    private void initData() {
        ads.add(new Ad(R.mipmap.a, "巩俐不低俗，我就不能低俗"));
        ads.add(new Ad(R.mipmap.b, "朴树又回来了，在唱经典老歌引百万人合唱"));
        ads.add(new Ad(R.mipmap.c, "揭秘北京电影如何升级"));
        ads.add(new Ad(R.mipmap.d, "乐视网TV版大放送"));
        ads.add(new Ad(R.mipmap.e, "热血屌丝反杀"));
        initDots();
        viewPager.setAdapter(new MyPagerAdapter());
        viewPager.setCurrentItem(Integer.MAX_VALUE / 2 - (Integer.MAX_VALUE / 2 % ads.size()));
        updateInfoAndDot();
        handler.sendEmptyMessageDelayed(0, 2000);
    }

    /**
     * 初始化点布局
     */
    private void initDots() {
        for (int i = 0; i < ads.size(); i++) {
            View view = new View(this);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(12, 12);
            params.leftMargin = 8;
            view.setLayoutParams(params);
            view.setBackgroundResource(R.drawable.selector_dot);
            dot_linearlayout.addView(view);
        }
    }

    class MyPagerAdapter extends PagerAdapter {

        @Override
        public int getCount() {//返回多少的page
            return Integer.MAX_VALUE;//将这个值设置到最大实现无限轮播
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {//返回值为true：表示不去创建，使用缓存；false：表示去创建；
            return view == object;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {//销毁page
            container.removeView((View) object);
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {//类似 BaseAdapter 的 getView()方法
            View view = View.inflate(SowingMapActivity.this, R.layout.viewpager_item, null);
            ImageView imageView = view.findViewById(R.id.imageview);
            imageView.setImageResource(ads.get(position % ads.size()).getIconResID());
            container.addView(view);
            return view;
        }
    }
}
