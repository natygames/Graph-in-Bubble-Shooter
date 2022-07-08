package com.nativegame.graphbubbleshooter.game;

import com.nativegame.graphbubbleshooter.R;

/**
 * Created by Oscar Liang on 2022/07/08
 */

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
