package com.nativegame.graphbubbleshooter.engine;

import android.graphics.Canvas;

public abstract class GameObject {

    public int mLayer;

    public abstract void startGame();

    public abstract void onUpdate(long elapsedMillis, GameEngine gameEngine);

    public abstract void onDraw(Canvas canvas);

    public void onPostUpdate() {
    }

    public void onGameEvent(GameEvent gameEvent) {
    }

}
