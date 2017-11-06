package com.origami.spacedragons;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.Log;

import java.util.Random;

public class EnemyFast {
    private Rect hitbox;
    private Bitmap bitmap;
    private Rect frameFast;
    private RectF positionFast;
    private long lastFrameChangeTime = 0;
    private int frameLengthInMilliseconds = 200;
    private int currentFrame = 0, frameCount = 6;

    public EnemyFast(Context context) {
        int x = GameActivity.deviceWidth;
        int y = new Random().nextInt(GameActivity.deviceHeight);
        Log.d("Constructor", "");
        Log.d("C", Float.toString(y));
        while (y > (GameActivity.deviceHeight - 150)) {
            y = new Random().nextInt(GameActivity.deviceHeight);
            Log.d("Constructor", "/");
            Log.d("C", Float.toString(y));
        }
        frameFast = new Rect(0, 0, 90, 65);
        positionFast = new RectF(x, y, GameActivity.deviceWidth + 90, y + 65);
        bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.enemy_fast);
        bitmap = Bitmap.createScaledBitmap(bitmap, 540, 65, false);
        hitbox = new Rect(x, y, bitmap.getWidth(), bitmap.getHeight());
    }

    public void update() {
        frameFast.left = currentFrame * 90;
        frameFast.right = frameFast.left + 90;
        positionFast.left -= 12;
        positionFast.right -= 12;


        if (positionFast.left < -100) {
            positionFast.left = GameActivity.deviceWidth;
            positionFast.right = GameActivity.deviceWidth + 90;
            int y = new Random().nextInt(GameActivity.deviceHeight);
            while (y > (GameActivity.deviceHeight - 150)) {
                y = new Random().nextInt(GameActivity.deviceHeight);
            }
            positionFast.top = y;
            positionFast.bottom = y + 65;
        }

        hitbox = new Rect((int) positionFast.left+2, (int) positionFast.top, (int) positionFast.right, (int) positionFast.bottom);

    }

    public Bitmap getBitmap() {
        return bitmap;
    }

    public Rect getFrameFast() {
        return frameFast;
    }

    public RectF getPositionFast() {
        return positionFast;
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
