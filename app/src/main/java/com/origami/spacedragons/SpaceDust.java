package com.origami.spacedragons;

import java.util.Random;

public class SpaceDust {
    private int x, y;
    private int xMax, yMax;

    public SpaceDust(int screenWidth, int screenHeight) {
        xMax = screenWidth;
        yMax = screenHeight;
        x = new Random().nextInt(xMax);
        y = new Random().nextInt(yMax);
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }
}
