package ru.vlasova.mills.core;

public interface MillsAPI {
    boolean makeMove(int x, int y, int z);
    boolean makeMove( int fromX, int fromY, int fromZ, int toX, int toY, int toZ);
    void removePiece(int x, int y, int z);
    Cell[][][] getField();
    boolean isMill();
    boolean isAllPiecesSet();
    Player getActivePlayer();
}
