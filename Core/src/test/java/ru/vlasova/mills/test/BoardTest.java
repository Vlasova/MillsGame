package ru.vlasova.mills.test;

import org.junit.Test;
import ru.vlasova.mills.core.Board;
import ru.vlasova.mills.core.CellStatus;

import static org.junit.Assert.*;

public class BoardTest{

    Board board = new Board();

    @Test
    public void testSetPiece() {
        board.setPiece(1, 0, 0, 0);
        assertEquals(CellStatus.OCCUPIED, board.getCells()[0][0][0].getStatus());
        assertEquals(CellStatus.NOTAVAILABLE, board.getCells()[1][1][0].getStatus());
    }

    @Test
    public void testMoveOneCoord() {
        board.setPiece(0, 2, 1, 0 );
        board.moveOneCoord(2, 1, 0, 1, 1, 0);
        assertEquals(CellStatus.OCCUPIED, board.getCells()[1][1][0].getStatus());
        assertEquals(0, board.getCells()[1][1][0].getPiece().getColor());
        assertEquals(CellStatus.EMPTY, board.getCells()[2][1][0].getStatus());
    }

    @Test
    public void testIsMill() {
        board.setPiece(0, 0, 0, 0);
        board.setPiece(0, 1, 2, 2);
        board.setPiece(0, 2, 2, 2);
        board.setPiece(1, 2, 1, 0);
        board.setPiece(1, 2, 1, 1);
        board.setPiece(1, 2, 1, 2);
        assertEquals(true, board.isMill(1));
        assertEquals(false, board.isMill(0));
        board.setPiece(0, 0, 0 ,1);
        board.setPiece(0, 0, 1, 1);
        board.setPiece(0, 0, 2, 1);
        assertEquals(true, board.isMill(0));
    }
}
