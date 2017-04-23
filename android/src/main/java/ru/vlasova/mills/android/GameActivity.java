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
        private Bitmap whiteFigure;
        private Bitmap blackFigure;
        private Paint paint;
        private Point point;
        private Game game;
        private int cellSize;

        GameView(Context context) {
            super(context);

            Display display = getWindowManager().getDefaultDisplay();
            point = new Point();
            display.getSize(point);
            int x = (int) (point.x * 0.9);
            fieldBitmap = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.field), x, x, false);

            paint = new Paint(Paint.ANTI_ALIAS_FLAG);

            x = (int) fieldBitmap.getHeight()/9;
            whiteFigure = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.white), x, x, false);
            blackFigure = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.black), x, x, false);

            game = new Game();

            cellSize = fieldBitmap.getHeight()/14;
        }

        protected void onDraw(Canvas canvas) {
            int x = ((point.x - fieldBitmap.getWidth())/2);
            int y = ((point.y - fieldBitmap.getWidth())/2)-x;
            canvas.drawBitmap(fieldBitmap, x, y, paint);
            Canvas fieldCanvas = new Canvas(fieldBitmap);
            drawField(fieldCanvas);
        }

        public boolean onTouchEvent(MotionEvent event) {
            double eventX = event.getX();
            double eventY = event.getY();
            Point coordXY = calculateXY(eventX, eventY);
            int x = coordXY.x;
            int y = coordXY.y;
            int z = calculateZ(eventX, eventY);
            if(!game.isAllPiecesSet() && x != -1 && y != -1 && z != -1) {
                game.makeMove(x, y, z);
            }
            invalidate();
            return false;
        }

        public void drawField(Canvas canvas) {

            Cell[][][] cells = game.getField();
            for(int i=0; i<3; i++)
                for(int j=0; j<3; j++)
                    for(int k=0; k<3; k++)
                        if(cells[i][j][k].getStatus().equals(CellStatus.OCCUPIED)) {
                            drawFigure(canvas, i, j, k, cells[i][j][k].getPiece().getColor());
                        }
        }

        private void drawFigure(Canvas canvas, int x, int y, int z, int color) {
            int coordX = calculateCoordAfterMove(x, z);
            int coordY = calculateCoordAfterMove(y, z);
            if(color == 0) {
                canvas.drawBitmap(whiteFigure, coordX, coordY, paint);
            }
            else {
                canvas.drawBitmap(blackFigure, coordX, coordY, paint);
            }
        }

        private int calculateCoordAfterMove(int coord, int z) {
            int trueCoord;
            int figureSize = whiteFigure.getHeight();
            if(coord==0)
                trueCoord = cellSize*(2*z+1)-figureSize/2;
            else if(coord==1)
               trueCoord = cellSize*7-figureSize/2;
            else
                trueCoord = cellSize*(13-2*z)-figureSize/2;
            return trueCoord;
        }

        private int calculateCoordBeforeMove(int coord) {
            int trueCoord;
            if(coord<6*cellSize)
                trueCoord = 0;
            else if(coord>=6*cellSize && coord<=8*cellSize)
                trueCoord = 1;
            else if(coord>8*cellSize && coord<14*cellSize)
                trueCoord = 2;
            else trueCoord = -1;
            return trueCoord;

        }

        private Point calculateXY(double x, double y) {
            int trueX = (int) (x - (point.x - fieldBitmap.getWidth())/2);
            int trueY = (int) (y - (point.y - fieldBitmap.getHeight())/2);
            trueX = calculateCoordBeforeMove(trueX);
            trueY = calculateCoordBeforeMove(trueY);
            return new Point(trueX, trueY);
        }

        private int calculateZ(double x, double y) {
            int trueX = (int) (x - (point.x - fieldBitmap.getWidth())/2);
            int trueY = (int) (y - (point.y - fieldBitmap.getHeight())/2);
            int z;
            if(trueX<=cellSize || trueX>=13*cellSize)
                z = 0;
            else if((trueX>=2*cellSize && trueX<=4*cellSize) || (trueX>=10*cellSize && trueX<=12*cellSize))
                z = 1;
            else if((trueX>=4*cellSize && trueX<=6*cellSize) || (trueX>=8*cellSize && trueX<=10*cellSize))
                z = 2;
            else if(trueX>6*cellSize && trueX<8*cellSize) {
                if(trueY<=cellSize || trueY>=13*cellSize)
                    z = 0;
                else if((trueY>=2*cellSize && trueY<=4*cellSize) || (trueY>=10*cellSize && trueY <=12*cellSize))
                    z = 1;
                else if((trueY>=4*cellSize && trueY<=6*cellSize) || (trueY>=8*cellSize && trueY<=10*cellSize))
                    z = 2;
                else z = -1;
            }
            else
                z = -1;
            return z;
        }
    }
}
