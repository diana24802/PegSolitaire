package sk.tuke.gamestudio.server.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.WebApplicationContext;
import sk.tuke.gamestudio.entity.Score;
import sk.tuke.gamestudio.game.pegsolitaire.core.*;
import sk.tuke.gamestudio.service.ScoreService;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static sk.tuke.gamestudio.game.pegsolitaire.core.TileType.PEG;
import static sk.tuke.gamestudio.game.pegsolitaire.core.TileType.HOLE;

@Controller
@RequestMapping("/pegsolitaire")
@Scope(WebApplicationContext.SCOPE_SESSION)
public class PegSolitaireController {
    private Board board;
    @Autowired
    private MainController mainController;
    @Autowired
    private ScoreService scoreService;
    private boolean added = false;
    @RequestMapping
    public String PegSolitaire(@RequestParam(required = false) String row, @RequestParam(required = false) String column, @RequestParam(required = false) String destRow, @RequestParam(required = false) String destCol) {
        if (board == null) {
            board = new English();
        }
        processCommand(row, column, destRow, destCol);
        return "PegSolitaire";
    }

    private void processCommand(String row, String column, String destRow, String destCol) {
        try {
            if (board.getState() == GameState.PLAYING) {
                if (row != null && column != null && destRow != null && destCol != null) {
                    board.makeMove(Integer.parseInt(row), Integer.parseInt(column), Integer.parseInt(destRow), Integer.parseInt(destCol));
                }
            }if ((board.getState() == GameState.SOLVED || board.getState() == GameState.STALEMATE)
                    && mainController.isLogged() && !added) {
                String boardType = board.getClass().toString().substring(board.getClass().toString().lastIndexOf(".") + 1);
                Score score = new Score(mainController.getLoggedProfile().getLogin(), "PegSolitaire",
                        boardType, board.getPegCount(), new Date(), board.getElapsedTime());
                scoreService.addScore(score);
                added = true;
                System.out.println("Score added");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("Game is " + board.getState());
        System.out.println("Elapsed time: " + board.getElapsedTime());
    }

    @RequestMapping("/new")
    public String newGame(@RequestParam(required = false) String boardType, @RequestParam(required = false) Integer rowCount, @RequestParam(required = false) Integer columnCount) {
        if (boardType == null) {
            board = new English();
        } else {
            switch (boardType) {
                case "Asymmetrical" -> board = new Asymmetrical();
                case "Custom" -> board = new CustomBoard(rowCount, columnCount);
                case "Diamond" -> board = new Diamond();
                case "English" -> board = new English();
                case "European" -> board = new European();
                case "German" -> board = new German();
                default -> board = new English();
            }
        }
        return "redirect:/pegsolitaire";
    }


    @RequestMapping(value = "/board", produces = MediaType.TEXT_HTML_VALUE)
    @ResponseBody
    public String pegSolitaire(@RequestParam(required = false) String row, @RequestParam(required = false) String column, @RequestParam(required = false) String destRow, @RequestParam(required = false) String destCol) {
        processCommand(row, column, destRow, destCol);
        return getHtmlBoard();
    }
    @RequestMapping(value = "/score", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public Map<String, Integer> getScore() {
        Map<String, Integer> scoreData = new HashMap<>();
        scoreData.put("score", board.getPegCount());
        return scoreData;
    }

    public String getHtmlBoard(){
        if (board == null) return "";
        StringBuilder sb = new StringBuilder();
        sb.append("<table class='board'>\n");
        for(int row = 0; row < board.getRowCount(); row++){
            sb.append("<tr>\n");
            for(int column = 0; column < board.getColumnCount(); column++){
                sb.append("<td>\n");
                Tile tile = board.getPlayableTile(row,column);
                if (tile != null)
                    sb.append("<a href='/pegsolitaire?row=" + row + "&column=" + column + "'>\n");
                appendTileImage(tile, sb, row, column);
                if (tile != null)
                    sb.append("</a>\n");
                sb.append("</td>\n");
            }
            sb.append("</tr>\n");
        }
        sb.append("</table>\n");
        return sb.toString();
    }

    private void appendTileImage(Tile tile, StringBuilder sb, int row, int column) {
        if (tile != null){
            if (((PlayableTile) tile).getType() == PEG){
                sb.append("<img src='/images/pegsolitaire/" + "peg" + ".png'" +
                        "class='peg' id='peg-" + row + "-" + column + "' data-row='" + row + "' data-col='" + column + "'>\n");
            }else if (((PlayableTile) tile).getType() == HOLE){
                sb.append("<img src='/images/pegsolitaire/" + "hole" + ".png'" +
                        "class='hole' id='hole-" + row + "-" + column + "' data-row='" + row + "' data-col='" + column + "'>\n");
            }
        }else{
            sb.append("<img src='/images/pegsolitaire/" + "unplayable" + ".png'" +
                    "class='unplayable' id='unplayable-" + row + "-" + column + "' data-row='" + row + "' data-col='" + column + "'>\n");
        }
    }
    public int getPegCount(){
        return board.getPegCount();
    }
    public boolean isBoardInitialized(){
        return board != null;
    }
}
