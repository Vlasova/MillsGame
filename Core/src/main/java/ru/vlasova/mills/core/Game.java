package ru.vlasova.mills.core;

public class Game implements MillsAPI {
    private Board board;
    private Player whitePlayer;
    private Player blackPlayer;
    private Player activePlayer;

    public Game() {
        board = new Board();
        whitePlayer = new Player(0);
        blackPlayer = new Player(1);
        activePlayer = whitePlayer;
    }

    @Override
    public boolean makeMove(int x, int y, int z) throws RuntimeException{
        if (activePlayer.isNewMill()) {
            removePiece(x, y, z);
            return false;
        }
        else if (!board.isMill(activePlayer)) {
            board.setPiece(activePlayer, x, y, z);
            if (!board.isMill(activePlayer))
                changeActivePlayer();
        }
        return true;
    }

    @Override
    public void makeMove(int fromX, int fromY, int fromZ, int toX, int toY, int toZ) {
        if(activePlayer.getStatus().equals(PlayerStatus.BASIC))
            board.moveOneCoord(activePlayer.getColor(), fromX, fromY, fromZ, toX, toY, toZ);
        if(activePlayer.getStatus().equals(PlayerStatus.FINAL))
            board.moveAnyCoord(activePlayer.getColor(), fromX, fromY, fromZ, toX, toY, toZ);
        changeActivePlayer();
    }

    @Override
    public void removePiece(int x, int y, int z) {
        try {
            Piece piece = board.removePiece(activePlayer.getColor(), x, y, z);
            activePlayer.removePiece(piece);
            changeActivePlayer();
        } catch (RuntimeException e) {
            activePlayer.returnMill();
        }
    }

    @Override
    public Cell[][][] getField() {
        return board.getCells();
    }

    /**@Override
    public boolean isMill() {
        if(board.isMill(activePlayer))
            return true;
        else
            return false;
    }*/

    private void changeActivePlayer() {
        if(activePlayer == whitePlayer)
            activePlayer = blackPlayer;
        else
            activePlayer = whitePlayer;
    }

    @Override
    public boolean isAllPiecesSet() {
        if(whitePlayer.getStatus().equals(PlayerStatus.BASIC) && blackPlayer.getStatus().equals(PlayerStatus.BASIC))
            return true;
        else
            return false;
    }

    @Override
    public int getActivePlayer() {
        return activePlayer.getColor();
    }
}
