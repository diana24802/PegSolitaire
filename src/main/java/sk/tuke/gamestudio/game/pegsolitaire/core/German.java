package sk.tuke.gamestudio.game.pegsolitaire.core;

public class German extends Board{
    private final static String[] boardShape = {"   ...   ",
                                                "   ...   ",
                                                "   ...   ",
                                                ".........",
                                                "....o....",
                                                ".........",
                                                "   ...   ",
                                                "   ...   ",
                                                "   ...   "};
    public German() {
    }
    @Override
    public String[] getBoardShape() {
        return boardShape;
    }
}
