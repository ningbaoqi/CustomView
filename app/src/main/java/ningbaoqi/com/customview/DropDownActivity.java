package ningbaoqi.com.customview;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupWindow;
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

public class DropDownActivity extends AppCompatActivity implements View.OnClickListener {
    private int popupwindowHeight = 700;
    private EditText editText;
    private ImageView imageView;
    private ListView listView;
    private ArrayList<String> list = new ArrayList<>();
    private PopupWindow popupWindow;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
        initListener();
        initData();
    }

    private void initData() {
        for (int i = 0; i < 15; i++) {
            list.add(90000 + +i + "");
        }
        initLisView();
    }

    private void initLisView() {
        listView = new ListView(this);
        listView.setBackgroundColor(Color.WHITE);
        listView.setVerticalScrollBarEnabled(false);//设置listview的滚动条不可见
        listView.setAdapter(new MyAdapter());
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                editText.setText(list.get(position));
                popupWindow.dismiss();
            }
        });
    }

    private void initListener() {
        imageView.setOnClickListener(this);
    }

    private void initView() {
        setContentView(R.layout.dropdown);
        editText = (EditText) findViewById(R.id.edittext);
        imageView = (ImageView) findViewById(R.id.iv_select);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_select:
                showNumberList();
                break;
            default:
                break;
        }
    }

    private void showNumberList() {
        if (popupWindow == null) {
            popupWindow = new PopupWindow(listView, editText.getWidth(), popupwindowHeight);
        }
        popupWindow.showAsDropDown(editText, 0, 10);//设置popupwindow相对的组件的偏移量
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
            return position;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            final View view = View.inflate(DropDownActivity.this, R.layout.list_item, null);
            TextView tv_number = view.findViewById(R.id.tv_number);
            ImageView iv_delete = view.findViewById(R.id.iv_delete);
            tv_number.setText(list.get(position));
            iv_delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    list.remove(position);
                    notifyDataSetChanged();//通知数据变化
                    int listview_height = view.getHeight() * list.size();
                    popupWindow.update(editText.getWidth() , listview_height > popupwindowHeight ? popupwindowHeight : listview_height);
                    if (list.size() == 0) {
                        popupWindow.dismiss();
                        imageView.setVisibility(View.GONE);
                    }
                }
            });
            return view;
        }
    }
}
