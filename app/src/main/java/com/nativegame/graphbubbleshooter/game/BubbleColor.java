package com.nativegame.graphbubbleshooter.game;

import com.nativegame.graphbubbleshooter.R;

public enum BubbleColor {
    RED,
    YELLOW,
    BLUE,
    BLANK;

    public int getImageResId() {
        switch (this) {
            case BLUE:
                return R.drawable.circle_blue;
            case RED:
                return R.drawable.circle_red;
            case YELLOW:
                return R.drawable.circle_yellow;
            case BLANK:
                return R.drawable.circle_gray;
        }
        return 0;
    }
}
