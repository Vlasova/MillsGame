package ru.vlasova.mills.core;

public class Piece {
    private String color;
    private PieceStatus status;

    public Piece(String color) {
        this.color = color;
        this.status = PieceStatus.NEW;
    }

    public void setStatus(PieceStatus status) {
        this.status = status;
    }

    public PieceStatus getStatus() {
        return status;
    }

    public String getColor() {
        return color;
    }
}
