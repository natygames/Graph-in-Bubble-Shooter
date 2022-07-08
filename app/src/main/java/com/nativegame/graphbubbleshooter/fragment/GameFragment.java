package com.nativegame.graphbubbleshooter.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.nativegame.graphbubbleshooter.R;
import com.nativegame.graphbubbleshooter.engine.GameEngine;
import com.nativegame.graphbubbleshooter.engine.GameView;
import com.nativegame.graphbubbleshooter.game.BubbleManager;
import com.nativegame.graphbubbleshooter.game.Player;
import com.nativegame.graphbubbleshooter.input.BasicInputController;

public class GameFragment extends BaseFragment {

    private GameEngine mGameEngine;

    public GameFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_game, container, false);
    }

    @Override
    protected void onLayoutCompleted() {
        startGame();
    }

    private void startGame() {
        // Init engine
        mGameEngine = new GameEngine(getMainActivity(), (GameView) getView().findViewById(R.id.game_view));
        mGameEngine.setInputController(new BasicInputController(mGameEngine));
        mGameEngine.setSoundManager(getMainActivity().getSoundManager());

        // Init bubble
        char[][] bubbleArray = new char[][]{
                {'0', 'r', 'r', 'r', 'r', 'r', 'r', 'r', 'r', '0'},
                {'0', 'y', 'y', 'y', 'y', 'y', 'y', 'y', '0', '0'},
                {'0', '0', 'b', 'b', 'b', 'b', 'b', 'b', '0', '0'},
                {'0', '0', 'r', 'r', 'r', 'r', 'r', '0', '0', '0'},
                {'0', '0', '0', 'y', 'y', 'y', 'y', '0', '0', '0'},
                {'0', '0', '0', 'b', 'b', 'b', '0', '0', '0', '0'},
                {'0', '0', '0', '0', 'r', 'r', '0', '0', '0', '0'},
                {'0', '0', '0', '0', 'y', '0', '0', '0', '0', '0'},
                {'0', '0', '0', '0', '0', '0', '0', '0', '0', '0'}};
        // When the bubble is added exceed the max row, app will throw index out of bound exception,
        // so the better way is to set a fix row number and show a line at bottom, and when player
        // add bubble over the line, the game is over
        BubbleManager bubbleManager = new BubbleManager(mGameEngine, bubbleArray);

        // Add all the object to engine
        mGameEngine.addGameObject(new Player(mGameEngine, bubbleManager), 2);
        // mGameEngine.addGameObject(new FPSCounter(mGameEngine), 0);
        mGameEngine.startGame();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (mGameEngine != null && mGameEngine.isRunning() && mGameEngine.isPaused()) {
            mGameEngine.resumeGame();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (mGameEngine.isRunning() && !mGameEngine.isPaused()) {
            mGameEngine.pauseGame();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mGameEngine.stopGame();
    }

    @Override
    public boolean onBackPressed() {
        getMainActivity().finish();
        return true;
    }

}
