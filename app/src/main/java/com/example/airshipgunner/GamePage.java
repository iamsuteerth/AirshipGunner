package com.example.airshipgunner;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.WindowCompat;

import android.graphics.Point;
import android.os.Build;
import android.os.Bundle;
import android.view.Gravity;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

public class GamePage extends AppCompatActivity {

    public void onBackPressed() {
        finish();
    }

    private GameView gameview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        Window w = this.getWindow(); // in Activity's onCreate() for instance
        w.setFlags(WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED,
                WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED);
        super.onCreate(savedInstanceState);
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            WindowCompat.setDecorFitsSystemWindows(getWindow(), false);
        }
        Point point = new Point();
        getWindowManager().getDefaultDisplay().getSize(point);

        gameview = new GameView(this, point.x,point.y);

        setContentView(gameview);
        Toast toast = Toast.makeText(this, "Press on Left to fly and Release to drop.\nPress on Right to shoot.\nBut you cannot shoot while flying and vice versa!",Toast.LENGTH_LONG);
        toast.setGravity(Gravity.FILL_HORIZONTAL | Gravity.BOTTOM, 0, 0);
        toast.show();
    }

    @Override
    protected void onPause() {
        super.onPause();
        gameview.pause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        gameview.resume();
    }
}