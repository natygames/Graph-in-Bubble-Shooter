package com.nativegame.graphbubbleshooter.engine;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.View;

import java.util.ArrayList;

public class GameView extends View {

    private ArrayList<ArrayList<GameObject>> mLayers;

    public GameView(Context context) {
        super(context);
    }

    public GameView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public GameView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void setGameObjects(ArrayList<ArrayList<GameObject>> gameObjects) {
        mLayers = gameObjects;
    }

    public void draw() {
        postInvalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        synchronized (mLayers) {
            int numLayers = mLayers.size();
            for (int i = 0; i < numLayers; i++) {
                ArrayList<GameObject> currentLayer = mLayers.get(i);
                int numObjects = currentLayer.size();
                for (int j = 0; j < numObjects; j++) {
                    currentLayer.get(j).onDraw(canvas);
                }
            }
        }
    }

}
