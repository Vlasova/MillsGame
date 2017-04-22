package ru.vlasova.mills.core;

public class Piece {
    private int color;
    private PieceStatus status;
    private int x;
    private int y;
    private int z;

    public Piece(int color, int x, int y, int z) {
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
