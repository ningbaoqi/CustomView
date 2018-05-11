package ningbaoqi.com.wuziqi;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

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
 * 创建 时间:18-5-11
 * <p/>
 * 描    述:
 * <p/>
 * <p/>
 * 修订 历史:
 * <p/>
 * =========================================
 */

public class WuziqiPanel extends View {
    private int mPanelWidht;
    private float mLinHeight;
    private int MAX_LINE = 10;
    private Paint mPaint = new Paint();
    private Bitmap mWhite;
    private Bitmap mBlack;
    private float ratio = 0.75f;
    private ArrayList<Point> whiteList = new ArrayList<>();
    private ArrayList<Point> blackList = new ArrayList<>();
    private boolean isWhite = true;//白棋先手
    private boolean isGameOver;
    private boolean isWhiteWinner;
    private int MAX_COUNT_IN_LIEN = 5;

    public WuziqiPanel(Context context) {
        this(context, null);
    }

    public WuziqiPanel(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public WuziqiPanel(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        mPaint.setColor(0x88000000);
        mPaint.setAntiAlias(true);
        mPaint.setDither(true);
        mPaint.setStyle(Paint.Style.STROKE);


        mWhite = BitmapFactory.decodeResource(getResources(), R.mipmap.stone_w2);
        mBlack = BitmapFactory.decodeResource(getResources(), R.mipmap.stone_b1);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int width = Math.min(widthSize, heightSize);
        if (widthMode == MeasureSpec.UNSPECIFIED) {
            width = heightSize;
        } else if (heightMode == MeasureSpec.UNSPECIFIED) {
            width = widthSize;
        }
        setMeasuredDimension(width, width);
    }


    /**
     * 当宽高确定了发生改变了，回调该方法
     */
    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mPanelWidht = w;
        mLinHeight = mPanelWidht * 1.0f / MAX_LINE;

        int pieceWidth = (int) (mLinHeight * ratio);
        mWhite = Bitmap.createScaledBitmap(mWhite, pieceWidth, pieceWidth, false);
        mBlack = Bitmap.createScaledBitmap(mBlack, pieceWidth, pieceWidth, false);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        drawBoard(canvas);
        drawPices(canvas);
        chechGameOver();
    }

    private void chechGameOver() {
        boolean whiteWin = chechFiveInLine(whiteList);
        boolean blackWin = chechFiveInLine(blackList);
        if (blackWin || whiteWin) {
            isGameOver = true;
            isWhiteWinner = whiteWin;
            String text = isWhiteWinner ? "白棋胜利" : "黑棋胜利";
            Toast.makeText(getContext(), text, Toast.LENGTH_LONG).show();
        }
    }

    private boolean chechFiveInLine(List<Point> points) {
        for (Point p : points) {
            int x = p.x;
            int y = p.y;
            boolean isWin = checkHorizontal(x, y, points);
            if (isWin) {
                return true;
            }
            isWin = checkVertical(x, y, points);
            if (isWin) {
                return true;
            }
            isWin = checkLeftDiagonal(x, y, points);
            if (isWin) {
                return true;
            }
            isWin = checkRightDiagonal(x, y, points);
            if (isWin) {
                return true;
            }
        }
        return false;
    }

    private boolean checkHorizontal(int x, int y, List<Point> points) {
        int count = 1;
        /**
         * 左边
         * */
        for (int i = 1; i < MAX_COUNT_IN_LIEN; i++) {
            if (points.contains(new Point(x - i, y))) {
                count++;
            } else {
                break;
            }
        }
        if (count == MAX_COUNT_IN_LIEN) {
            return true;
        }
        /**
         * 右边
         * */
        for (int i = 0; i < MAX_COUNT_IN_LIEN; i++) {
            if (points.contains(new Point(x + i, y))) {
                count++;
            } else {
                break;
            }
        }

        if (count == MAX_COUNT_IN_LIEN) {
            return true;
        }
        return false;
    }

    private boolean checkVertical(int x, int y, List<Point> points) {
        int count = 1;
        /**
         * 上
         * */
        for (int i = 1; i < MAX_COUNT_IN_LIEN; i++) {
            if (points.contains(new Point(x, y - i))) {
                count++;
            } else {
                break;
            }
        }
        if (count == MAX_COUNT_IN_LIEN) {
            return true;
        }
        /**
         * 下
         * */
        for (int i = 0; i < MAX_COUNT_IN_LIEN; i++) {
            if (points.contains(new Point(x, y + i))) {
                count++;
            } else {
                break;
            }
        }

        if (count == MAX_COUNT_IN_LIEN) {
            return true;
        }
        return false;
    }

    private boolean checkLeftDiagonal(int x, int y, List<Point> points) {
        int count = 1;
        /**
         * 上
         * */
        for (int i = 1; i < MAX_COUNT_IN_LIEN; i++) {
            if (points.contains(new Point(x - i, y + i))) {
                count++;
            } else {
                break;
            }
        }
        if (count == MAX_COUNT_IN_LIEN) {
            return true;
        }
        /**
         * 下
         * */
        for (int i = 0; i < MAX_COUNT_IN_LIEN; i++) {
            if (points.contains(new Point(x + i, y - i))) {
                count++;
            } else {
                break;
            }
        }

        if (count == MAX_COUNT_IN_LIEN) {
            return true;
        }
        return false;
    }

    private boolean checkRightDiagonal(int x, int y, List<Point> points) {
        int count = 1;
        /**
         * 上
         * */
        for (int i = 1; i < MAX_COUNT_IN_LIEN; i++) {
            if (points.contains(new Point(x - i, y - i))) {
                count++;
            } else {
                break;
            }
        }
        if (count == MAX_COUNT_IN_LIEN) {
            return true;
        }
        /**
         * 下
         * */
        for (int i = 0; i < MAX_COUNT_IN_LIEN; i++) {
            if (points.contains(new Point(x + i, y + i))) {
                count++;
            } else {
                break;
            }
        }

        if (count == MAX_COUNT_IN_LIEN) {
            return true;
        }
        return false;
    }


    private void drawPices(Canvas canvas) {
        for (int i = 0, n = whiteList.size(); i < n; i++) {
            Point whitePoint = whiteList.get(i);
            canvas.drawBitmap(mWhite, (whitePoint.x + (1 - ratio) / 2) * mLinHeight, (whitePoint.y + (1 - ratio) / 2) * mLinHeight, null);
        }
        for (int i = 0, n = blackList.size(); i < n; i++) {
            Point blackPoint = blackList.get(i);
            canvas.drawBitmap(mBlack, (blackPoint.x + (1 - ratio) / 2) * mLinHeight, (blackPoint.y + (1 - ratio) / 2) * mLinHeight, null);
        }
    }

    private void drawBoard(Canvas canvas) {
        int w = mPanelWidht;
        float lineHeight = mLinHeight;
        for (int i = 0; i < MAX_LINE; i++) {
            int startx = (int) (lineHeight / 2);
            int endX = (int) (w - lineHeight / 2);
            int y = (int) ((0.5 + i) * lineHeight);
            canvas.drawLine(startx, y, endX, y, mPaint);//横向

            canvas.drawLine(y, startx, y, endX, mPaint);//纵向
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (isGameOver) {
            return false;
        }
        int action = event.getAction();
        if (action == MotionEvent.ACTION_UP) {//对MotionEvent.ACTION_UP感兴趣
            int x = (int) event.getX();
            int y = (int) event.getY();
            Point point = getValidPoint(x, y);
            if (whiteList.contains(point) || blackList.contains(point)) {
                return false;
            }
            if (isWhite) {
                whiteList.add(point);
            } else {
                blackList.add(point);
            }
            invalidate();
            isWhite = !isWhite;
            return true;
        }
        return true;
    }

    private Point getValidPoint(int x, int y) {
        return new Point((int) (x / mLinHeight), (int) (y / mLinHeight));
    }

    public void start() {
        whiteList.clear();
        blackList.clear();
        isGameOver = false;
        isWhiteWinner = false;
        invalidate();
    }

    /**
     * 存储试图状态
     */
    private static final String INSTANCE = "instance";
    private static final String INSTANCE_GAME_OVER = "instance_game_over";
    private static final String INSTANCE_WHITE_ARRAY = "instance_white_array";
    private static final String INSTANCE_BLACK_ARRAY = "instance_black_array";

    @Override
    protected Parcelable onSaveInstanceState() {
        Bundle bundle = new Bundle();
        bundle.putParcelable(INSTANCE, super.onSaveInstanceState());
        bundle.putBoolean(INSTANCE_GAME_OVER, isGameOver);
        bundle.putParcelableArrayList(INSTANCE_WHITE_ARRAY, whiteList);
        bundle.putParcelableArrayList(INSTANCE_BLACK_ARRAY, blackList);
        return bundle;
    }

    @Override
    protected void onRestoreInstanceState(Parcelable state) {//判断是不是我们保存的东西
        if (state instanceof Bundle) {
            Bundle bundle = (Bundle) state;
            isGameOver = bundle.getBoolean(INSTANCE_GAME_OVER);
            whiteList = bundle.getParcelableArrayList(INSTANCE_WHITE_ARRAY);
            blackList = bundle.getParcelableArrayList(INSTANCE_BLACK_ARRAY);
            super.onRestoreInstanceState(bundle.getParcelable(INSTANCE));
            return;
        }
        super.onRestoreInstanceState(state);
    }
}

