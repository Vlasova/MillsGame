package ru.vlasova.mills.core;

public interface MillsAPI {
    void makeMove(int x, int y, int z);
    void makeMove( int fromX, int fromY, int fromZ, int toX, int toY, int toZ);
    void removePiece(int x, int y, int z);
    Cell[][][] getField();
    boolean isMill();
    boolean isAllPiecesSet();
    int getActivePlayer();
}
