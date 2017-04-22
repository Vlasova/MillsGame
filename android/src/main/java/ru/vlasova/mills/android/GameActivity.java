package ru.vlasova.mills.android;

import android.app.Activity;
import android.content.Context;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Point;
import android.os.Bundle;
import android.view.Display;
import android.view.MotionEvent;
import android.view.View;
import android.graphics.Bitmap;
import android.graphics.Paint;

import java.util.ArrayList;

import ru.vlasova.mills.core.Cell;
import ru.vlasova.mills.core.CellStatus;
import ru.vlasova.mills.core.Game;

public class GameActivity extends Activity {

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(new GameView(this));
    }

    class GameView extends View {
        private Bitmap fieldBitmap;
        private ArrayList<Bitmap> whiteFigures;
        private ArrayList<Bitmap> blackFigures;
        Paint paint;
        Point point;
        Game game;

        GameView(Context context) {
            super(context);

            Display display = getWindowManager().getDefaultDisplay();
            point = new Point();
            display.getSize(point);
            int x = (int) Math.round(point.x * 0.9);
            fieldBitmap = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.field), x, x, false);

            paint = new Paint(Paint.ANTI_ALIAS_FLAG);

            x = fieldBitmap.getHeight()/11;
            whiteFigures = new ArrayList<>();
            blackFigures = new ArrayList<>();

            game = new Game();
        }

        protected void onDraw(Canvas canvas) {
            int x = ((point.x - fieldBitmap.getWidth())/2);
            int y = ((point.y - fieldBitmap.getWidth())/2)-x;
            canvas.drawBitmap(fieldBitmap, x, y, paint);
            Canvas fieldCanvas = new Canvas(fieldBitmap);
            drawField(fieldCanvas);
        }

        public void drawField(Canvas canvas) {
            int x = fieldBitmap.getHeight()/12;
            game.makeMove(2,2,2);
            whiteFigures.add(Bitmap.createScaledBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.white), x, x, false));
            Cell[][][] cells = game.getField();
            for(int i=0; i<3; i++)
                for(int j=0; j<3; j++)
                    for(int k=0; k<3; k++)
                        if(cells[i][j][k].getStatus().equals(CellStatus.OCCUPIED)) {
                            drawFigure(canvas, i, j, k, cells[i][j][k].getPiece().getColor());
                        }
        }

        private void drawFigure(Canvas canvas, int x, int y, int z, int color) {
            int coordX = calculateCoord(x, z);
            int coordY = calculateCoord(y, z);
            if(color == 0) {
                canvas.drawBitmap(whiteFigures.get(whiteFigures.size()), coordX, coordY, paint);
            }
            else {
                canvas.drawBitmap(blackFigures.get(blackFigures.size()), coordX, coordY, paint);
            }
        }

        private int calculateCoord(int coord, int z) {
            int trueCoord;
            int cellSize = fieldBitmap.getHeight()/14;
            int figureSize = whiteFigures.get(0).getHeight();
            if(coord==0)
                trueCoord = cellSize*(2*z+1)-figureSize;
            else if(coord==1)
               trueCoord = cellSize*7-figureSize;
            else
                trueCoord = cellSize*(13-2*z)-figureSize;
            return trueCoord;
        }
    }
}
