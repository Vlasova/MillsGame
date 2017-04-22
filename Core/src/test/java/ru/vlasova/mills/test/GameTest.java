package ru.vlasova.mills.test;

import org.junit.Test;

import ru.vlasova.mills.core.Game;

import static org.junit.Assert.*;

public class GameTest {
    Game game = new Game();

    @Test
    public void testMakeMove() {
        game.makeMove(0, 0, 0);
        assertEquals(game.getField()[0][0][0].getPiece().getColor(), 0);
        game.makeMove(2, 2, 2);
        assertEquals(game.getField()[2][2][2].getPiece().getColor(), 1);
    }
}
