package com.nativegame.graphbubbleshooter.input;

import android.view.MotionEvent;
import android.view.View;

import com.nativegame.graphbubbleshooter.engine.GameEvent;
import com.nativegame.graphbubbleshooter.engine.InputController;
import com.nativegame.graphbubbleshooter.R;
import com.nativegame.graphbubbleshooter.engine.GameEngine;

public class BasicInputController extends InputController {

    private final GameEngine mGameEngine;

    public BasicInputController(GameEngine gameEngine) {
        mGameEngine = gameEngine;
        gameEngine.mActivity.findViewById(R.id.game_view).setOnTouchListener(new BasicOnTouchListener());
    }

    private class BasicOnTouchListener implements View.OnTouchListener {
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            int action = event.getAction();
            if (action == MotionEvent.ACTION_DOWN) {
                mAiming = true;
                mXDown = (int) event.getX();
                mYDown = (int) event.getY();
                // Log.d("input", "(" + mXDown + ", " + mYDown + ")");
            } else if (action == MotionEvent.ACTION_MOVE) {
                mXDown = (int) event.getX();
                mYDown = (int) event.getY();
                // Log.d("input", "(" + mXDown + ", " + mYDown + ")");
            } else if (action == MotionEvent.ACTION_UP) {
                mAiming = false;
                mXUp = (int) event.getX();
                mYUp = (int) event.getY();
                // Log.d("input", "(" + mXUp + ", " + mYUp + ")");

                mGameEngine.onGameEvent(GameEvent.SHOOT);
            }

            return true;
        }
    }

}
