package com.example.airshipgunner;

import static java.lang.Runtime.getRuntime;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Build;
import android.view.MotionEvent;
import android.view.SurfaceView;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class GameView extends SurfaceView implements Runnable {

    public int score, sound, sound2;
    private Thread thread;
    private boolean isPlaying;
    private int screenX, screenY;
    private Paint paint;
    private Background background1, background2,background3,gover;
    private Flight flight;
    private List<Bullet> bullets;
    private Bird[] birds;
    private Random rand;
    public static float screenRatioX, screenRatioY; //For maintaining speed and uniformity across devices
    private boolean isGameOver=false;
    private SharedPreferences preferences;
    private GamePage activity;
    private SoundPool soundPool;

    public GameView(GamePage activity,int screenX, int screenY) {

        super(activity);
        this.activity = activity;
        this.screenX = screenX;
        this.screenY = screenY;
        //screenRatio must be set to manual integer for emulator
        screenRatioX = Resources.getSystem().getDisplayMetrics().widthPixels/screenX ;
        screenRatioY = Resources.getSystem().getDisplayMetrics().heightPixels/ screenY;

        preferences= activity.getSharedPreferences("Game",Context.MODE_PRIVATE);

        if ( Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP ) {

            AudioAttributes audioAttributes = new AudioAttributes.Builder()
                    .setUsage(AudioAttributes.USAGE_GAME)
                    .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                    .build();

            soundPool = new SoundPool.Builder()
                        .setAudioAttributes(audioAttributes)
                        .build();

        } else {
            soundPool = new SoundPool(10, AudioManager.STREAM_MUSIC,0);
        }

        sound = soundPool.load(activity, R.raw.shoot,1);
        sound2 = soundPool.load(activity, R.raw.hq_explosion,1);

        paint = new Paint();
        paint.setTextSize(64);
        paint.setTextSize(Color.BLACK);

        background1 = new Background(screenX, screenY, getResources());
        background2 = new Background(screenX, screenY, getResources());
        background3 = new Background(screenX,screenY,getResources());
        gover = new Background(screenX/2,screenY/2,getResources());
        flight = new Flight(this, screenY,getResources());

        bullets = new ArrayList<>();

        background2.x = screenX;
        background3.x = screenX + background2.background.getWidth();

        birds = new Bird[4]; //4 birds on screen at a time

        for (int i = 0;i<4;i++){
            Bird bird = new Bird(getResources());
            birds[i] = bird;
        }

        rand = new Random();
    }

    @Override //Implementing and overriding Runnable methods
    public void run() {
        while ( isPlaying ) {
            update();
            draw();
            sleep();
            // We need to give initial positions and draw the car on screen and then update the position
        }
    }
    private void update() {

        background1.x -= (16f * screenRatioX);
        background2.x -= (16f * screenRatioX);
        background3.x -= (16f * screenRatioX);

        if ( background1.x + background1.background.getWidth() < 0 ) { // < 0 means img is off the screen
            background1.x = screenX;
        }

        if ( background2.x + background2.background.getWidth() <= 0 ) { // < 0 means img is off the screen
            background2.x = screenX;
        }

        if (background3.x + background3.background.getWidth() < 0) {
            background3.x = background2.x + background2.background.getWidth();
        }

        if (flight.isGoingUp) {
            flight.y -= 18 * screenRatioX; //- means that we are moving up on canvas
        } else {
            flight.y += 18 * screenRatioY;
        }

        if (flight.y < 0){
            flight.y = 0;
        }

        if (flight.y > screenY - flight.height) { //True if flight is off the screen
            flight.y = screenY - flight.height; //To fix this case, we correct it
        }

        List<Bullet> garb = new ArrayList<>();

        for (Bullet bullet : bullets) {
            if (bullet.x > screenX) {
                garb.add(bullet);
            }
            bullet.x += 60 * screenRatioX;

            for(Bird bird : birds) {
                if(Rect.intersects(bird.getCollisionShape(),
                        bullet.getCollisionShape())) {
                    score++;
                    bird.x = -500;
                    bullet.x = screenX + 500;
                    getRuntime().gc();//Places bullet off screen, thus the condition is recheckec
                    bird.wasShot = true;
                }
            }
        }

        for ( Bullet bullet : garb ) {
            bullets.remove(bullet);
            getRuntime().gc();
        }

        for ( Bird bird : birds ) {

            bird.x -= bird.speed;

            if ( bird.x + bird.width < 0 ) {

                if(!bird.wasShot) {
                    isGameOver = true;
                    return;
                }

                int bound = (int) (20*screenRatioX);
                bird.speed = rand.nextInt(bound);

                if ( bird.speed < 4 * screenRatioX ) {
                    bird.speed = (int)(4 * screenRatioX);
                }

                bird.x = screenX;
                bird.y = rand.nextInt(screenY - bird.height); //So birdie doesnt go out

                bird.wasShot = false;
            }
            if ( Rect.intersects( bird.getCollisionShape(), flight.getCollisionShape() ) ) {
                isGameOver = true;
                return;
            }
        }

        getRuntime().gc();
        garb.clear();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        getRuntime().gc();
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                if (event.getX() < screenX / 2) {
                    flight.isGoingUp = true;
                }
                break;
            case MotionEvent.ACTION_UP:
                flight.isGoingUp = false;
                if (event.getX() > screenX / 2) {
                    flight.toShoot++;
                }
                break;
        }
        return true;
    }

    private void draw() {
        if ( getHolder().getSurface().isValid() ) { // If surface view object has successfully initiated

            Canvas canvas = getHolder().lockCanvas();

            canvas.drawBitmap(background1.background, background1.x, background1.y, paint);
            canvas.drawBitmap(background2.background, background2.x, background2.y, paint);
            canvas.drawBitmap(background3.background, background3.x, background3.y, paint);

            for (Bird bird : birds) {
                canvas.drawBitmap(bird.getBird(),bird.x,bird.y,paint);
            }

            canvas.drawText(Integer.toString(score),screenX/2f,172,paint);

            if ( isGameOver ) {

                isPlaying = false;
                canvas.drawBitmap(flight.getDed(),flight.x,flight.y,paint);
                canvas.drawBitmap(gover.gameover,((canvas.getWidth()-gover.background.getWidth())/1.4f),((canvas.getHeight()-gover.background.getHeight())/1.5f),paint);
                getHolder().unlockCanvasAndPost(canvas);

                saveIfHigScore();
                waitBeforeExiting();
                return;
            }

            canvas.drawBitmap(flight.getFlight(),flight.x,flight.y,paint);

            for (Bullet bullet : bullets){
                canvas.drawBitmap(bullet.bullet,bullet.x, bullet.y, paint);
            }
            getHolder().unlockCanvasAndPost(canvas); //To show canvas on screen
        }
        getRuntime().gc();
    }

    private void waitBeforeExiting() {
        if(preferences.getBoolean("isMute",false)){
            soundPool.play(sound2,1,1,0,0,1);
        }
        try {
            Thread.sleep(3000);
            getRuntime().gc();
            activity.startActivity(new Intent(activity, MainActivity.class));
            activity.finish();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void saveIfHigScore() {
        if(preferences.getInt("highscore",0) < score){
            SharedPreferences.Editor editor = preferences.edit();
            editor.putInt("highscore", score);
            editor.apply();
        }
    }

    private void sleep() {
        try {
            Thread.sleep(3);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        getRuntime().gc();
    }

    public void resume(){ //Resume/Start the game
        isPlaying = true;
        thread = new Thread(this);
        thread.start();
    }

    public void pause(){ //Pause the game
        try {
            isPlaying = false;
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }


    public void newBullet() {
        if(preferences.getBoolean("isMute",false)){
            soundPool.play(sound,1,1,0,0,1);
        }
        Bullet bullet = new Bullet(getResources());
        bullet.x = flight.x + flight.width;
        bullet.y = flight.y + (int)((float)flight.height / 1.55);//Place bullet appropriately
        bullets.add(bullet);
        getRuntime().gc();
    }
}