package sk.tuke.gamestudio.game.pegsolitaire.core;

public class BoardFactory {
    public BoardFactory() {
    }
    public Board createPredefinedBoard(char name){
        name = Character.toUpperCase(name);
        return switch (name) {
            case 'A' -> new Asymmetrical();
            case 'D' -> new Diamond();
            case 'E' -> new English();
            case 'U' -> new European();
            case 'G' -> new German();
            default -> throw new IllegalArgumentException("Invalid board name " + name);
        };
    }
    public Board createCustomBoard(int width, int height){
        return new CustomBoard(width, height);
    }
}