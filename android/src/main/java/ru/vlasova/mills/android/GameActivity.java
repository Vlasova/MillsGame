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
import android.view.WindowManager;

import java.util.ArrayList;

import ru.vlasova.mills.core.Cell;
import ru.vlasova.mills.core.CellStatus;
import ru.vlasova.mills.core.Game;

public class GameActivity extends Activity {

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
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

            x = (int) fieldBitmap.getHeight()/10;
            whiteFigure = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.white), x, x, false);
            blackFigure = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.black), x, x, false);

            game = new Game();

            cellSize = fieldBitmap.getHeight()/14;
        }

        protected void onDraw(Canvas canvas) {
            int x = ((point.x - fieldBitmap.getWidth())/2);
            int y = ((point.y - fieldBitmap.getWidth())/2);
            canvas.drawBitmap(fieldBitmap, x, y, paint);
            Canvas fieldCanvas = new Canvas(fieldBitmap);
            drawField(fieldCanvas);
        }

        public boolean onTouchEvent(MotionEvent event) {
            double eventX = event.getX();
            double eventY = event.getY();
            int z = calculateZ(eventX, eventY);
            Point coordXY = calculateXY(eventX, eventY, z);
            int x = coordXY.x;
            int y = coordXY.y;
            if(game.isMill()) {
                game.removePiece(x, y, z);
                invalidate();
            }
            else if(!game.isAllPiecesSet() && x != -1 && y != -1 && z != -1) {
                game.makeMove(x, y, z);
                invalidate();
            }
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

        private int calculateCoordBeforeMove(double coord, int z) {
            int trueCoord;
            if(coord>=(cellSize/2) && coord<=cellSize*(2*z+2))
                trueCoord = 0;
            else if(coord>=6*cellSize && coord<=8*cellSize)
                trueCoord = 1;
            else if(coord>=(cellSize*(13-2*z)-cellSize/2) && coord<=14*cellSize-cellSize/3)
                trueCoord = 2;
            else trueCoord = -1;
            return trueCoord;

        }

        private Point calculateXY(double x, double y, int z) {
            double trueX = x - (point.x - fieldBitmap.getWidth())/2;
            double trueY = y - (point.y - fieldBitmap.getHeight())/2;
            return new Point(calculateCoordBeforeMove(trueX, z), calculateCoordBeforeMove(trueY, z));
        }

        private int calculateZ(double x, double y) {
            double trueX = x - (point.x - fieldBitmap.getWidth())/2;
            double trueY = y - (point.y - fieldBitmap.getHeight())/2;
            int z;
            if(trueX<=1.5*cellSize || trueX>=12.5*cellSize)
                z = 0;
            else if((trueX>=2*cellSize && trueX<=4*cellSize && trueY>=2*cellSize && trueY<=11*cellSize)
                    || (trueX>=10*cellSize && trueX<=12*cellSize && trueY>=2*cellSize && trueY<=11*cellSize))
                z = 1;
            else if((trueX>=4*cellSize && trueX<=6*cellSize && trueY>=5*cellSize && trueY<=9*cellSize)
                    || (trueX>=8*cellSize && trueX<=10*cellSize && trueY>=5*cellSize && trueY<=9*cellSize))
                z = 2;
            else if(trueX>6*cellSize && trueX<8*cellSize) {
                if(trueY<=1.5*cellSize || trueY>=12.5*cellSize)
                    z = 0;
                else if((trueY>=2*cellSize && trueY<=4*cellSize && trueX>=2*cellSize && trueX<=11*cellSize)
                        || (trueY>=10*cellSize && trueY <=12*cellSize && trueY>=2*cellSize && trueX<=11*cellSize))
                    z = 1;
                else if((trueY>=4*cellSize && trueY<=6*cellSize && trueX>=5*cellSize && trueX<=9*cellSize)
                        || (trueY>=8*cellSize && trueY<=10*cellSize && trueX>=5*cellSize && trueX<=9*cellSize))
                    z = 2;
                else z = -1;
            }
            else
                z = -1;
            return z;
        }
    }
}
