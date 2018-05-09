package ningbaoqi.com.customviewother;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

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

public class HorizontalProgressBarActivity extends AppCompatActivity {

    private HorizontalProgressBarWithProgress progressBarWithProgress;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            int progresss = progressBarWithProgress.getProgress();
            progressBarWithProgress.setProgress(++progresss);
            if (progresss >= 100) {
                handler.removeMessages(1);
            } else {
                handler.sendEmptyMessageDelayed(1, 100);
            }
        }
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.progressbar);
        progressBarWithProgress = (HorizontalProgressBarWithProgress) findViewById(R.id.progress);
        handler.sendEmptyMessage(1);
    }
}
