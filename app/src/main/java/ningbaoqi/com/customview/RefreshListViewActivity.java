package ningbaoqi.com.customview;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
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

public class RefreshListViewActivity extends AppCompatActivity {
    private static final String TAG = "ningbaoqi123";
    private RefreshListView refreshListView;
    private ArrayList<String> list = new ArrayList<>();
    private MyAdapter adapter;
    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            adapter.notifyDataSetChanged();
            refreshListView.completeRefresh();
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
        initData();
    }

    private void initView() {
        setContentView(R.layout.refresh);
        refreshListView = (RefreshListView) findViewById(R.id.refresh);
    }

    private void initData() {
        for (int i = 0 ; i < 30 ; i ++) {
            list.add("listView原来的数据--" + i);
        }
        adapter = new MyAdapter();
        refreshListView.setAdapter(adapter);
        refreshListView.setOnRefreshListener(new RefreshListView.OnRefreshListener() {
            @Override
            public void onPullPrefresh() {
                requestDataFromServer(false);
            }

            @Override
            public void onLoadingMore() {
                requestDataFromServer(true);
            }
        });
//        final View view = View.inflate(this , R.layout.header , null);
//        /**
//         *
//         * */
//        view.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
//            @Override
//            public void onGlobalLayout() {//设置监听了，等到onLayout方法调用完成之后会回调
//                //view.getViewTreeObserver().removeOnGlobalLayoutListener(this);//获取之后需要删除，不然每次调用完onLayout方法都获取一遍
//                int headerViewHeight = view.getHeight();
//                Log.d("caonima" , "headerViewHeight");
//                Log.d(TAG , "----------0 " + headerViewHeight);
//                view.setPadding(0, -headerViewHeight, 0, 0);
//                /**
//                 * 为ListView顶部添加组件，必须在setAdapter之前调用
//                 * */
//                refreshListView.addHeaderView(view);
//            }
//        });
//        view.measure(0 , 0);
//        int headerViewHeight = view.getMeasuredHeight();
//        Log.d(TAG , "HEAD:" + headerViewHeight);
//        view.setPadding(0 , - headerViewHeight , 0 , 0);
//        refreshListView.addHeaderView(view);
    }

    private void requestDataFromServer(final boolean isLoadingMore) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                SystemClock.sleep(3000);//睡眠
                if (isLoadingMore) {
                    list.add("加载更多的数据----1");
                    list.add("加载更多的数据----2");
                    list.add("加载更多的数据----3");
                } else {
                    list.add(0 , "刷新出来的数据");
                }
                handler.sendEmptyMessage(0);
            }
        }).start();
    }

    class MyAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return list.size();
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            TextView textView = new TextView(RefreshListViewActivity.this);
            textView.setPadding(20 , 20 , 20 , 20);
            textView.setTextSize(18);
            textView.setText(list.get(position));
            return textView;
        }
    }
}
