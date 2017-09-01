package ru.vlasova.mills.android;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);

        Typeface millTypeface = Typeface.createFromAsset(getAssets(), "fonts/MillFont.ttf");
        Typeface buttonTypeface = Typeface.createFromAsset(getAssets(), "fonts/font.otf");

        TextView tv = (TextView) findViewById(R.id.mill);
        tv.setTypeface(millTypeface);

        Button start = (Button) findViewById(R.id.buttonStart);
        start.setTypeface(buttonTypeface);
        View.OnClickListener startListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, GameActivity.class);
                startActivity(intent);
            }
        };
        start.setOnClickListener(startListener);

        Button regulations = (Button) findViewById(R.id.buttonRegulations);
        regulations.setTypeface(buttonTypeface);

        Button exit = (Button) findViewById(R.id.buttonExit);
        exit.setTypeface(buttonTypeface);
    }

}
