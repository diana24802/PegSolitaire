package sk.tuke.gamestudio.game.pegsolitaire.consoleui;

import sk.tuke.gamestudio.entity.Comment;
import sk.tuke.gamestudio.entity.Profile;
import sk.tuke.gamestudio.entity.Rating;
import sk.tuke.gamestudio.game.pegsolitaire.core.*;
import sk.tuke.gamestudio.entity.Score;
import sk.tuke.gamestudio.service.CommentService;
import sk.tuke.gamestudio.service.ProfileService;
import sk.tuke.gamestudio.service.RatingService;
import sk.tuke.gamestudio.service.ScoreService;

import java.util.Date;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ConsoleUI {
    private Board board;
    private static final Pattern COMMAND_PATTERN = Pattern.compile("([A-Z])([1-9]+) ([A-Z])([1-9]+)");
    private static final Pattern MAIN_MENU = Pattern.compile("[cCpPsSmMrR]");
    private static final Pattern PREDEFINED_BOARD_TYPE = Pattern.compile("[aAdDeEuUgGrR]");
    private final Scanner scanner = new Scanner(System.in);
    //@Autowired
    private ScoreService scoreService;// = new sk.tuke.gamestudio.service.ScoreServiceJDBC();
    //@Autowired
    private CommentService commentService;
    private RatingService ratingService;
    private ProfileService profileService;

    public ConsoleUI(ScoreService scoreService, CommentService commentService, RatingService ratingService, ProfileService profileService) {
        this.scoreService = scoreService;
        this.commentService = commentService;
        this.ratingService = ratingService;
        this.profileService = profileService;
    }
    public void play(){
        //scoreService.addScore(new Score("d", "PegSolitaire", "Custom", 12, new Date()));
        mainMenu();
        printGameState();
        printBoard();
        do {
            processInput();
            printBoard();
        }while (board.getState() == GameState.PLAYING);
        printEndScreen();
    }

    private void printEndScreen() {
        if (board.getState() == GameState.SOLVED)
            System.out.println("Congratulations! You won!");
        else if (board.getState() == GameState.STALEMATE) {
            System.out.println("Unfortunately, there are no more valid moves.");
            System.out.println("Your Score: " + board.getPegCount());
        }
        System.out.println("Would you like to add your score/comment/rating ?");
        System.out.print("If yes, type 'y', otherwise type 'n': ");
        String answer = scanner.nextLine().trim();
        if (answer.equalsIgnoreCase("y"))
            writeInServices();
    }

    private void writeInServices() {
        String answer;
        System.out.print("Would you like to write your score? (y/n): ");
        answer = scanner.nextLine().trim();
        if (answer.equalsIgnoreCase("y"))
            writeScore();
        System.out.print("Would you like to add a comment? (y/n): ");
        answer = scanner.nextLine().trim();
        if (answer.equalsIgnoreCase("y"))
            writeComment();
        System.out.print("Would you like to add a rating? (y/n): ");
        answer = scanner.nextLine().trim();
        if (answer.equalsIgnoreCase("y"))
            writeRating();
        System.out.println("Thanks for playing!");

    }

    private void writeRating() {
        int r;
        do {
            System.out.print("Enter your rating (1-5; 1-worst, 5-best): ");
            r = Integer.parseInt(scanner.nextLine().trim());
        }while (r > 5 || r < 1);
        Rating rating = new Rating(System.getProperty("user.name"), "PegSolitaire", r, new Date());
        ratingService.addRating(rating);
    }

    private void writeComment() {
        System.out.print("Enter your comment: ");
        String c = scanner.nextLine().trim();
        Comment comment = new Comment(System.getProperty("user.name"), "PegSolitaire", c, new Date());
        commentService.addComment(comment);
    }

    private void writeScore() {
        System.out.print("Enter your name: ");
        String name = scanner.nextLine().trim();
        String boardType = board.getClass().toString().substring(board.getClass().toString().lastIndexOf(".") + 1);
        Score score = new Score(name, "PegSolitaire", boardType, board.getPegCount(), new Date(), board.getElapsedTime());
        scoreService.addScore(score);
    }

    private void mainMenu() {
        System.out.println("--Peg Solitaire--");
        System.out.println("Type C to play on a custom square board or");
        System.out.println("Type P to play on a predefined board or");
        System.out.println("Type S to show top scores or");
        System.out.println("Type M to show comments or");
        System.out.print("Type R to show average rating: ");
        String input;
        Matcher matcher;
        do {
            input = scanner.nextLine().trim();
            matcher = MAIN_MENU.matcher(input);
        }while(!matcher.matches());
        if (input.equalsIgnoreCase("c"))
            createCustomBoard();
        else if (input.equalsIgnoreCase("p"))
            createPredefinedBoard();
        else if (input.equalsIgnoreCase("s"))
            printTopScores();
        else if (input.equalsIgnoreCase("m"))
            printComments();
        else if (input.equalsIgnoreCase("r"))
            printRatings();
    }

    private void printRatings() {
        if (ratingService == null) mainMenu();
        var ratings = ratingService.getAverageRating("PegSolitaire");
        System.out.println("Average rating for this game: " + ratings);
        returnToMainMenu();
    }

    private void returnToMainMenu() {
        System.out.println("Type R to return to Main Menu.");
        String choice = scanner.nextLine().trim();
        if (choice.equalsIgnoreCase("R")){
            mainMenu();
        }
    }

    private void printComments() {
        if (commentService == null) mainMenu();
        var comments = commentService.getComments("PegSolitaire");
        System.out.println(comments);
        returnToMainMenu();
    }

    private void printTopScores() {
        if (scoreService == null) mainMenu();
        var scores = scoreService.getTopScores("PegSolitaire");
        System.out.println(scores);
        returnToMainMenu();
    }

    private void createPredefinedBoard() {
        System.out.println("------------------------------------");
        System.out.println("Type A to create Asymmetrical board.");
        System.out.println("Type D to create Diamond board.");
        System.out.println("Type E to create English board.");
        System.out.println("Type U to create European board.");
        System.out.println("Type G to create German board.");
        System.out.println("Type R to return to Main Menu.");
        String choice = scanner.nextLine().trim();
        Matcher matcher = PREDEFINED_BOARD_TYPE.matcher(choice);
        while (!matcher.matches()){
            System.out.print("Type one letter that corresponds to the given boards! ");
            choice = scanner.nextLine().trim();
            matcher = PREDEFINED_BOARD_TYPE.matcher(choice);
        }
        if (choice.equalsIgnoreCase("R")){
            mainMenu();
            return;
        }
        BoardFactory factory = new BoardFactory();
        this.board = factory.createPredefinedBoard(choice.charAt(0));
    }
    private void createCustomBoard() {
        System.out.println("--------------------------------------------");
        int width = 1;
        int height = 1;
        while (26 < width || width < 4){
            System.out.print("Enter the width of the board (min 4 max 26): ");
            try {
                width = Integer.parseInt(scanner.nextLine().trim());
            }catch (NumberFormatException e){
                System.out.println("Enter a number!");
            }
        }
        while (26 < height || height < 4){
            System.out.print("Enter the height of the board (min 4 max 26): ");
            try {
                height = Integer.parseInt(scanner.nextLine().trim());
            }catch (NumberFormatException e){
                System.out.println("Enter a number!");
            }
        }
        BoardFactory factory = new BoardFactory();
        board = factory.createCustomBoard(width, height);
        board.getPlayableTile(0,1).setType(TileType.HOLE);
        board.getPlayableTile(1,1).setType(TileType.PEG);
        board.makeMove(2,1,0,1);
        board.makeMove(1,3,1,1);
        board.makeMove(0,1,2,1);
        board.makeMove(3,1,1,1);
        board.makeMove(0,3,0,1);
        board.makeMove(2,3,2,1);
        board.makeMove(3,3,3,1);
        board.makeMove(3,0,3,2);
        board.makeMove(2,0,2,2);
        board.makeMove(3,2,1,2);
        board.makeMove(0,0,2,0);
        board.makeMove(0,1,2,1);
        board.makeMove(2,0,2,2);
    }

    private void processInput() {
        System.out.print("Enter command: ");
        String line = scanner.nextLine().trim().toUpperCase();
        if ("X".equals(line))
            System.exit(0);
        Matcher matcher = COMMAND_PATTERN.matcher(line);
        if (matcher.matches()) {
            int startRow = matcher.group(1).charAt(0) - 'A';
            int startColumn = Integer.parseInt(matcher.group(2)) - 1;
            int destRow = matcher.group(3).charAt(0) - 'A';
            int destColumn = Integer.parseInt(matcher.group(4)) - 1;
            board.makeMove(startRow, startColumn, destRow, destColumn);
        } else {
            System.err.println("Invalid input! Correct input example: A1 B2");
        }
    }
    private void printGameState(){
        System.out.println("Game state: " + board.getState());
    }
    private void printBoard(){
        System.out.print("  ");
        for (int c = 0; c < board.getColumnCount(); c++)
            System.out.print(c+1 + " ");
        System.out.println();
        for (int row=0; row < board.getRowCount(); row++){
            System.out.print((char)(row+'A') + " ");
            for (int column=0; column < board.getColumnCount(); column++){
                printTile(board.getTile(row,column));
                System.out.print(" ");
            }
            System.out.println();
        }
        System.out.println("Peg count: " + board.getPegCount());
        System.out.println("-----------");
    }
    private void printTile(Tile tile){
        TileType type;
        if (tile instanceof PlayableTile) {
            type = ((PlayableTile) tile).getType();
            if (type == TileType.PEG) System.out.print("●");
            else if (type == TileType.HOLE) System.out.print("o");
        }
        else
            System.out.print(" ");
    }
}