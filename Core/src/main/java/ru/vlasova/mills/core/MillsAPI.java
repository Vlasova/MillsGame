package ru.vlasova.mills.core;

public interface MillsAPI {
    void makeMove(int x, int y, int z);
    void makeMove(Player player, int fromX, int fromY, int fromZ, int toX, int toY, int toZ);
    void removePiece(Player player, int x, int y, int z);
    Cell[][][] getField();
    boolean isMill(Player player);
}
