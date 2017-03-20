package ru.vlasova.mills.core;

public class Cell {
    private CellStatus status;
    private Piece piece;

    public Cell() {
        this.status = CellStatus.EMPTY;
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

    public Piece getPiece() {
        status = CellStatus.EMPTY;
        return piece;
    }
}
