package com.origami.spacedragons;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;
import android.graphics.RectF;


public class JeanMarc {
    private Rect hitbox;
    private Bitmap bitmap;
    private Rect frameJeanMarc;
    private RectF positionJeanMarc;
    private long lastFrameChangeTime = 0;
    private int frameLengthInMilliseconds = 200;
    private int currentFrame = 0, frameCount = 4;

    public JeanMarc(Context context) {
        int x = GameActivity.deviceWidth / 8;
        int y = GameActivity.deviceHeight / 3;
        frameJeanMarc = new Rect(0, 0, 90, 65);
        positionJeanMarc = new RectF(x, y, x + 100, y + 75);
        bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.jeanmarc);
        bitmap = Bitmap.createScaledBitmap(bitmap, 360, 65, false);
        hitbox = new Rect(x, y, bitmap.getWidth(), bitmap.getHeight());
    }

    public void update() {

        frameJeanMarc.left = currentFrame * 90;
        frameJeanMarc.right = frameJeanMarc.left + 90;
        if (positionJeanMarc.top - GameActivity.zPosition * 3 < 0 || positionJeanMarc.bottom - GameActivity.zPosition * 3 > GameActivity.deviceHeight - 50) {

        } else {
            float newPositionTop = positionJeanMarc.top - GameActivity.zPosition * 3;
            float newPositionBottom = positionJeanMarc.bottom - GameActivity.zPosition * 3;

            if (newPositionTop - positionJeanMarc.top > 10 || newPositionTop - positionJeanMarc.top < -10) {

                if (newPositionTop - positionJeanMarc.top < -10) {
                    newPositionTop = positionJeanMarc.top - 10;
                    newPositionBottom = positionJeanMarc.bottom - 10;
                } else {
                    newPositionTop = positionJeanMarc.top + 10;
                    newPositionBottom = positionJeanMarc.bottom + 10;
                }
            }
            if (newPositionTop - positionJeanMarc.top > -5 && newPositionTop - positionJeanMarc.top < 0) {
                newPositionTop = positionJeanMarc.top - 3;
                newPositionBottom = positionJeanMarc.bottom - 3;

            }
            if (newPositionTop - positionJeanMarc.top > 0 && newPositionTop - positionJeanMarc.top < 5) {
                newPositionTop = positionJeanMarc.top + 3;
                newPositionBottom = positionJeanMarc.bottom + 3;
            }
            if (newPositionTop - positionJeanMarc.top > -3 && newPositionTop - positionJeanMarc.top < 4) {
                newPositionTop = positionJeanMarc.top;
                newPositionBottom = positionJeanMarc.bottom;
            }

            positionJeanMarc.top = newPositionTop;
            positionJeanMarc.bottom = newPositionBottom;

            hitbox = new Rect((int) positionJeanMarc.left+5, (int) positionJeanMarc.top+10, (int) positionJeanMarc.right-2, (int) positionJeanMarc.bottom-10);
        }
    }

    public Bitmap getBitmap() {
        return bitmap;
    }

    public Rect getFrameJeanMarc() {
        return frameJeanMarc;
    }

    public RectF getPositionJeanMarc() {
        return positionJeanMarc;
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
    }

    public Rect getHitbox() {
        return hitbox;
    }
}
