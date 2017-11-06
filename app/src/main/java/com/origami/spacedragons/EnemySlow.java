package com.origami.spacedragons;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;
import android.graphics.RectF;

import java.util.Random;

public class EnemySlow {
    private Rect hitbox;
    private Bitmap bitmap;
    private Rect frameSlow;
    private RectF positionSlow;
    private long lastFrameChangeTime = 0;
    private int frameLengthInMilliseconds = 250;
    private int currentFrame = 0, frameCount = 2;

    public EnemySlow(Context context) {
        int x = GameActivity.deviceWidth;
        int y = new Random().nextInt(GameActivity.deviceHeight);
        while (y > (GameActivity.deviceHeight - 150)) {
            y = new Random().nextInt(GameActivity.deviceHeight);
        }
        frameSlow = new Rect(0, 0, 90, 65);
        positionSlow = new RectF(x, y, GameActivity.deviceWidth+90, y+65);
        bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.enemy_slow);
        bitmap = Bitmap.createScaledBitmap(bitmap, 180, 65, false);
        hitbox = new Rect(x, y, bitmap.getWidth(), bitmap.getHeight());
    }

    public void update() {
        frameSlow.left = currentFrame * 90;
        frameSlow.right = frameSlow.left + 90;
        positionSlow.left -= 5;
        positionSlow.right -= 5;

        if (positionSlow.left < -100) {
            positionSlow.left = GameActivity.deviceWidth;
            positionSlow.right = GameActivity.deviceWidth+90;
            int y = new Random().nextInt(GameActivity.deviceHeight);
            while (y > (GameActivity.deviceHeight - 150)) {
                y = new Random().nextInt(GameActivity.deviceHeight);
            }
            positionSlow.top = y;
            positionSlow.bottom = y + 65;
        }
        hitbox = new Rect((int) positionSlow.left+4, (int) positionSlow.top+12, (int) positionSlow.right, (int) positionSlow.bottom-10);
    }

    public Bitmap getBitmap() {
        return bitmap;
    }

    public Rect getFrameSlow() {
        return frameSlow;
    }

    public RectF getPositionSlow() {
        return positionSlow;
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
