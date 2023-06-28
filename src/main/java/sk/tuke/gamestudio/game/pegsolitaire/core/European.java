package sk.tuke.gamestudio.game.pegsolitaire.core;

public class European extends Board{
    private final static String[] boardShape = {"  ...  ",
            " ..... ",
            "...o...",
            ".......",
            ".......",
            " ..... ",
            "  ...  "};
    public European() {
    }
    @Override
    public String[] getBoardShape() {
        return boardShape;
    }
}
