package com.example.airshipgunner;

import static com.example.airshipgunner.GameView.screenRatioX;
import static com.example.airshipgunner.GameView.screenRatioY;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;

public class Flight {

    public boolean isGoingUp = false;
    public int toShoot = 0;
    int x, y, width, height, afterburner = 0, shootCounter = 1;
    Bitmap f1,f2;
    Bitmap shoot1, shoot2, shoot3, shoot4, shoot5, ded;
    private GameView gameview;

    Flight ( GameView gameview,int screenY, Resources res ) {

        this.gameview = gameview;

        f1 = BitmapFactory.decodeResource(res,R.drawable.base);
        f2 = BitmapFactory.decodeResource(res,R.drawable.base2);

        width = f1.getWidth();
        height = f1.getHeight();
        width /= 10;
        height /=10;
        width *= (int) screenRatioX;
        height *= (int) screenRatioY;

        f1 = Bitmap.createScaledBitmap(f1,width, height, false);
        f2 = Bitmap.createScaledBitmap(f2, width, height, false);

        shoot1 = BitmapFactory.decodeResource(res, R.drawable.shootzero);
        shoot2 = BitmapFactory.decodeResource(res, R.drawable.shootone);
        shoot3 = BitmapFactory.decodeResource(res, R.drawable.shoottwo);
        shoot4 = BitmapFactory.decodeResource(res, R.drawable.shootthree);
        shoot5 = BitmapFactory.decodeResource(res, R.drawable.shootfour);

        shoot1 = Bitmap.createScaledBitmap(shoot1,width,height,false);
        shoot2 = Bitmap.createScaledBitmap(shoot2,width,height,false);
        shoot3 = Bitmap.createScaledBitmap(shoot3,width,height,false);
        shoot4 = Bitmap.createScaledBitmap(shoot4,width,height,false);
        shoot5 = Bitmap.createScaledBitmap(shoot5,width,height,false);

        ded = BitmapFactory.decodeResource(res, R.drawable.ded);
        ded = Bitmap.createScaledBitmap(ded,width,height,false);

        y = screenY / 2;
        x = (int) (64 * screenRatioX);

    }

    Bitmap getFlight() {

        if (toShoot!=0){ //Means that we can shoot bullets

            if(shootCounter == 1){
                shootCounter++;
                return shoot1;
            }

            if(shootCounter == 2){
                shootCounter++;
                return shoot2;
            }

            if(shootCounter == 3){
                shootCounter++;
                return shoot3;
            }

            if(shootCounter == 4){
                shootCounter++;
                return shoot4;
            }

            shootCounter = 1;
            toShoot--;
            gameview.newBullet();

            return shoot5;
        }
        if(afterburner==0){ //When we call it for first time while drawing on canvas, aburner is 0 and the cycle continues
            afterburner++;
            return f1;
        }
        afterburner--;
        return f2;

    }
    Rect getCollisionShape (){
        return new Rect(x,y,x+width,y+height);
    }
    Bitmap getDed(){
        return ded;
    }
}
