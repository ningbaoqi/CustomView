package ningbaoqi.com.customview;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;
import android.widget.ToggleButton;

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

public class SwitcherActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.switcher);
        Switcher switcher = (Switcher) findViewById(R.id.switcher);
        switcher.setSlideBackgroundResource(R.mipmap.btn_switch_to_on_mtrl_00012);
        switcher.setSwitchBackgroundResource(R.mipmap.button_onoff_indicator_on);
        switcher.setToggleState(Switcher.ToggleState.OPEN);
        switcher.setOnToggleStateChangeLisner(new Switcher.OnToggleStateChangeListener() {
            @Override
            public void OnToggleStateChange(Switcher.ToggleState state) {
                Toast.makeText(SwitcherActivity.this , state == Switcher.ToggleState.OPEN ? "开启" : "关闭" , Toast.LENGTH_LONG).show();
            }
        });
    }
}
