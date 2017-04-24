package ru.vlasova.mills.test;

import org.junit.Test;
import ru.vlasova.mills.core.Board;
import ru.vlasova.mills.core.CellStatus;
import ru.vlasova.mills.core.Piece;
import ru.vlasova.mills.core.Player;
import ru.vlasova.mills.core.PlayerStatus;

import static org.junit.Assert.*;

public class PlayerTest {
    Board board = new Board();
    Player player1= new Player(0);
    Player player2 = new Player(1);
    Piece whitePiece = new Piece(0, 0, 0, 0);
    Piece blackPiece = new Piece(1, 0, 0, 1);

    @Test
    public void testSetPiece() {
        player1.setPiece(whitePiece);
        player2.setPiece(blackPiece);
        assertEquals(1, board.getCells()[0][1][1].getPiece().getColor());
        assertEquals(CellStatus.OCCUPIED, board.getCells()[0][0][0].getStatus());
    }

    @Test
    public void testMakeMove() {
        player1.setPiece(whitePiece);
        player2.setPiece(blackPiece);
        player1.setStatus(PlayerStatus.BASIC);
        player2.setStatus(PlayerStatus.FINAL);
        //player1.makeMove(0, 0, 0, 1, 0, 0);
        //player2.makeMove(0, 1, 1, 2, 0,0);
        //player1.makeMove(1, 0, 0, 1, 0, 1);
        //assertEquals(0, board.getCells()[1][0][1].getPiece().getColor());
    }
}
