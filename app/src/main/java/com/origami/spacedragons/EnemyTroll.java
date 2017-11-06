package com.origami.spacedragons;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;
import android.graphics.RectF;

import java.util.Random;

public class EnemyTroll {
    private Rect hitbox;
    private Bitmap bitmap;
    private Rect frameTroll;
    private RectF positionTroll;
    private long lastFrameChangeTime = 0;
    private int frameLengthInMilliseconds = 500;
    private int currentFrame = 0, frameCount = 1;
    private int acceleration = 10;



    public EnemyTroll(Context context) {
        int x = GameActivity.deviceWidth;
        int y = new Random().nextInt(GameActivity.deviceHeight);
        while (y > (GameActivity.deviceHeight - 150)) {
            y = new Random().nextInt(GameActivity.deviceHeight);
        }
        frameTroll = new Rect(0, 0, 100, 55);

        int genTroll = new Random().nextInt(5000);
        positionTroll = new RectF(x+2000+genTroll, y, GameActivity.deviceWidth+100+2000+genTroll, y+55);
        bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.enemy_troll);
        bitmap = Bitmap.createScaledBitmap(bitmap, 100, 55, false);
        hitbox = new Rect(x, y, bitmap.getWidth(), bitmap.getHeight());
    }

    public void update() {
        frameTroll.left = currentFrame * 100;
        frameTroll.right = frameTroll.left + 100;

        acceleration += 1;
        positionTroll.left -= acceleration;
        positionTroll.right -= acceleration;

        if (positionTroll.left >  GameActivity.deviceWidth) {
            acceleration = 10;
        }
        if (positionTroll.left < -100) {
            acceleration = 10;
            int placeRespawn;
            if (GameActivity.score > 1000) {
                placeRespawn = new Random().nextInt(250);
                placeRespawn += 250;
            }
            else {
                placeRespawn = new Random().nextInt(2000 - GameActivity.score);
                placeRespawn += 2000;
            }

            positionTroll.left = GameActivity.deviceWidth+placeRespawn;
            positionTroll.right = GameActivity.deviceWidth+placeRespawn+100;
            int y = new Random().nextInt(GameActivity.deviceHeight);
            while (y > (GameActivity.deviceHeight - 150)) {
                y = new Random().nextInt(GameActivity.deviceHeight);
            }
            positionTroll.top = y;
            positionTroll.bottom = y + 55;
        }
        hitbox = new Rect((int) positionTroll.left, (int) positionTroll.top, (int) positionTroll.right, (int) positionTroll.bottom);
    }

    public Bitmap getBitmap() {
        return bitmap;
    }

    public Rect getFrameTroll() {
        return frameTroll;
    }

    public RectF getPositionTroll() {
        return positionTroll;
    }

    public void getCurrentFrame() {
        long time = System.currentTimeMillis();
        if (time > lastFrameChangeTime + frameLengthInMilliseconds) {
            lastFrameChangeTime = time;
            currentFrame++;
            if (currentFrame >= frameCount) {
                currentFrame = 0;
            }
        }
        update();
    }

    public Rect getHitbox() {
        return hitbox;
    }
}
