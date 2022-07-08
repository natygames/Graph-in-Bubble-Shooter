package com.nativegame.graphbubbleshooter.game;

import com.nativegame.graphbubbleshooter.engine.GameEngine;
import com.nativegame.graphbubbleshooter.engine.Sprite;

import java.util.ArrayList;

/**
 * Created by Oscar Liang on 2022/07/08
 */

public class Bubble extends Sprite {

    public final int mRow, mCol;
    public BubbleColor mBubbleColor;
    public int mDepth = -1;
    public boolean mDiscover = false;
    public final ArrayList<Bubble> mEdges = new ArrayList<>(6);

    protected Bubble(GameEngine gameEngine, int row, int col, BubbleColor bubbleColor) {
        super(gameEngine, bubbleColor.getImageResId());

        mRow = row;
        mCol = col;
        mBubbleColor = bubbleColor;
    }

    @Override
    public void startGame() {
    }

    @Override
    public void onUpdate(long elapsedMillis, GameEngine gameEngine) {
    }

    public void setBubbleColor(BubbleColor color) {
        mBubbleColor = color;
        mBitmap = getDefaultBitmap(mResources.getDrawable(color.getImageResId()));
    }

    public void setBlankBubble() {
        setBubbleColor(BubbleColor.BLANK);
    }

}
