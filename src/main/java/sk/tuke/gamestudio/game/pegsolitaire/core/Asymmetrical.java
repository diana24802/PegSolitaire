package sk.tuke.gamestudio.game.pegsolitaire.core;

public class Asymmetrical extends Board{
    private final static String[] boardShape = {"  ...   ",
            "  ...   ",
            "  ...   ",
            "........",
            "...o....",
            "........",
            "  ...   ",
            "  ...   "};
    public Asymmetrical() {
    }
    @Override
    public String[] getBoardShape() {
        return boardShape;
    }
}
