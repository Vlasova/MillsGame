package ru.vlasova.mills.core;

public class Piece {
    int color;
    PieceStatus status;
    int x;
    int y;
    int z;

    public Piece(int color, int x, int y, int z) {
        this.color = color;
        this.status = PieceStatus.NEW;
        this.x = x;
        this.y = y;
        this.z = z;
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

    public void setCoord(int x, int y, int z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }
}
