package com.nativegame.graphbubbleshooter.game;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

import com.nativegame.graphbubbleshooter.engine.GameEngine;
import com.nativegame.graphbubbleshooter.engine.GameObject;

public class FPSCounter extends GameObject {

    private final float mTextWidth;
    private final float mTextHeight;
    private final Paint mPaint = new Paint();

    private long mCurrentTime;
    private int mDraws;

    private String mFpsText = "";

    public FPSCounter(GameEngine gameEngine) {
        mPaint.setTextAlign(Paint.Align.CENTER);
        mTextHeight = gameEngine.mPixelFactor * 100f;
        mTextWidth = gameEngine.mPixelFactor * 200f;
        mPaint.setTextSize(mTextHeight / 2);
    }

    @Override
    public void startGame() {
        mCurrentTime = 0;
    }

    @Override
    public void onUpdate(long elapsedMillis, GameEngine gameEngine) {
        mCurrentTime += elapsedMillis;
        if (mCurrentTime > 1000) {
            int mFps = (int) (mDraws * 1000 / mCurrentTime);
            mFpsText = mFps + "fps";
            mCurrentTime = 0;
            mDraws = 0;
        }
    }

    @Override
    public void onDraw(Canvas canvas) {
        mPaint.setColor(Color.BLACK);
        canvas.drawRect(0, (int) (canvas.getHeight() - mTextHeight), mTextWidth, canvas.getHeight(), mPaint);
        mPaint.setColor(Color.WHITE);
        canvas.drawText(mFpsText, mTextWidth / 2, (int) (canvas.getHeight() - mTextHeight / 2), mPaint);
        mDraws++;
    }
}


