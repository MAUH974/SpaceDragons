package com.origami.spacedragons;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.media.MediaPlayer;
import android.util.AttributeSet;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;

import java.util.ArrayList;
import java.util.Random;


public class GameScene extends SurfaceView implements Runnable {

    private GameActivity gameActivity;


    private int lives = 3;
    private volatile boolean running;
    private Thread gameThread = null;

    public JeanMarc jeanMarc;

    private Paint paint;
    private Canvas canvas;
    private SurfaceHolder surfaceHolder;

    private Life life;
    private EnemyTroll enemyTroll;
    private ArrayList<SpaceDust> spaceDusts;
    private ArrayList<EnemySlow> enemiesSlow;
    private ArrayList<EnemyFast> enemiesFast;
    private int indexSlow = 0, indexFast = 0;

    private long timeOfDeath = 0;

    private MediaPlayer mediaPlayerHit, mediaPlayerDeath;

    public GameScene(Context context) {
        super(context);

        mediaPlayerHit = MediaPlayer.create(getContext(), R.raw.soundhit);
        mediaPlayerDeath = MediaPlayer.create(getContext(), R.raw.sounddeath);

        surfaceHolder = getHolder();
        paint = new Paint();

        createSpaceDusts();
        life = new Life(context);
        jeanMarc = new JeanMarc(context);
        createEnemies();
    }

    public GameScene(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);

        mediaPlayerHit = MediaPlayer.create(getContext(), R.raw.soundhit);
        mediaPlayerDeath = MediaPlayer.create(getContext(), R.raw.sounddeath);

        surfaceHolder = getHolder();
        paint = new Paint();

        createSpaceDusts();
        life = new Life(context);
        jeanMarc = new JeanMarc(context);
        createEnemies();
    }

    public GameScene(Context context, AttributeSet attributeSet, int defStyle) {
        super(context, attributeSet, defStyle);

        mediaPlayerHit = MediaPlayer.create(getContext(), R.raw.soundhit);
        mediaPlayerDeath = MediaPlayer.create(getContext(), R.raw.sounddeath);

        surfaceHolder = getHolder();
        paint = new Paint();

        createSpaceDusts();
        life = new Life(context);
        jeanMarc = new JeanMarc(context);
        createEnemies();
    }

    @Override
    public void run() {
        while (running) {
            draw();
            getCollisions();
            control();
        }
    }


    private void draw() {
        if (surfaceHolder.getSurface().isValid()) {
            canvas = surfaceHolder.lockCanvas();
            canvas.drawColor(Color.argb(255, 0, 0, 0));

            paint.setColor(Color.argb(255, 255, 255, 255));
            for (SpaceDust sd : spaceDusts) {
                canvas.drawPoint(sd.getX(), sd.getY(), paint);
            }

            jeanMarc.getCurrentFrame();
            canvas.drawBitmap(jeanMarc.getBitmap(), jeanMarc.getFrameJeanMarc(), jeanMarc.getPositionJeanMarc(), paint);

            spawnEnemies();

            surfaceHolder.unlockCanvasAndPost(canvas);
        }
    }

    private void drawLives() {
        for (int i = 0; i < lives; i++) {
            canvas.drawBitmap(life.getBitmap(), life.getX() + i * 40, life.getY(), paint);
        }
    }

    private void drawHUD() {
        drawLives();
        paint.setTextSize(30);
        canvas.drawText("SCORE", 10, GameActivity.deviceHeight / 16, paint);
        canvas.drawText(Integer.toString(GameActivity.score), 10, GameActivity.deviceHeight * 2 / 16, paint);
        GameActivity.score++;
    }

    private void createSpaceDusts() {
        spaceDusts = new ArrayList<>();
        for (int i = 0; i < 1024; i++) {
            SpaceDust spaceDust = new SpaceDust(GameActivity.deviceWidth, GameActivity.deviceHeight);
            spaceDusts.add(spaceDust);
        }
    }

    private void createEnemies() {
        enemyTroll = new EnemyTroll(getContext());
        enemiesSlow = new ArrayList<>();
        enemiesFast = new ArrayList<>();

        for (int i = 0; i <= 10; i++) {
            enemiesSlow.add(new EnemySlow(getContext()));
            enemiesFast.add(new EnemyFast(getContext()));
        }
    }

    private void spawnEnemies() {
        drawHUD();
        spawnSlow();
        spawnFast();
        spawnTroll();
    }

    private void spawnSlow() {
        for (int i = 0; i < indexSlow; i++) {
            enemiesSlow.get(i).getCurrentFrame();
            canvas.drawBitmap(enemiesSlow.get(i).getBitmap(), enemiesSlow.get(i).getFrameSlow(), enemiesSlow.get(i).getPositionSlow(), paint);
        }

        int genSlow = new Random().nextInt(300);
        if (genSlow == 3) {
            if (indexSlow < 10) {
                indexSlow++;
            }
        }
    }

    private void spawnFast() {
        for (int i = 0; i < indexFast; i++) {
            enemiesFast.get(i).getCurrentFrame();
            canvas.drawBitmap(enemiesFast.get(i).getBitmap(), enemiesFast.get(i).getFrameFast(), enemiesFast.get(i).getPositionFast(), paint);
        }

        int genFast = new Random().nextInt(400);
        if (genFast == 4) {
            if (indexFast < 5) {
                indexFast++;
            }
        }
    }

    private void spawnTroll() {
        enemyTroll.getCurrentFrame();
        canvas.drawBitmap(enemyTroll.getBitmap(), enemyTroll.getFrameTroll(), enemyTroll.getPositionTroll(), paint);
    }

    private void getCollisions() {
        boolean hitDetected = false;
        for (EnemySlow es : enemiesSlow) {
            if (Rect.intersects(jeanMarc.getHitbox(), es.getHitbox())) {
                hitDetected = true;
            }
        }
        for (EnemyFast ef : enemiesFast) {
            if (Rect.intersects(jeanMarc.getHitbox(), ef.getHitbox())) {
                hitDetected = true;
            }
        }

        if (Rect.intersects(jeanMarc.getHitbox(), enemyTroll.getHitbox())) {
            hitDetected = true;
        }

        if (hitDetected) {
            --lives;


            if ((System.currentTimeMillis() - timeOfDeath) < 3000) {
                ++lives;
            } else {
                timeOfDeath = System.currentTimeMillis();
                mediaPlayerHit.start();
            }

            if (lives <= 0) {
                mediaPlayerDeath.start();
                gameOver();
                running = false;
            }
        }
    }

    private void control() {
        try {
            gameThread.sleep(17);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void pause() {
        running = false;
        try {
            gameThread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void resume() {
        running = true;
        gameThread = new Thread(this);
        gameThread.start();
    }

    private void gameOver() {
        gameActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                gameActivity.getReplay().setBackgroundResource(0);
                gameActivity.getMap().setBackgroundResource(0);
                gameActivity.getReplay().setVisibility(View.VISIBLE);
                gameActivity.getMap().setVisibility(View.VISIBLE);
            }
        });
    }

    public JeanMarc getJeanMarc() {
        return jeanMarc;
    }

    public void setGameActivity(GameActivity gameActivity) {
        this.gameActivity = gameActivity;
    }
}

