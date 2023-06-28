package sk.tuke.gamestudio.game.pegsolitaire.core;

import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import static org.junit.jupiter.api.Assertions.*;

class BoardTest {
    @Test
    void customBoardNegativeParams(){
        assertThrows(IllegalArgumentException.class, () -> new CustomBoard(-1,1));
        assertThrows(IllegalArgumentException.class, () -> new CustomBoard(1,-1));
        assertThrows(IllegalArgumentException.class, () -> new CustomBoard(-1,-1));
    }
    @Test
    void customBoardTooSmallAndTooBigParams(){
        assertThrows(IllegalArgumentException.class, () -> new CustomBoard(5,2));
        assertThrows(IllegalArgumentException.class, () -> new CustomBoard(1,30));
    }
    @Test
    void getPegCountFromCustomBoard(){
        Board board1 = new CustomBoard(10,10);
        assertSame(99, board1.getPegCount());
        Board board2 = new CustomBoard(4,4);
        assertSame(15, board2.getPegCount());
    }
    @Test
    void makeMove(){
        Board board1 = new CustomBoard(5,5);
        board1.makeMove(0,2,2,2);
        assertSame(TileType.HOLE, board1.getPlayableTile(0,2).getType());
        assertSame(TileType.HOLE, board1.getPlayableTile(1,2).getType());
        assertSame(TileType.PEG, board1.getPlayableTile(2,2).getType());

        board1.makeMove(0,0,0,2);
        assertSame(TileType.HOLE, board1.getPlayableTile(0,0).getType());
        assertSame(TileType.HOLE, board1.getPlayableTile(0,1).getType());
        assertSame(TileType.PEG, board1.getPlayableTile(0,2).getType());

        board1.makeMove(0,3,0,1);
        assertSame(TileType.HOLE, board1.getPlayableTile(0,3).getType());
        assertSame(TileType.HOLE, board1.getPlayableTile(0,2).getType());
        assertSame(TileType.PEG, board1.getPlayableTile(0,1).getType());

        board1.makeMove(3,2,1,2);
        assertSame(TileType.HOLE, board1.getPlayableTile(3,2).getType());
        assertSame(TileType.HOLE, board1.getPlayableTile(2,2).getType());
        assertSame(TileType.PEG, board1.getPlayableTile(1,2).getType());

        assertThrows(ArrayIndexOutOfBoundsException.class, () -> board1.makeMove(-1,0,6,0));

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        System.setOut(new PrintStream(baos));
        board1.makeMove(0,0,1,1);
        String output = baos.toString();
        assertTrue(output.contains("Invalid coordinates!"));
    }
    @Test
    void testIsSolvedWithMultiplePegs(){
        Board board = new CustomBoard(5,5);
        board.makeMove(0,2,2,2);
        assertFalse(board.isSolved());
        assertSame(GameState.PLAYING, board.getState());
    }
    @Test
    void testIsSolvedWhenAllPegsAreRemoved(){
        Board board = new CustomBoard(4,4);
        board.getPlayableTile(0,1).setType(TileType.HOLE);
        board.getPlayableTile(1,1).setType(TileType.PEG);
        board.makeMove(2,1,0,1);
        board.makeMove(1,3,1,1);
        board.makeMove(0,1,2,1);
        board.makeMove(3,1,1,1);
        board.makeMove(0,3,0,1);
        board.makeMove(2,3,2,1);
        board.makeMove(3,3,3,1);
        board.makeMove(3,0,3,2);
        board.makeMove(2,0,2,2);
        board.makeMove(3,2,1,2);
        board.makeMove(0,0,2,0);
        board.makeMove(0,1,2,1);
        board.makeMove(2,0,2,2);
        assertFalse(board.isSolved());
        assertEquals(2, board.getPegCount());
        board.makeMove(2,2,0,2);
        assertTrue(board.isSolved());
        assertEquals(1, board.getPegCount());
    }

    @Test
    void isStaleMate(){
        Board board = new CustomBoard(4,4);
        board.getPlayableTile(0,0).setType(TileType.HOLE);
        board.getPlayableTile(1,1).setType(TileType.PEG);
        board.makeMove(0,2,0,0);
        board.makeMove(2,1,0,1);
        board.makeMove(0,0,0,2);
        board.makeMove(2,0,0,0);
        board.makeMove(0,3,0,1);
        board.makeMove(0,0,0,2);
        board.makeMove(1,3,1,1);
        board.makeMove(3,2,1,2);
        board.makeMove(1,1,1,3);
        board.makeMove(2,3,0,3);
        board.makeMove(0,3,0,1);
        board.makeMove(3,0,3,2);
        assertFalse(board.isStalemate());
        board.makeMove(3,3,3,1);
        assertTrue(board.isStalemate());
    }
}