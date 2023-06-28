package sk.tuke.gamestudio.game.pegsolitaire.core;

public class Diamond extends Board{
    private final static String[] boardShape = {"    .    ",
            "   ...   ",
            "  .....  ",
            " ....... ",
            "....o....",
            " ....... ",
            "  .....  ",
            "   ...   ",
            "    .    "};
    public Diamond() {
    }
    @Override
    public String[] getBoardShape() {
        return boardShape;
    }
}
