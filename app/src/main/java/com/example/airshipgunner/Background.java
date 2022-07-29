package com.example.airshipgunner;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

public class Background {

    int x = 0, y = 0;
    Bitmap background, gameover;
    // Constructor takes size of screen on x and y axis and takes object of resources used to decode bitmap from drawable
    Background(int screenX, int screenY, Resources res) {

        background = BitmapFactory.decodeResource(res,R.drawable.gamemap);
        background = Bitmap.createScaledBitmap(background, screenX, screenY, false);
        gameover = BitmapFactory.decodeResource(res,R.drawable.game_over);
        gameover = Bitmap.createScaledBitmap(gameover,512,256,false);
    }
}
