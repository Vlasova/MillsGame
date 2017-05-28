package ru.vlasova.mills.android;

import android.app.Activity;
import android.content.Context;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.PorterDuff;
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
        private Figure[] whiteFigures;
        private Figure[] blackFigures;
        private Paint paint;
        private Point point;
        private Game game;
        private int cellSize;
        boolean afterRemove;

        GameView(Context context) {
            super(context);

            Display display = getWindowManager().getDefaultDisplay();
            point = new Point();
            display.getSize(point);
            int x = (int) (point.x * 0.9);
            fieldBitmap = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.field), x, x, false);

            paint = new Paint(Paint.ANTI_ALIAS_FLAG);

            whiteFigures = new Figure[9];
            blackFigures = new Figure[9];
            int size = fieldBitmap.getHeight()/9;
            x = (point.x - fieldBitmap.getWidth())/2;
            int y = (point.y - fieldBitmap.getWidth())/2;
            for (int i=0; i<9; i++) {
                whiteFigures[i] = new Figure(0, x+i*size, y-size, size);
                blackFigures[i] = new Figure(1, x+i*size, y+fieldBitmap.getHeight(), size);
            }

            game = new Game();

            cellSize = fieldBitmap.getHeight()/14;
        }

        protected void onDraw(Canvas canvas) {
            int x = ((point.x - fieldBitmap.getWidth())/2);
            int y = ((point.y - fieldBitmap.getWidth())/2);
            canvas.drawBitmap(fieldBitmap, x, y, paint);
            Canvas fieldCanvas = new Canvas(fieldBitmap);
            drawField(fieldCanvas);
            if (afterRemove) {
                fieldCanvas.drawColor(Color.WHITE);
                fieldCanvas.drawBitmap(fieldBitmap, 0, 0, paint);
                drawField(fieldCanvas);
                afterRemove = false;
            }
            for (Figure figure : whiteFigures) {
                if (figure.getStatus() == 0)
                    figure.draw(canvas);
            }
            for (Figure figure : blackFigures) {
                if (figure.getStatus() == 0)
                    figure.draw(canvas);
            }
        }

        public boolean onTouchEvent(MotionEvent event) {
            double eventX = event.getX();
            double eventY = event.getY();
            int z = calculateZ(eventX, eventY);
            Point coordXY = calculateXY(eventX, eventY, z);
            int x = coordXY.x;
            int y = coordXY.y;
            /**if(game.isMill() && x !=-1 && y != -1 && z != -1) {
                game.removePiece(x, y, z);
                deleteFigure(x, y, z, game.getActivePlayer());
                afterRemove = true;
                invalidate();
            }*/
            if(!game.isAllPiecesSet() && x != -1 && y != -1 && z != -1) {
                game.makeMove(x, y, z);
                invalidate();
            }
            return false;
        }

        public void drawField(Canvas canvas) {
            int x, y, color;
            Cell[][][] cells = game.getField();
            for(int i=0; i<3; i++)
                for(int j=0; j<3; j++)
                    for(int k=0; k<3; k++)
                        if(cells[i][j][k].getStatus().equals(CellStatus.OCCUPIED)) {
                            x = calculateCoordAfterMove(i, k);
                            y = calculateCoordAfterMove(j, k);
                            color = cells[i][j][k].getPiece().getColor();
                            if (!game.isAllPiecesSet() && findFigure(x, y, color) == null)
                                drawFigure(canvas, x, y, color);
                        }
        }

        private Figure findFigure(int x, int y, int color) {
            if (color == 0) {
                for (Figure figure : whiteFigures) {
                    if (figure.checkCoords(x, y))
                        return figure;
                }
            }
            else {
                for (Figure figure : blackFigures) {
                    if (figure.checkCoords(x, y))
                        return figure;
                }
            }
            return null;
        }

        private void drawFigure(Canvas canvas, int x, int y, int color) {
            if (color == 0) {
                for (Figure figure : whiteFigures) {
                    if (figure.getStatus() == 0) {
                        if (drawFigure(figure, canvas, x, y))
                            break;
                    }
                }
            }
            else {
                for (Figure figure : blackFigures) {
                    if (figure.getStatus() == 0) {
                        if (drawFigure(figure, canvas, x, y))
                            break;
                    }
                }
            }
        }

        private boolean drawFigure(Figure figure, Canvas canvas, int x, int y) {
            if (figure.getStatus() == 0) {
                figure.setCoords(x, y);
                figure.setStatus(1);
                figure.draw(canvas);
                return true;
            }
            return false;
        }

        private int calculateCoordAfterMove(int coord, int z) {
            int trueCoord;
            int figureSize = whiteFigures[0].getBitmap().getHeight();
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

        public void deleteFigure(int x, int y, int z,int color) {
            x = calculateCoordAfterMove(x, z);
            y = calculateCoordAfterMove(y, z);
            if (color == 0) {
                for (Figure figure : whiteFigures) {
                    if (figure.checkCoords(x, y))
                        figure.delete();
                    break;
                }
            }
            else {
                for (Figure figure : blackFigures) {
                    if (figure.checkCoords(x, y))
                        figure.delete();
                    break;
                }
            }
        }
    }

    class Figure {
        private int x;
        private int y;
        private int color;
        private Bitmap bitmap;
        private int status;

        Figure(int color, int x, int y, int size) {
            this.color = color;
            this.x = x;
            this.y = y;
            this.status = 0;
            if (color == 0)
                this.bitmap = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.white), size, size, false);
            else
                this.bitmap = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.black), size, size, false);
        }

        public void setCoords(int x, int y) {
            this.x = x;
            this.y = y;
        }

        public Bitmap getBitmap() {
            return bitmap;
        }

        public boolean checkCoords(int x, int y) {
            return this.x == x && this.y == y;
        }

        public void draw(Canvas canvas){
            canvas.drawBitmap(bitmap, x, y, null);
        }

        public int getStatus() {
            return status;
        }

        public void setStatus(int status) {
            this.status = status;
        }

        public void delete() {
            bitmap.recycle();
            bitmap = null;
        }
    }
}
