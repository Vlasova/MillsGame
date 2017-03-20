package ru.vlasova.mills.core;

public class Board {
    private Cell[][][] cells;

    public Board() {
        cells = new Cell[3][3][3];
        for(int i=0; i<3; i++)
            cells[1][1][i].setStatus(CellStatus.NOTAVAILABLE);
    }

    public Cell[][][] getCells() {
        return cells;
    }

    public void setPiece(int color, int x, int y, int z) {
        cells[x][y][z].setPiece(new Piece(color));
    }

    public void moveByX(int fromX, int toX, int y, int z) {
        cells[toX][y][z].setPiece(cells[fromX][y][z].getPiece());
    }

    public void moveByY(int x, int fromY, int toY, int z) {
        cells[x][toY][z].setPiece(cells[x][fromY][z].getPiece());
    }

    public void moveByZ(int x, int y, int fromZ, int toZ) {
        cells[x][y][toZ].setPiece(cells[x][y][fromZ].getPiece());
    }

    public void moveByCoords(int fromX, int fromY, int fromZ, int toX, int toY, int toZ) {
        cells[toX][toY][toZ].setPiece(cells[fromX][fromY][fromZ].getPiece());
    }
}
