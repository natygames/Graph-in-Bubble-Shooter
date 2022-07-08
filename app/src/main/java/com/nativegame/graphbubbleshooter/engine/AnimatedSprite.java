package com.nativegame.graphbubbleshooter.engine;

import android.graphics.Bitmap;

public abstract class AnimatedSprite extends Sprite {

    private long mTimePreFrame;
    private long mCurrentTime;
    private int mIndex;

    protected Bitmap[] mSpriteBitmaps;

    protected AnimatedSprite(GameEngine gameEngine, int drawableRes) {
        super(gameEngine, drawableRes);
    }

    @Override
    public void startGame() {
        mCurrentTime = 0;
        mIndex = 0;
    }

    @Override
    public void onUpdate(long elapsedMillis, GameEngine gameEngine) {
        mCurrentTime += elapsedMillis;
        if (mCurrentTime >= mTimePreFrame) {
            mIndex++;
            if (mIndex > mSpriteBitmaps.length - 1) {
                mIndex = 0;
            }

            mBitmap = mSpriteBitmaps[mIndex];
            mCurrentTime = 0;
        }
    }

    public void setAnimatedSpriteBitmaps(Bitmap[] spriteBitmaps) {
        mSpriteBitmaps = spriteBitmaps;
    }

    public void setTimePreFrame(long timePreFrame) {
        mTimePreFrame = timePreFrame;
    }

}
