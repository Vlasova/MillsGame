package ru.vlasova.mills.core;

import java.util.ArrayList;

public class Player {
    private PlayerStatus status;
    private int color;
    private ArrayList<Piece> pieces;
    private ArrayList<Cell[]> mills;
    private int countOfUsedMills = 0;
    private int countOfDestroyed = 0;

    public Player(int color) {
        status = PlayerStatus.INITIAL;
        this.color = color;
        pieces = new ArrayList<>();
        mills = new ArrayList<>();
    }

    public void addMill(Cell[] cells) {
        mills.add(cells);
    }

    public void setStatus(PlayerStatus status) {
        this.status = status;
    }

    public void setPiece(Piece piece) throws RuntimeException{
        if(!status.equals(PlayerStatus.INITIAL))
            throw new RuntimeException("All pieces are placed");
        pieces.add(piece);
        if(pieces.size()==9)
            status = PlayerStatus.BASIC;
    }

    public int getColor() {
        return color;
    }

    public PlayerStatus getStatus() {
        return status;
    }

    public void removePiece(Piece piece) {
        for(Piece p : pieces)
            if(p.getX() == piece.getX() && p.getY() == piece.getY() &&
                    piece.getZ() == p.getZ() && p.getColor() == piece.getColor() && p.getStatus() == PieceStatus.NEW) {
                p.setStatus(PieceStatus.DESTROYED);
                countOfDestroyed++;
            }
        if (countOfDestroyed == 6)
            status = PlayerStatus.FINAL;
        if (countOfDestroyed == 7)
            status = PlayerStatus.LOSER;
    }

    public void returnMill() {
        countOfUsedMills--;
    }

    public ArrayList<Cell[]> getMills() {
        return mills;
    }

    public boolean isNewMill() {
        if (mills.size()>countOfUsedMills) {
            countOfUsedMills++;
            return true;
        }
        else
            return false;
    }
}
