package ru.vlasova.mills.android;

import android.app.Activity;
import android.content.Intent;
import android.content.res.AssetManager;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ScrollView;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;

import java.io.InputStream;
import java.io.Reader;
import java.net.URL;
import java.nio.Buffer;

public class RegulationsActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_regulations);

        TextView tv = (TextView) findViewById(R.id.regulations);
        Typeface buttonTypeface = Typeface.createFromAsset(getAssets(), "fonts/ButtonFont.otf");
        tv.setTypeface(buttonTypeface);

        TextView text = (TextView) findViewById(R.id.text);
        try {
            text.setText(readFile());
        } catch (IOException e) {}

        Button button = (Button) findViewById(R.id.back);
        button.setTypeface(buttonTypeface);
        View.OnClickListener exitListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RegulationsActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        };
        button.setOnClickListener(exitListener);
    }

    private String readFile() throws IOException {
        AssetManager manager = this.getAssets();
        InputStreamReader isr = new InputStreamReader(manager.open("files/regulations.txt"), "windows-1251");
        BufferedReader br = new BufferedReader(isr);
        String str = "";
        String result = "";
        while ((str = br.readLine()) != null) {
            result += str;
            result += "\n";
        }
        return result;
    }
}
