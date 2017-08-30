package ru.vlasova.mills.android;

import android.app.Activity;
import android.content.Context;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
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
import ru.vlasova.mills.core.PieceStatus;

public class GameActivity extends Activity {

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(new GameView(this));
    }

    class GameView extends View {
        private Bitmap fieldBitmap;
        private Bitmap background;
        private Figure[] whiteFigures;
        private Figure[] blackFigures;
        private Paint paint;
        private Point point;
        private Game game;
        private int cellSize;
        boolean afterRemove;
        int xForMove = -1, yForMove = -1, zForMove = -1;
        private Point fieldCoords;
        private int figureSize;

        GameView(Context context) {
            super(context);
            Display display = getWindowManager().getDefaultDisplay();
            point = new Point();
            display.getSize(point);
            int x = (int) (point.x * 0.9);
            fieldBitmap = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.field), x, x, false);
            background = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.background), point.x, point.y, false);
            paint = new Paint(Paint.ANTI_ALIAS_FLAG);
            fieldCoords = new Point();
            cellSize = fieldBitmap.getHeight()/14;
            fieldCoords.x = (point.x - fieldBitmap.getWidth())/2;
            fieldCoords.y = (point.y - fieldBitmap.getWidth())/2;
            game = new Game();

            whiteFigures = new Figure[9];
            blackFigures = new Figure[9];
            int size = fieldBitmap.getHeight()/9;
            for (int i=0; i<9; i++) {
                whiteFigures[i] = new Figure(0, fieldCoords.x+i*size, fieldCoords.y-size, size);
                blackFigures[i] = new Figure(1, fieldCoords.x+i*size, fieldCoords.y+fieldBitmap.getHeight(), size);
            }
            int figureSize = whiteFigures[0].getBitmap().getHeight();
        }

        protected void onDraw(Canvas canvas) {
            canvas.drawBitmap(background, 0, 0, null);
            canvas.drawBitmap(fieldBitmap, fieldCoords.x, fieldCoords.y, paint);
            drawField(canvas);

            for (Figure figure : whiteFigures) {
                if (figure.getStatus().equals(PieceStatus.NEW))
                    figure.draw(canvas);
            }
            for (Figure figure : blackFigures) {
                if (figure.getStatus().equals(PieceStatus.NEW))
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
            try {
                if (!game.isAllPiecesSet() && x != -1 && y != -1 && z != -1) {
                    makeInitiatingMove(x, y, z);
                    return false;
                }
                else if (x != -1 && y != -1 && z != -1) {
                    movePiece(x, y, z);
                }
            } catch (RuntimeException e) {
                return false;
            }
            return false;
        }

        public void drawField(Canvas fieldCanvas) {
            int color;
            Figure figure;
            Cell[][][] cells = game.getField();
            for(int i=0; i<3; i++)
                for(int j=0; j<3; j++)
                    for(int k=0; k<3; k++)
                        if(cells[i][j][k].getStatus().equals(CellStatus.OCCUPIED)) {
                            color = cells[i][j][k].getPiece().getColor();
                            figure = findFigure(i, j, k, color);
                            if (figure != null && !isAllInitialized()) {

                                figure.draw(fieldCanvas);
                            }
                            if (figure == null && !isAllInitialized())
                                initializeFigure(fieldCanvas, i, j, k, color);
                            else if (isAllInitialized() && figure != null){
                                figure.draw(fieldCanvas);
                            }
                        }
        }

        private void initializeFigure(Canvas canvas, int x, int y, int z, int color) {
            if (color == 0) {
                for (Figure figure : whiteFigures) {
                    if (figure.getStatus().equals(PieceStatus.NEW)) {
                        initializeFigure(canvas, x, y, z, figure);
                        break;
                    }
                }
            }
            else {
                for (Figure figure : blackFigures) {
                    if (figure.getStatus().equals(PieceStatus.NEW)) {
                        initializeFigure(canvas, x, y, z, figure);
                        break;
                    }
                }
            }
        }

        private void initializeFigure(Canvas canvas, int x, int y, int z, Figure figure) {
            figure.setCoords(x, y, z);
            int drawX = calculateCoordAfterMove(x, z)+fieldCoords.x-cellSize/2-10;
            int drawY = calculateCoordAfterMove(y, z)+fieldCoords.y-cellSize/2-10;
            figure.setDrawCoords(drawX, drawY);
            figure.draw(canvas);
            figure.setStatus(PieceStatus.INGAME);
        }

        private boolean isAllInitialized() {
            for (Figure figure : whiteFigures)
                if (figure.getStatus().equals(PieceStatus.NEW))
                    return false;
            for (Figure figure : blackFigures)
                if (figure.getStatus().equals(PieceStatus.NEW))
                    return false;
            return true;
        }

        private Figure findFigure(int x, int y, int z, int color) {
            if (color == 0) {
                for (Figure figure : whiteFigures) {
                    if (figure.checkCoords(x, y, z))
                        return figure;
                }
            }
            else {
                for (Figure figure : blackFigures) {
                    if (figure.checkCoords(x, y, z))
                        return figure;
                }
            }
            return null;
        }

       /** private void drawFigure(Canvas canvas, int x, int y, int color) {
            if (color == 0) {
                for (Figure figure : whiteFigures) {
                    if (figure.getStatus().equals(PieceStatus.NEW)) {
                        if (drawFigure(figure, canvas, x, y))
                            break;
                    }
                }
            }
            else {
                for (Figure figure : blackFigures) {
                    if (figure.getStatus().equals(PieceStatus.NEW)) {
                        if (drawFigure(figure, canvas, x, y))
                            break;
                    }
                }
            }
        }

        private boolean drawFigure(Figure figure, Canvas canvas, int x, int y) {
            if (figure.getStatus().equals(PieceStatus.NEW)) {
                figure.setCoords(x, y);
                figure.setStatus(PieceStatus.INGAME);
                figure.draw(canvas);
                return true;
            }
            return false;
        }*/

        private int calculateCoordAfterMove(int coord, int z) {
            int trueCoord;
            if(coord==0)
                trueCoord = cellSize*(2*z+1)-figureSize;
            else if(coord==1)
                trueCoord = cellSize*7-figureSize;
            else
                trueCoord = cellSize*(13-2*z)-figureSize;
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

        public void movePiece(int x, int y, int z) {
            Figure figure;
            if (xForMove == -1) {
                figure = findFigure(x, y, z, game.getActivePlayer());
                if (figure != null) {
                    xForMove = x;
                    yForMove = y;
                    zForMove = z;
                }
            }
            else {
                try {
                    figure = findFigure(xForMove, yForMove, zForMove, game.getActivePlayer());
                    figure.setCoords(x, y, z);

                    figure.setDrawCoords(calculateCoordAfterMove(x, z)+fieldCoords.x-cellSize/2-10,
                            calculateCoordAfterMove(y, z)+fieldCoords.y-cellSize/2-10);
                    game.makeMove(xForMove, yForMove, zForMove, x, y, z);
                    xForMove = -1;
                    yForMove = -1;
                    zForMove = -1;
                    invalidate();
                } catch (RuntimeException e) {}
            }
        }

        /**public void deleteFigure(int x, int y, int z,int color) {
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
        }*/

        private void makeInitiatingMove(int x, int y, int z) throws RuntimeException{
            try {
                if (game.makeMove(x, y, z)) {
                    invalidate();
                }
                else {
                    Figure figure = findFigure(x, y, z, game.getActivePlayer());
                    figure.destroy();
                    invalidate();
                }
            } catch (RuntimeException e) {
                throw e;
            }
        }
    }

    class Figure {
        private int x;
        private int y;
        private int z;
        private int drawX;
        private int drawY;
        private int color;
        private int size;
        private Bitmap bitmap;
        private PieceStatus status;

        Figure(int color, int drawX, int drawY, int size) {
            this.color = color;
            this.drawX = drawX;
            this.drawY = drawY;
            this.x = -1;
            this.y = -1;
            this.z = -1;
            this.size = size;
            this.status = PieceStatus.NEW;
            if (color == 0)
                this.bitmap = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.white), size, size, false);
            else
                this.bitmap = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.black), size, size, false);
        }

        public void setCoords(int x, int y, int z) {
            this.x = x;
            this.y = y;
            this.z = z;
        }

        public void setDrawCoords(int x, int y) {
            this.drawX = x;
            this.drawY = y;
        }

        public Bitmap getBitmap() {
            return bitmap;
        }

        public boolean checkCoords(int x, int y, int z) {
            return this.x == x && this.y == y && this.z == z;
        }

        public void draw(Canvas canvas){
            canvas.drawBitmap(bitmap, drawX, drawY, null);
        }

        public PieceStatus getStatus() {
            return status;
        }

        public void setStatus(PieceStatus status) {
            this.status = status;
        }

        public void moveBitmap(Canvas canvas, int x, int y) {
            Matrix matrix = new Matrix();
            matrix.preTranslate(x-drawX, y-drawY);
            canvas.drawBitmap(bitmap, matrix, null);
        }

        public void destroy() {
            status = PieceStatus.DESTROYED;
            x = -1;
            y = -1;
            z = -1;
        }

    }
}
