package com.nativegame.graphbubbleshooter.engine;

import android.app.Activity;
import android.content.Context;

import com.nativegame.graphbubbleshooter.sound.SoundManager;

import java.util.ArrayList;

public class GameEngine {
    private final ArrayList<ArrayList<GameObject>> mLayers = new ArrayList<ArrayList<GameObject>>();
    private final ArrayList<GameObject> mGameObjects = new ArrayList<>();
    private final ArrayList<GameObject> mObjectsToAdd = new ArrayList<>();
    private final ArrayList<GameObject> mObjectsToRemove = new ArrayList<>();

    public final Activity mActivity;
    private final GameView mGameView;
    private UpdateThread mUpdateThread;
    private DrawThread mDrawThread;
    public SoundManager mSoundManager;
    public InputController mInputController;

    public final float mPixelFactor;
    public final int mScreenWidth;
    public final int mScreenHeight;

    public GameEngine(Activity activity, GameView gameView) {
        mActivity = activity;
        mGameView = gameView;
        mGameView.setGameObjects(mLayers);

        mScreenWidth = gameView.getWidth() - gameView.getPaddingRight() - gameView.getPaddingLeft();
        mScreenHeight = gameView.getHeight() - gameView.getPaddingTop() - gameView.getPaddingBottom();
        mPixelFactor = mScreenWidth / 2000f;
    }

    public void setSoundManager(SoundManager soundManager) {
        mSoundManager = soundManager;
    }

    public void setInputController(InputController inputController) {
        mInputController = inputController;
    }

    //--------------------------------------------------------
    // methods to change state of Game Engine
    // start, stop, pause, resume
    //--------------------------------------------------------

    public void startGame() {
        // Stop a game if it is running
        stopGame();

        // Setup the game objects
        int numGameObjects = mGameObjects.size();
        for (int i = 0; i < numGameObjects; i++) {
            mGameObjects.get(i).startGame();
        }

        if (mInputController != null) {
            mInputController.onStart();
        }

        // Start the update thread
        mUpdateThread = new UpdateThread(this);
        mUpdateThread.start();

        // Start the drawing thread
        mDrawThread = new DrawThread(this);
        mDrawThread.start();
    }

    public void stopGame() {
        if (mUpdateThread != null) {
            mUpdateThread.stopGame();
            mUpdateThread = null;
        }
        if (mDrawThread != null) {
            mDrawThread.stopGame();
        }
        if (mInputController != null) {
            mInputController.onStop();
        }
    }

    public void pauseGame() {
        if (mUpdateThread != null) {
            mUpdateThread.pauseGame();
        }
        if (mDrawThread != null) {
            mDrawThread.pauseGame();
        }
        if (mInputController != null) {
            mInputController.onPause();
        }
    }

    public void resumeGame() {
        if (mUpdateThread != null) {
            mUpdateThread.resumeGame();
        }
        if (mDrawThread != null) {
            mDrawThread.resumeGame();
        }
        if (mInputController != null) {
            mInputController.onResume();
        }
    }

    public boolean isRunning() {
        return mUpdateThread != null && mUpdateThread.isGameRunning();
    }

    public boolean isPaused() {
        return mUpdateThread != null && mUpdateThread.isGamePaused();
    }

    //=================================================================================

    public void onUpdate(long elapsedMillis) {
        int numObjects = mGameObjects.size();
        for (int i = 0; i < numObjects; i++) {
            mGameObjects.get(i).onUpdate(elapsedMillis, this);
            mGameObjects.get(i).onPostUpdate();
        }
        checkCollisions();
        synchronized (mLayers) {
            while (!mObjectsToRemove.isEmpty()) {
                GameObject objectToRemove = mObjectsToRemove.remove(0);
                mGameObjects.remove(objectToRemove);
                mLayers.get(objectToRemove.mLayer).remove(objectToRemove);
            }
            while (!mObjectsToAdd.isEmpty()) {
                GameObject gameObject = mObjectsToAdd.remove(0);
                addToLayerNow(gameObject);
            }
        }
    }

    public void onDraw() {
        mGameView.draw();
    }

    private void checkCollisions() {
        int numGameObjects = mGameObjects.size();
        for (int i = 0; i < numGameObjects; i++) {
            GameObject objectA = mGameObjects.get(i);
            for (int j = i + 1; j < numGameObjects; j++) {
                GameObject objectB = mGameObjects.get(j);
                if (objectA instanceof Sprite && objectB instanceof Sprite) {
                    Sprite spriteA = (Sprite) objectA;
                    Sprite spriteB = (Sprite) objectB;
                    if (spriteA.checkCollision(spriteB)) {
                        spriteA.onCollision(this, spriteB);
                        spriteB.onCollision(this, spriteA);
                    }
                }
            }
        }
    }

    private void addToLayerNow(GameObject object) {
        int layer = object.mLayer;

        // Add new layer if need
        while (mLayers.size() <= layer) {
            mLayers.add(new ArrayList<GameObject>());
        }

        mLayers.get(layer).add(object);
        mGameObjects.add(object);
    }

    public void addGameObject(final GameObject gameObject, int layer) {
        gameObject.mLayer = layer;
        if (isRunning()) {
            mObjectsToAdd.add(gameObject);
        } else {
            addToLayerNow(gameObject);
        }
    }

    public void removeGameObject(final GameObject gameObject) {
        mObjectsToRemove.add(gameObject);
    }

    public void onGameEvent(GameEvent gameEvent) {
        // We notify all the GameObjects
        int numObjects = mGameObjects.size();
        for (int i = 0; i < numObjects; i++) {
            mGameObjects.get(i).onGameEvent(gameEvent);
        }
    }

    public Context getContext() {
        return mGameView.getContext();
    }

}
