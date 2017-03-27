package ru.vlasova.mills.test;

import org.junit.Test;
import ru.vlasova.mills.core.Board;
import ru.vlasova.mills.core.CellStatus;
import ru.vlasova.mills.core.Player;
import ru.vlasova.mills.core.PlayerStatus;

import static org.junit.Assert.*;

public class PlayerTest {
    Board board = new Board();
    Player player1= new Player(0, board);
    Player player2 = new Player(1, board);

    @Test
    public void testSetPiece() {
        player1.setPiece(0, 0, 0);
        player2.setPiece(0, 1, 1);
        assertEquals(1, board.getCells()[0][1][1].getPiece().getColor());
        assertEquals(CellStatus.OCCUPIED, board.getCells()[0][0][0].getStatus());
    }

    @Test
    public void testMakeMove() {
        player1.setPiece(0, 0, 0);
        player2.setPiece(0, 1, 1);
        player1.setStatus(PlayerStatus.BASIC);
        player2.setStatus(PlayerStatus.FINAL);
        player1.makeMove(0, 0, 0, 1, 0, 0);
        player2.makeMove(0, 1, 1, 2, 0,0);
        player1.makeMove(1, 0, 0, 1, 0, 1);
        assertEquals(0, board.getCells()[1][0][1].getPiece().getColor());
    }

    @Test
    public void testRemovePiece() {
        player1.setPiece(0, 0, 0);
        player2.setPiece(0, 1, 1);
        player1.removePiece(0, 1, 1);
        player2.removePiece(0, 0, 0);
        assertEquals(CellStatus.EMPTY, board.getCells()[0][0][0].getStatus());
    }
}
