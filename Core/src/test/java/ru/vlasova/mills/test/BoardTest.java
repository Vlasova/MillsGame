package ru.vlasova.mills.test;

import org.junit.Test;

import ru.vlasova.mills.core.Board;
import ru.vlasova.mills.core.Cell;
import ru.vlasova.mills.core.CellStatus;
import ru.vlasova.mills.core.Player;

import static org.junit.Assert.*;

public class BoardTest{

    Board board = new Board();
    Player player = new Player(0);

    @Test
    public void testSetPiece() {
        board.setPiece(player, 0, 0, 0);
        assertEquals(CellStatus.OCCUPIED, board.getCells()[0][0][0].getStatus());
        assertEquals(CellStatus.NOTAVAILABLE, board.getCells()[1][1][0].getStatus());
    }

    @Test
    public void testMoveOneCoord() {
        board.setPiece(player, 2, 1, 0 );
        board.moveOneCoord(0, 2, 1, 0, 1, 1, 0);
        assertEquals(CellStatus.OCCUPIED, board.getCells()[1][1][0].getStatus());
        assertEquals(0, board.getCells()[1][1][0].getPiece().getColor());
        assertEquals(CellStatus.EMPTY, board.getCells()[2][1][0].getStatus());
    }

    @Test
    public void testIsMill() {
        board.setPiece(player, 0, 0, 0);
        board.setPiece(player, 1, 0, 0);
        assertFalse(board.isMill(player));
        board.setPiece(player, 2, 0, 0);
        assertTrue(board.isMill(player));
        assertFalse(board.isMill(player));
        board.setPiece(player, 0, 1, 0);
        board.setPiece(player, 0, 2, 0);
        assertTrue(board.isMill(player));
        assertFalse(board.isMill(player));
        board.setPiece(player, 0, 1, 1);
        board.setPiece(player, 0, 1, 2);
        assertTrue(board.isMill(player));
        assertFalse(board.isMill(player));
    }
}
