package com.smcl.test.movinglinetest;

import android.app.Activity;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.view.View;

class MyView extends View {
    private static final int COUNT = 5;
    private static final int LINE_WIDTH = 5;
    private static final int MIN_ALPHA = 48;
    private static final int SCAN_TIME = 1000;
    private static final int TEXT_OFFSET = 20;
    private static final int TEXT_SIZE = 200;

    private long mStartTime;
    private int mPos[] = new int[COUNT];
    private Paint mPaints[] = new Paint[COUNT];
    private Paint mTextPaint;
    private boolean mEnabled;
    private int mFrameCount;
    private int mLastCount;
    private long mLastTime;
    private String mText;

    MyView(Context context) {
        super(context);
        setBackgroundColor(Color.BLACK);
        setLayerType(View.LAYER_TYPE_HARDWARE, null);
        //setWillNotCacheDrawing(true);
        for (int i = 0; i < COUNT; i++) {
            Paint p = new Paint();
            int alpha = (255 - MIN_ALPHA) * i / (COUNT - 1) + MIN_ALPHA;
            if (i == COUNT - 1)
                p.setARGB(alpha, 255, 255, 0);
            else
                p.setARGB(alpha, 0, 255, 0);
            mPaints[i] = p;
            mPos[i] = -1;
        }
        mTextPaint = new Paint();
        mTextPaint.setColor(Color.WHITE);
        mTextPaint.setTextSize(TEXT_SIZE);

        enable(true);
    }

    void reDraw() {
        if (mEnabled)
            invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        long time = System.currentTimeMillis();
        int w = getWidth();
        int h = getHeight();

        System.arraycopy(mPos, 1, mPos, 0, COUNT - 1);
        int p = (int)((time - mStartTime) * w / SCAN_TIME) % w;
        mPos[COUNT - 1] = p;

        long diff = time - mLastTime;
        if (diff >= 1000) {
            int fps = Math.round(1000 * (float)(mFrameCount - mLastCount) / diff);
            mLastTime = time;
            mLastCount = mFrameCount;
            mText = "FPS:" + fps;
        }

        for (int i = 0; i < COUNT; i++) {
            int x = mPos[i];
            canvas.drawRect(x, 0, x + LINE_WIDTH, h, mPaints[i]);
        }

        if (mText != null)
            canvas.drawText(mText, TEXT_OFFSET, TEXT_SIZE, mTextPaint);

        mFrameCount++;
        reDraw();

        FPSCounter.tick();
    }

    void enable(boolean enable) {
        if (mEnabled ^ enable) {
            mEnabled = enable;
            if (enable) {
                mLastTime = mStartTime = System.currentTimeMillis();
                mLastCount = 0;
                mFrameCount = 0;
                mText = null;
                reDraw();
            }
        }
    }
    void toggle() {
        enable(!mEnabled);
    }
}

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final View view = new MyView(this);
        setContentView(view);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((MyView)view).toggle();
            }
        });
    }
}
