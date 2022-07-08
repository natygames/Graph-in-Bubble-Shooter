package com.nativegame.graphbubbleshooter.engine;

public class DrawThread extends GameThread {

    private static final int SLEEP_TIME = 16;   // This is 60 fps

    public DrawThread(GameEngine gameEngine) {
        super(gameEngine);
    }

    protected void doIt(long elapsedMillis) {

        if (elapsedMillis < SLEEP_TIME) {
            try {
                Thread.sleep(SLEEP_TIME - elapsedMillis);
            } catch (InterruptedException e) {
                // We just continue
            }
        }

        mGameEngine.onDraw();

    }

}
