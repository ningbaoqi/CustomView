package ningbaoqi.com.customview;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
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

public class BeautifulMenuActivity extends AppCompatActivity implements View.OnClickListener {
    private ImageView iv_home, iv_menu;
    private RelativeLayout level1, level2, level3;
    private boolean isShowLevel2 = true;
    private boolean isShowLevel3 = true;
    private boolean isShowMenu = true;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
        initListener();
    }

    private void initListener() {
        this.iv_menu.setOnClickListener(this);
        this.iv_home.setOnClickListener(this);
        level1 = (RelativeLayout) findViewById(R.id.level1);
        level2 = (RelativeLayout) findViewById(R.id.level2);
        level3 = (RelativeLayout) findViewById(R.id.level3);
    }

    private void initView() {
        this.setContentView(R.layout.beautifulmenu);
        this.iv_home = (ImageView) findViewById(R.id.iv_home);
        this.iv_menu = (ImageView) findViewById(R.id.iv_menu);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_home:
                if (AnimationUtil.animationCount != 0) {//有动画在执行
                    return;
                }
                if (isShowLevel2) {
                    int delayTime = 0;
                    if (isShowLevel3) {
                        AnimationUtil.closeMenu(level3, delayTime);
                        delayTime = 300;
                        isShowLevel3 = false;
                    }
                    AnimationUtil.closeMenu(level2, delayTime);
                } else {
                    AnimationUtil.showMenu(level2, 0);
                }
                isShowLevel2 = !isShowLevel2;
                break;
            case R.id.iv_menu:
                if (AnimationUtil.animationCount != 0) {
                    return;
                }
                if (isShowLevel3) {
                    AnimationUtil.closeMenu(level3, 0);
                } else {
                    AnimationUtil.showMenu(level3, 0);
                }
                isShowLevel3 = !isShowLevel3;
                break;
            default:
                break;
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (isShowMenu) {
                int delayTime = 0;
                if (isShowLevel3) {
                    AnimationUtil.closeMenu(level3, delayTime);
                    delayTime += 200;
                    isShowLevel3 = false;
                }
                if (isShowLevel2) {
                    AnimationUtil.closeMenu(level2, delayTime);
                    delayTime += 200;
                    isShowLevel2 = false;
                }
                AnimationUtil.closeMenu(level1, delayTime);
            } else {
                int delayTime = 0;
                AnimationUtil.showMenu(level1, delayTime);
                AnimationUtil.showMenu(level2, 200);
                isShowLevel2 = true;
                AnimationUtil.showMenu(level3, 400);
                isShowLevel3 = true;
            }
            isShowMenu = !isShowMenu;
        }
        return true;
    }
}
