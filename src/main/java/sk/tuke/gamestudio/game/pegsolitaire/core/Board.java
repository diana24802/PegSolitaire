package sk.tuke.gamestudio.game.pegsolitaire.core;

import static java.lang.Math.abs;

public abstract class Board {
    private int rowCount;
    private int columnCount;
    private Tile[][] tiles;
    private GameState state = GameState.PLAYING;
    private int pegCount;
    private long startTime;
    private long elapsedTime;
    public Board(int rowCount, int columnCount) {
        if (rowCount <= 0 || columnCount <= 0)
            throw new IllegalArgumentException("Invalid board dimensions.");
        else if (rowCount > 26 || columnCount > 26)
            throw new IllegalArgumentException("Selected board Dimensions are too big!");
        else if (rowCount < 4 || columnCount < 4)
            throw new IllegalArgumentException("Selected board Dimensions are too small!");
        this.rowCount = rowCount;
        this.columnCount = columnCount;
        this.tiles = new Tile[rowCount][columnCount];
        this.pegCount = rowCount * columnCount - 1;
        this.startTime = System.currentTimeMillis();
        generateBoard();
    }

    public Board() {
        this.startTime = System.currentTimeMillis();
        generateSpecificBoard();
    }

    private void generateSpecificBoard() {
        String[] boardShape = getBoardShape();
        this.rowCount = boardShape.length;
        this.columnCount = getBoardShape()[0].length();
        this.tiles = new Tile[rowCount][columnCount];
        for (int row = 0; row < rowCount; row++) {
            for (int column = 0; column < columnCount; column++) {
                char character = boardShape[row].charAt(column);
                if (character == '.'){
                    tiles[row][column] = new PlayableTile(TileType.PEG);
                    pegCount++;
                }else if (character == 'o')
                    tiles[row][column] = new PlayableTile(TileType.HOLE);
                else if (character == ' ')
                    tiles[row][column] = new UnplayableTile();
            }
        }
    }

    private void generateBoard() {
        for (int row = 0; row < rowCount; row++) {
            for (int column = 0; column < columnCount; column++) {
                tiles[row][column] = new PlayableTile(TileType.PEG);
                if (row == (rowCount-1) / 2 && column == (columnCount-1) / 2)
                    tiles[row][column] = new PlayableTile(TileType.HOLE);
            }
        }
    }

    public void makeMove(int startRow, int startCol, int destRow, int destCol) {
        if (state != GameState.PLAYING) return;
        try{
            checkIfTileIsValid(startRow, startCol);
            checkIfTileIsValid(destRow, destCol);
        }catch (IllegalArgumentException exception){
            System.out.println(exception.getMessage());
        }
        if (getTile(startRow, startCol) instanceof UnplayableTile || getTile(destRow, destCol) instanceof UnplayableTile)
            System.out.println("Invalid tile!");
        else if (isValidMove(startRow, startCol, destRow, destCol)) {
            getPlayableTile((startRow + destRow) / 2, startCol).setType(TileType.HOLE);
            getPlayableTile(startRow, (startCol + destCol) / 2).setType(TileType.HOLE);
            getPlayableTile(destRow, destCol).setType(TileType.PEG);
            pegCount--;
            this.elapsedTime = (System.currentTimeMillis() - startTime) / 1000;
            if (isSolved())
                this.state = GameState.SOLVED;
            else if (isStalemate())
                this.state = GameState.STALEMATE;
        }else{
            System.out.println("Invalid coordinates!");
            System.out.printf("startRow=%d, startCol=%d, destRow=%d, destCol=%d\n", startRow, startCol, destRow, destCol);
        }
    }

    private boolean isValidMove(int startRow, int startCol, int destRow, int destCol) {
        Tile startTile = getTile(startRow, startCol);
        Tile destTile = getTile(destRow, destCol);
        if (startTile instanceof UnplayableTile || destTile instanceof UnplayableTile)
            return false;
        PlayableTile startPlayableTile = getPlayableTile(startRow, startCol);
        PlayableTile destPlayableTile = getPlayableTile(destRow, destCol);
        int rowDiff = abs(startRow - destRow);
        int colDiff = abs(startCol - destCol);
        boolean isWithinTwoSpaces = rowDiff < 2 && colDiff < 2;
        boolean isDiagonal = (startRow != destRow) && (startCol != destCol);
        boolean isSamePosition = (startRow == destRow) && (startCol == destCol);
        boolean isOutOfBounds = startRow == rowCount || startCol == columnCount || destRow == rowCount || destCol == columnCount;
        if (startPlayableTile.getType() != TileType.PEG ||
                destPlayableTile.getType() != TileType.HOLE ||
                rowDiff > 2 || colDiff > 2 || isWithinTwoSpaces ||
                isSamePosition || isDiagonal || isOutOfBounds){
            return false;
        }
        boolean sameRow = startRow == destRow;
        boolean isMiddleTileInColumnPeg = (getPlayableTile(startRow, (startCol + destCol) / 2)).getType() != TileType.PEG;
        boolean sameCol = startCol == destCol;
        boolean isMiddleTileInRowPeg = getPlayableTile((startRow + destRow) / 2, startCol).getType() != TileType.PEG;
        if ((sameRow && isMiddleTileInColumnPeg) || 
                (sameCol && isMiddleTileInRowPeg)) {
            return false;
        }
        return true;
    }

    public boolean isSolved() {
        return pegCount == 1;
    }

    public boolean isStalemate() {
        for (int row = 0; row < rowCount; row++) {
            for (int column = 0; column < columnCount; column++) {
                if (!isTilePlayable(row, column))
                    continue;
                boolean isTilePeg = getPlayableTile(row, column).getType() == TileType.PEG;
                if (isTilePeg && isMovePossibleFromCurrentTile(row, column))
                    return false;
            }
        }
        return true;
    }

    private boolean isMovePossibleFromCurrentTile(int row, int column) {
        if (row > 1 && isTilePlayable(row-1,column) && isTilePlayable(row-2,column) &&
                getPlayableTile(row - 1, column).getType() == TileType.PEG &&
                getPlayableTile(row - 2, column).getType() == TileType.HOLE) {  // move peg up
            return true;
        }else if (row < rowCount - 2 && isTilePlayable(row+1, column) && isTilePlayable(row+2, column) &&
                getPlayableTile(row + 1, column).getType() == TileType.PEG &&
                getPlayableTile(row + 2, column).getType() == TileType.HOLE) {  // move peg down
            return true;
        }else if (column > 1 && isTilePlayable(row, column-1) && isTilePlayable(row, column-2) &&
                getPlayableTile(row, column - 1).getType() == TileType.PEG &&
                getPlayableTile(row, column-2).getType() == TileType.HOLE) {  // move peg left
            return true;
        }else if (column < columnCount - 2 && isTilePlayable(row, column+1) && isTilePlayable(row, column+2) &&
                getPlayableTile(row, column+1).getType() == TileType.PEG &&
                getPlayableTile(row, column + 2).getType() == TileType.HOLE) {  // move peg right
            return true;
        }
        return false;
    }

    public Tile getTile(int row, int column) {
        return tiles[row][column];
    }
    public PlayableTile getPlayableTile(int row, int column) {
        if (tiles[row][column] instanceof PlayableTile)
            return (PlayableTile) tiles[row][column];
        else
            return null;
    }
    private boolean isTilePlayable(int row, int column){
        return tiles[row][column] instanceof PlayableTile;
    }

    private void checkIfTileIsValid(int row, int column) {
        if (row < 0 || column < 0) {
            throw new ArrayIndexOutOfBoundsException("Negative coordinates!");
        }
        if (row >= rowCount || column >= columnCount)
            throw new ArrayIndexOutOfBoundsException("Coordinates are too big!");
    }

    public int getRowCount() {
        return rowCount;
    }

    public int getColumnCount() {
        return columnCount;
    }

    public GameState getState() {
        return state;
    }
    public abstract String[] getBoardShape();
    public int getPegCount() {
        return pegCount;
    }

    public long getElapsedTime() {
        return elapsedTime;
    }
}