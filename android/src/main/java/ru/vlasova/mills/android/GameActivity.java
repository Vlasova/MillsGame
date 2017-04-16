package ru.vlasova.mills.android;

import android.app.Activity;
import android.content.Context;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Point;
import android.os.Bundle;
import android.view.Display;
import android.view.View;
import android.graphics.Bitmap;
import android.graphics.Paint;

public class GameActivity extends Activity {

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(new GameView(this));
    }

    class GameView extends View {
        Bitmap fieldBitmap;
        Bitmap figureBitmap;
        Paint paint;
        Point point;

        GameView(Context context) {
            super(context);

            Display display = getWindowManager().getDefaultDisplay();
            point = new Point();
            display.getSize(point);
            int x = (int) Math.round(point.x * 0.9);

            fieldBitmap = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.field), x, x, false);
            paint = new Paint(Paint.ANTI_ALIAS_FLAG);

            x = fieldBitmap.getHeight()/9;
            figureBitmap = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.figure), x, x, false);
        }
        protected void onDraw(Canvas canvas) {
            int x = ((point.x - fieldBitmap.getWidth())/2);
            int y = ((point.y - fieldBitmap.getWidth())/2)-x;
            canvas.drawBitmap(fieldBitmap, x, y, paint);
            Canvas newCan = new Canvas(fieldBitmap);
            x = fieldBitmap.getHeight()/30*15- figureBitmap.getWidth()/2;
            y = fieldBitmap.getHeight()/30*10- figureBitmap.getHeight()/2;
            newCan.drawBitmap(figureBitmap, x, y, paint);
        }
    }
}
