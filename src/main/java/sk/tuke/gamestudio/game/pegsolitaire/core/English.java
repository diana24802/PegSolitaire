package sk.tuke.gamestudio.game.pegsolitaire.core;

public class English extends Board{
    private final static String[] boardShape = {"  ...  ",
            "  ...  ",
            ".......",
            "...o...",
            ".......",
            "  ...  ",
            "  ...  "};
    public English() {
    }
    @Override
    public String[] getBoardShape() {
        return boardShape;
    }
}
