package ru.vlasova.mills.core;

import java.util.ArrayList;

public class Player {
    private PlayerStatus status;
    private int color;
    private ArrayList<Piece> pieces;

    public Player(int color) {
        status = PlayerStatus.INITIAL;
        this.color = color;
        pieces = new ArrayList<>();
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
            if(p.equals(piece))
                pieces.remove(piece);
    }

    public int getNumberOfPieces() {
        return pieces.size();
    }

}
