package ru.vlasova.mills.test;

import org.junit.Test;

import ru.vlasova.mills.core.CellStatus;
import ru.vlasova.mills.core.Game;

import static org.junit.Assert.*;

public class GameTest {

    @Test
    public void testMakeMove() {
        Game game = new Game();
        game.makeMove(0, 0, 0);
        assertEquals(game.getField()[0][0][0].getPiece().getColor(), 0);
        game.makeMove(2, 2, 2);
        assertEquals(game.getField()[2][2][2].getPiece().getColor(), 1);
    }

    @Test
    public void testIsMill() {
        Game game = new Game();
        game.makeMove(0, 0, 0);
        game.makeMove(2,2, 2);
        game.makeMove(1, 0, 0);
        game.makeMove(0,0, 1);
        game.makeMove(2, 0, 0);
        assertEquals(true, game.isMill());
    }

    @Test
    public void testRemovePiece() {
        Game game = new Game();
        game.makeMove(0, 0, 0);
        game.makeMove(2,2, 2);
        game.makeMove(1, 0, 0);
        game.makeMove(0,0, 1);
        game.makeMove(2, 0, 0);
        game.removePiece(2, 2, 2);
        assertEquals(game.getField()[2][2][2].getStatus(), CellStatus.EMPTY);
    }

    @Test
    public void testIsAllPiecesSet() {
        Game game = new Game();
        game.makeMove(0, 0, 0);
        game.makeMove(0, 1, 0);
        game.makeMove(0, 2, 0);
        game.makeMove(1, 0, 0);
        game.makeMove(2, 0, 0);
        assertFalse(game.isAllPiecesSet());
        game.makeMove(0, 0, 1);
        game.makeMove(1, 0, 1);
        game.makeMove(2, 0, 1);
        game.makeMove(0, 0, 2);
        game.makeMove(1, 0, 2);
        game.makeMove(2, 0, 2);
        game.makeMove(2, 1, 0);
        game.makeMove(2, 2, 0);
        game.makeMove(1, 2, 0);
        game.makeMove(0, 2, 1);
        game.makeMove(1, 2, 1);
        game.makeMove(2, 2, 1);
        assertFalse(game.isAllPiecesSet());
        game.makeMove(2, 1, 1);
        assertTrue(game.isAllPiecesSet());
    }
}
