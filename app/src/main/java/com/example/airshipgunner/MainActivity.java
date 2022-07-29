package com.example.airshipgunner;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.WindowCompat;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private boolean isMute = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            WindowCompat.setDecorFitsSystemWindows(getWindow(), false);
        }

        findViewById(R.id.play).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, GamePage.class));
            }
        });

        TextView highScoreText = (TextView) findViewById(R.id.highScoreText);

        final SharedPreferences preferences = getSharedPreferences("Game",MODE_PRIVATE);
        highScoreText.setText( "HighScore " + preferences.getInt("highscore",0) );

        isMute = preferences.getBoolean("isMute",false);

        final ImageView volctrl = (ImageView) findViewById(R.id.mute);

        if(isMute){
            volctrl.setImageResource(R.drawable.volume_up);
        }
        else {
            volctrl.setImageResource(R.drawable.volume_off);
        }

        volctrl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isMute = !isMute;
                if(isMute){
                    volctrl.setImageResource(R.drawable.volume_up);
                }
                else {
                    volctrl.setImageResource(R.drawable.volume_off);
                }
                SharedPreferences.Editor editor = preferences.edit();
                editor.putBoolean("isMute",isMute);
                editor.apply();
            }
        });

    }
}