package ru.vlasova.mills.core;

public class Piece {
    private int color;
    private PieceStatus status;

    public Piece(int color) {
        this.color = color;
        this.status = PieceStatus.NEW;
    }

    public void setStatus(PieceStatus status) {
        this.status = status;
    }

    public PieceStatus getStatus() {
        return status;
    }

    public int getColor() {
        return color;
    }
}
