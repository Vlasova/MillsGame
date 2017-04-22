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
    public void makeMove(int x, int y, int z) throws RuntimeException{
        board.setPiece(activePlayer, x, y, z);
        changeActivePlayer();
    }

    @Override
    public void makeMove(Player player, int fromX, int fromY, int fromZ, int toX, int toY, int toZ) {

    }

    @Override
    public void removePiece(Player player, int x, int y, int z) {

    }

    @Override
    public Cell[][][] getField() {
        return board.getCells();
    }

    @Override
    public boolean isMill(Player player) {
        if(board.isMill(player.getColor()))
            return true;
        else
            return false;
    }

    private void changeActivePlayer() {
        if(activePlayer == whitePlayer)
            activePlayer = blackPlayer;
        else
            activePlayer = whitePlayer;
    }
}
