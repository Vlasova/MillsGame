package ru.vlasova.mills.core;


public class Board {
    private Cell[][][] cells;

    public Board() {
        cells = new Cell[3][3][3];
        for(int i=0; i<3; i++) {
            for (int j = 0; j < 3; j++)
                for (int k = 0; k < 3; k++)
                    cells[i][j][k] = new Cell();
        }
        setNotAvailableCells();
    }

    private void setNotAvailableCells() {
        for(int i=0; i<3; i++)
            cells[1][1][i].setStatus(CellStatus.NOTAVAILABLE);
    }

    public Cell[][][] getCells() {
        return cells;
    }

    public void setPiece(int color, int x, int y, int z) {
        cells[x][y][z].setPiece(new Piece(color));
    }

    public void moveOneCoord(int fromX, int fromY, int fromZ, int toX, int toY, int toZ) throws RuntimeException{
        if(Math.abs(fromX - toX) > 1 || Math.abs(fromY - toY) > 1 || Math.abs(fromZ - toZ) > 1)
            throw new RuntimeException("the move is impossible");
        if(cells[toX][toY][toZ].getStatus().equals(CellStatus.OCCUPIED))
            throw new RuntimeException("the cell is occupied");
        cells[toX][toY][toZ].setPiece(cells[fromX][fromY][fromZ].removePiece());
    }

    public void moveAnyCoord(int fromX, int fromY, int fromZ, int toX, int toY, int toZ) throws RuntimeException{
        if(cells[toX][toY][toZ].getStatus().equals(CellStatus.EMPTY))
            cells[toX][toY][toZ].setPiece(cells[fromX][fromY][fromZ].getPiece());
        else
            throw new RuntimeException(("the cell is occupied"));
    }

    public boolean isMill(int color) {
        int countX = 0;
        int countY = 0;
        int countHorizontalZ = 0;
        int countVerticalZ = 0;
        for(int i=0; i<2; i++) {
            for(int j=0; j<3; j++) {
                for(int k=0; k<3; k++) {
                    if((cells[k][i*2][j].getStatus().equals(CellStatus.OCCUPIED)) && (cells[k][i*2][j].getPiece().getColor() == color))
                        countX++;
                    if((cells[i*2][k][j].getStatus().equals(CellStatus.OCCUPIED)) && (cells[i*2][k][j].getPiece().getColor() == color))
                        countY++;
                    if((cells[i*2][1][k].getStatus().equals(CellStatus.OCCUPIED)) && (cells[i*2][1][k].getPiece().getColor() == color))
                        countHorizontalZ++;
                    if((cells[1][i*2][k].getStatus().equals(CellStatus.OCCUPIED)) && (cells[1][i*2][k].getPiece().getColor() == color))
                        countVerticalZ++;
                }
                if(countX == 3 || countY == 3 || countHorizontalZ == 3 || countVerticalZ == 3)
                    return  true;
                countX = 0;
                countY = 0;
                countHorizontalZ = 0;
                countVerticalZ = 0;
            }
        }
        return false;
    }

    public void removePiece(int x, int y, int z) {
        cells[x][y][z].removePiece();
    }
}
