package com.example.airshipgunner;

import static com.example.airshipgunner.GameView.screenRatioX;
import static com.example.airshipgunner.GameView.screenRatioY;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;

public class Bird {

    public int speed = 8;
    public boolean wasShot=true;

    int x, y,width,height,birdCounter=1;

    Bitmap birdie1, birdie2, birdie3, birdie4;

    Bird (Resources res){
        birdie1 = BitmapFactory.decodeResource(res, R.drawable.bird1);
        birdie2 = BitmapFactory.decodeResource(res, R.drawable.bird2);
        birdie3 = BitmapFactory.decodeResource(res, R.drawable.bird3);
        birdie4 = BitmapFactory.decodeResource(res, R.drawable.bird4);

        width = birdie1.getWidth();
        height = birdie1.getHeight();
        width /= 8;
        height /= 8;
        width *= (int) screenRatioX;
        height *= (int) screenRatioY;

        birdie1 = Bitmap.createScaledBitmap(birdie1,width,height,false);
        birdie2 = Bitmap.createScaledBitmap(birdie2,width,height,false);
        birdie3 = Bitmap.createScaledBitmap(birdie3,width,height,false);
        birdie4 = Bitmap.createScaledBitmap(birdie4,width,height,false);

        y = -height;
    }
    Bitmap getBird(){
        if (birdCounter == 1)
        {
            birdCounter++;
            return birdie1;
        }
        if (birdCounter == 2){
            birdCounter++;
        }
        if(birdCounter == 3){
            birdCounter++;
            return birdie3;
        }
        birdCounter = 1;
        return birdie4;
    }
    Rect getCollisionShape () {
        return new Rect(x ,y ,x + width,y + height);
    }
}
