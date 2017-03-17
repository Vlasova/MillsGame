package ru.vlasova.mills.test;

import org.junit.Test;
import static org.junit.Assert.*;
import ru.vlasova.mills.core.Piece;
import ru.vlasova.mills.core.PieceStatus;

public class PieceTest {

    Piece piece = new Piece("white");

    @Test
    public void testGetStatus() {
        assertEquals(PieceStatus.NEW, piece.getStatus());
    }

    @Test
    public void testSetStatus() {
        piece.setStatus(PieceStatus.DESTROYED);
        assertEquals(PieceStatus.DESTROYED, piece.getStatus());
    }

    @Test
    public void testGetColor() {
        assertEquals("white", piece.getColor());
    }
}
