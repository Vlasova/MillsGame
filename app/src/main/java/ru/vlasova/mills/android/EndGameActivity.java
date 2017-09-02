package ru.vlasova.mills.android;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

public class EndGameActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_endgame);

        Typeface buttonTypeface = Typeface.createFromAsset(getAssets(), "fonts/ButtonFont.otf");
        TextView tv = (TextView) findViewById(R.id.winner);
        tv.setTypeface(buttonTypeface);

        Button replay = (Button) findViewById(R.id.buttonReplay);
        replay.setTypeface(buttonTypeface);
        View.OnClickListener replayListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(EndGameActivity.this, GameActivity.class);
                startActivity(intent);
                finish();
            }
        };
        replay.setOnClickListener(replayListener);

        Button menu = (Button) findViewById(R.id.buttonMenu);
        menu.setTypeface(buttonTypeface);
        View.OnClickListener menuListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(EndGameActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        };
        menu.setOnClickListener(menuListener);
    }
}
