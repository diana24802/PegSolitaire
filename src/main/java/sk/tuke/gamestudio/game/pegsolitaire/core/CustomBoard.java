package sk.tuke.gamestudio.game.pegsolitaire.core;

public class CustomBoard extends Board{
    public CustomBoard(int rowCount, int columnCount) {
        super(rowCount, columnCount);
    }

    @Override
    public String[] getBoardShape() {
        return null;
    }
}