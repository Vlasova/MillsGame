package ru.vlasova.mills.core;

public class Cell {
    private CellStatus status;
    private Piece piece;
    private int x;
    private int y;
    private int z;

    public Cell(int x, int y, int z) {
        this.status = CellStatus.EMPTY;
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public void setStatus(CellStatus status) {
        this.status = status;
    }

    public CellStatus getStatus() {
        return status;
    }

    public void setPiece(Piece piece) {
        this.piece = piece;
        status = CellStatus.OCCUPIED;
    }

    public Piece getPiece() throws RuntimeException {
        if(status ==CellStatus.OCCUPIED)
            return piece;
        else
            throw new RuntimeException("cell is empty");
    }

    public Piece removePiece() {
        status = CellStatus.EMPTY;
        return piece;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getZ() {
        return z;
    }
}
