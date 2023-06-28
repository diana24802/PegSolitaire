package sk.tuke.gamestudio.game.pegsolitaire.core;

public class PlayableTile extends Tile{
    private TileType type;
    public PlayableTile(TileType type) {
        this.type = type;
    }
    public TileType getType() {
        return type;
    }
    public void setType(TileType type) {
        this.type = type;
    }
}
