package sk.tuke.gamestudio.game.pegsolitaire.core;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;


public class BoardFactoryTest {
    @Test
    void createPredefinedBoard(){
        BoardFactory factory = new BoardFactory();
        assertTrue(factory.createPredefinedBoard('A') instanceof Asymmetrical);
        assertTrue(factory.createPredefinedBoard('D') instanceof Diamond);
        assertTrue(factory.createPredefinedBoard('E') instanceof English);
        assertTrue(factory.createPredefinedBoard('U') instanceof European);
        assertTrue(factory.createPredefinedBoard('G') instanceof German);
        assertThrows(IllegalArgumentException.class, () -> factory.createPredefinedBoard('X'));
    }
    @Test
    void createCustomBoard(){
        BoardFactory factory = new BoardFactory();
        CustomBoard board = new CustomBoard(10,10);
        assertEquals(10, board.getRowCount());
        assertEquals(10, board.getColumnCount());
        assertEquals(99, board.getPegCount());
    }
}
