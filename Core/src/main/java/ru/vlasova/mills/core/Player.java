package ru.vlasova.mills.core;

public class Player {
    private PlayerStatus status;
    private int color;
    private Board board;

    public Player(int color, Board board) {
        status = PlayerStatus.INITIAL;
        this.color = color;
        this.board = board;
    }

    public void setStatus(PlayerStatus status) {
        this.status = status;
    }

    public void setPiece(int x, int y, int z) {
        board.setPiece(color, x, y, z);
    }

    public void makeMove(int fromX, int fromY, int fromZ, int toX, int toY, int toZ) throws RuntimeException{
        try {
            if (status == PlayerStatus.BASIC)
                board.moveOneCoord(color, fromX, fromY, fromZ, toX, toY, toZ);
            if (status == PlayerStatus.FINAL)
                board.moveAnyCoord(color, fromX, fromY, fromZ, toX, toY, toZ);
        } catch (RuntimeException e) {
            throw e;
        }
    }

    public void removePiece(int x, int y, int z) throws RuntimeException{
        try {
            board.removePiece(color, x, y, z);
        } catch ( RuntimeException e) {
            throw e;
        }
    }
}
