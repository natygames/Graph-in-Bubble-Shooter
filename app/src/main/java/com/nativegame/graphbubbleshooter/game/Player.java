package com.nativegame.graphbubbleshooter.game;

import android.graphics.Canvas;
import android.graphics.Paint;

import com.nativegame.graphbubbleshooter.engine.GameEngine;
import com.nativegame.graphbubbleshooter.engine.GameEvent;
import com.nativegame.graphbubbleshooter.engine.GameObject;
import com.nativegame.graphbubbleshooter.engine.Sprite;

import java.util.Random;

/**
 * Created by Oscar Liang on 2022/07/08
 */

public class Player extends Sprite {

    public BubbleColor mBubbleColor;
    private final BubbleManager mBubbleManager;

    private final float mStartX, mStartY;
    private final float mMaxX;
    private final float mSpeed;
    private float mSpeedX, mSpeedY;
    private boolean mShoot = false;

    private final Random mRandom = new Random();

    public Player(GameEngine gameEngine, BubbleManager bubbleManager) {
        super(gameEngine, BubbleColor.BLUE.getImageResId());

        mBubbleColor = BubbleColor.BLUE;
        mBubbleManager = bubbleManager;

        mStartX = mScreenWidth / 2f;
        mStartY = mScreenHeight * 3 / 4f;

        mMaxX = gameEngine.mScreenWidth - mWidth;

        mSpeed = gameEngine.mPixelFactor * 3000 / 1000;   // We want to move at 3000px per second

        gameEngine.addGameObject(new BubblePath(), 2);
    }

    @Override
    public void startGame() {
        mX = mStartX - mWidth / 2f;
        mY = mStartY - mHeight / 2f;
    }

    @Override
    public void onUpdate(long elapsedMillis, GameEngine gameEngine) {
        // We shoot the bubble one time
        if (mShoot) {
            // We convert angle to x speed and y speed
            float sideX = gameEngine.mInputController.mXUp - mStartX;
            float sideY = gameEngine.mInputController.mYUp - mStartY;
            float angle = (float) Math.atan2(sideY, sideX);

            mSpeedX = (float) (mSpeed * Math.cos(angle));
            mSpeedY = (float) (mSpeed * Math.sin(angle));
            // Log.d("output", "(" + angle + ", " + sideX + ", " + sideY + ")");

            mShoot = false;
        }

        // Update position
        mX += mSpeedX * elapsedMillis;
        if (mX <= 0) {
            mX = 0;
            mSpeedX = -mSpeedX;
        }
        if (mX >= mMaxX) {
            mX = mMaxX;
            mSpeedX = -mSpeedX;
        }

        mY += mSpeedY * elapsedMillis;
        if (mY <= -mHeight || mY >= mScreenHeight) {   // Player out of screen
            setNextBubble();
        }
    }

    @Override
    public void onGameEvent(GameEvent gameEvent) {
        if (gameEvent == GameEvent.SHOOT) {
            mShoot = true;
        }
    }

    @Override
    public void onCollision(GameEngine gameEngine, Sprite otherObject) {
        if (otherObject instanceof Bubble) {
            Bubble bubble = (Bubble) otherObject;
            if (bubble.mBubbleColor != BubbleColor.BLANK && mY >= bubble.mY) {
                mBubbleManager.addBubble(this, bubble);
                setNextBubble();
            }
        }
    }

    private void setNextBubble() {
        // Set random color to next bubble
        BubbleColor color = BubbleColor.values()[mRandom.nextInt(BubbleColor.values().length - 1)];
        mBubbleColor = color;
        mBitmap = getDefaultBitmap(mResources.getDrawable(color.getImageResId()));

        // Reset Position
        mSpeedX = 0;
        mSpeedY = 0;
        mX = mStartX - mWidth / 2f;
        mY = mStartY - mHeight / 2f;
    }

    class BubblePath extends GameObject {

        private final float mMaxX, mMinX;   // Border of the path
        private final float mRadius;   // Radius of the path

        private float mReflectX, mReflectY;   // Path reflection point
        private float mEndX, mEndY;   // Path end point

        private boolean mDraw = false;
        private final Paint mPaint = new Paint();

        public BubblePath() {
            mMaxX = mScreenWidth - mWidth / 2f;
            mMinX = mWidth / 2f;
            mRadius = mScreenWidth / 2f - mWidth / 2f;
        }

        @Override
        public void startGame() {
        }

        @Override
        public void onUpdate(long elapsedMillis, GameEngine gameEngine) {
            if (gameEngine.mInputController.mAiming) {
                // We calculate ratio of two side
                float sideX = gameEngine.mInputController.mXDown - mStartX;
                float sideY = gameEngine.mInputController.mYDown - mStartY;
                float ratio = Math.abs(sideY / sideX);
                if (sideY >= 0) {
                    ratio = -ratio;
                }

                // Update reflection point and end point position
                mReflectX = sideX > 0 ? mMaxX : mMinX;
                mReflectY = mStartY - mRadius * ratio;

                mEndX = sideX > 0 ? mMinX : mMaxX;
                mEndY = mReflectY - mRadius * ratio * 2;

                mDraw = true;
            } else {
                mDraw = false;
            }
        }

        @Override
        public void onDraw(Canvas canvas) {
            if (!mDraw) {
                return;
            }
            canvas.drawLine(mStartX, mStartY, mReflectX, mReflectY, mPaint);
            canvas.drawLine(mReflectX, mReflectY, mEndX, mEndY, mPaint);
        }

    }

}
