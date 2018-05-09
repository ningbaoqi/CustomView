package ningbaoqi.com.customviewother;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.widget.TextView;

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

public class FlowLayoutActivity extends AppCompatActivity {
    private String[] mVals = new String[]{"Hellogvfsdfasdf", "WelcomefGJHJGSDF", "AndroidAFA", "ButtonASDBH", "TextViewAD", "HelloASDASFGFDG", "WelcomeADSDWDASDCSA", "AndroidSAD", "ButtonQ", "TextViewFAHSGAGFS", "HelloDFAHGDH", "WelcomeADA", "AndroidFA", "ButtonQ", "TextViewDASSFGHDJ", "HelloaSFsd", "WelcomSADADASDe", "AndrADAGSGoid", "ButtDASADFon", "TeADASDxtView"};
    private FlowLayout flowLayout;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.flowlayout);
        flowLayout = (FlowLayout) findViewById(R.id.flowlayout);
        initData();
    }

    LayoutInflater inflater;

    public void initData() {
        inflater = LayoutInflater.from(this);
        for (int i = 0; i < mVals.length; i++) {
            TextView textview = (TextView) inflater.inflate(R.layout.tv, flowLayout, false);
            textview.setText(mVals[i]);
            flowLayout.addView(textview);
        }
    }
}
