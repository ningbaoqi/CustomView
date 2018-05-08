package ningbaoqi.com.customview;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 * =========================================
 * <p/>
 * 版    权: ningxiansheng
 * <p/>
 * 作    者: 宁宝琪
 * <p/>
 * 版    本:1.0
 * <p/>
 * 创建 时间:18-5-8
 * <p/>
 * 描    述:
 * <p/>
 * <p/>
 * 修订 历史:
 * <p/>
 * =========================================
 */

public class TopBar extends RelativeLayout {
    private Button leftBtn, rightBtn;
    private TextView tvTitle;

    private int leftTextColor;
    private Drawable leftBackground;
    private String leftText;

    private int rightTextColor;
    private Drawable rightBackground;
    private String rightText;

    private float titleTextSize;
    private int titleTextColor;
    private String title;

    private RelativeLayout.LayoutParams leftParames, rightParams, titlePamas;

    public TopBar(Context context) {
        super(context);
    }

    /**
     * 获取自定义属性
     *
     * @param context
     * @param attrs
     */
    public TopBar(final Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.TopBar);
        leftTextColor = typedArray.getColor(R.styleable.TopBar_lefttextcolor, 0);
        leftBackground = typedArray.getDrawable(R.styleable.TopBar_leftbackground);
        leftText = typedArray.getString(R.styleable.TopBar_lefttext);
        rightTextColor = typedArray.getColor(R.styleable.TopBar_righttextcolor, 0);
        rightText = typedArray.getString(R.styleable.TopBar_righttext);
        rightBackground = typedArray.getDrawable(R.styleable.TopBar_rightbackground);
        titleTextSize = typedArray.getDimension(R.styleable.TopBar_titletextsize, 0);
        titleTextColor = typedArray.getColor(R.styleable.TopBar_titlecolor, 0);
        title = typedArray.getString(R.styleable.TopBar_title);
        typedArray.recycle();

        leftBtn = new Button(context);
        rightBtn = new Button(context);
        tvTitle = new TextView(context);

        leftBtn.setTextColor(leftTextColor);
        leftBtn.setBackground(leftBackground);
        leftBtn.setText(leftText);

        rightBtn.setTextColor(rightTextColor);
        rightBtn.setText(rightText);
        rightBtn.setBackground(rightBackground);

        tvTitle.setTextColor(titleTextColor);
        tvTitle.setTextSize(titleTextSize);
        tvTitle.setText(title);
        tvTitle.setGravity(Gravity.CENTER);

        setBackgroundColor(Color.GREEN);

        leftParames = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        leftParames.addRule(RelativeLayout.ALIGN_PARENT_LEFT, TRUE);//相对布局特有的属性
        addView(leftBtn, leftParames);

        rightParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        rightParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, TRUE);
        addView(rightBtn, rightParams);

        titlePamas = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT);
        titlePamas.addRule(RelativeLayout.CENTER_IN_PARENT, TRUE);
        addView(tvTitle, titlePamas);

        leftBtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                lickListner.onLeftBtnClick();
            }
        });
        rightBtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                lickListner.onRightBtnClick();
            }
        });
    }

    public TopBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void setTopbarClickListener(onSetTopBarCLickListner lickListner) {
        this.lickListner = lickListner;
    }

    private onSetTopBarCLickListner lickListner;

    public interface onSetTopBarCLickListner {
        void onLeftBtnClick();

        void onRightBtnClick();
    }
}
