import model.Obstacle
import model.Monster
import model.Player
import thread.MonsterControllerThread
import java.util.*

class GameStateMachine constructor (private var gameBoard : Gameboard, private var player : Player) {
    // spawns obstacles on gameboard
    //var obstacleSpawner = ObstacleSpawner(gameBoard);

    // contains player's current level
    var level = 1;

    // spawns and controls monsters on gameboard
    var monsterSpawner = MonsterControllerThread(gameBoard, level, this);

    var monsters : List<Monster> = listOf();

    var obstacles : List<Obstacle> = listOf();


    // defining states
    val NEW_GAME = "NEW_GAME";
    var NEW_ROOM = "NEW_ROOM";
    val WAITING = "WAITING";
    val MOVE_USER = "MOVE_USER";
    // var MOVE_MONSTERS = "MOVE_MONSTERS; not supported yet
    val GAME_OVER = "GAME_OVER";
    val CHECK_GAME_OVER = "CHECK_GAME_OVER";

    // for player movement directions
    val UP = 'w';
    val DOWN = 's';
    val LEFT = 'a';
    val RIGHT = 'd';

    // exit room variables
    val EXIT_ROOM_X = gameBoard.getTotalCols() - 1;
    val EXIT_ROOM_Y = gameBoard.getTotalRows() - 1;

    init {
        // keeps track of current state
        var state = NEW_GAME;
        while (true) {
            // compute new state
            val newState = executeCurrentState(state);

            // check if game is finished
            if (newState == GAME_OVER) {
                println("\nGame over! Thanks for playing!");
                // do stuff but break after
                break;
            }

            state = newState;
        }
    }


    fun executeCurrentState(state : String, userInput: Char = '0') : String {
        // in 1500ms (1.5 seconds), we would want the following to be accomplished:
        // - player would've moved
        // - monsters would've moved
        // TODO tell monster mover thread to move monsters in background
        var newState : String;
        when (state) {
            NEW_GAME -> newState = newGame();
            NEW_ROOM -> newState = newRoom();
            WAITING -> newState = waiting();
            MOVE_USER -> {
                gameBoard.claimNextTurn();
                newState = moveUser(userInput)
            };
            CHECK_GAME_OVER -> newState = checkGameOver();
            else -> { newState = GAME_OVER }
        }

        // only release lock if state machine has current turn and not monster controller thread
        if (gameBoard.doesPlayerHaveCurrentTurn())
            gameBoard.completedCurrentTurn();

        return newState;
    }

    /**
     * Queries user if they'd like to start a new game
     * Transitions to: NEW_ROOM
     */
    fun newGame() : String {
        // Print welcome text
        println("Welcome to the game. Objective is to reach the end of the third room, while defeating the monsters \n" +
                "that stand in your way!");

        // only start game if enter is pressed
        do {
            println("Press enter to play!");
            val userInput = readLine();

        } while (userInput != "");

        return NEW_ROOM;
    }

    /**
     * Used for generating playing field
     * Transitions to: WAITING
     */
    fun newRoom() : String {
        gameBoard.claimNextTurn();

        // reset player location
        gameBoard.resetPlayerLocation();

        // delete current monster thread if initialized

        /*
        MonsterControllerThread.currentThread();


//        this has been deprecated for now
//        // generate obstacles
//        obstacleSpawner.generateObstacles(level);

        // populate monsters on board
        monsterSpawner.generateMonsters(level);
         */

        // setup gameboard
        gameBoard.setupRoom(player);

        // print status of current room
        println("Current level: ${level}\n");
        gameBoard.printRoom();
        gameBoard.completedCurrentTurn();
        return WAITING;
    }

    /**
     * Waits for user input
     * Transitions to: WAITING, MOVE_USER
     */
    fun waiting() : String {
        val scanner = Scanner(System.`in`);
        val input = scanner.next()[0];
        if (input != LEFT && input != UP && input != DOWN && input != RIGHT) {
            return WAITING;
        }

        return executeCurrentState(MOVE_USER, input);
    }

    /**
     * Moves user in gameboard by calling move method inside gameboard
     * Transitions to: WAITING, MOVE_MONSTER (not supported), NEW_ROOM, GAME_OVER
     */
    private fun moveUser(direction: Char) : String {
        // move user according to direction specified
        gameBoard.moveUser(direction);

        // print status of current room after move was made
        gameBoard.printRoom();


        return CHECK_GAME_OVER;
    }

    // TODO add logic for checking if monster won
    fun checkGameOver() : String {
        // check if user has reached objective
        if (gameBoard.getPlayerXCoordinate() == EXIT_ROOM_X && gameBoard.getPlayerYCoordinate() == EXIT_ROOM_Y) {
            // check if user won the game
            if (level == 3) {
                return GAME_OVER;
            }

            println("\nLevel completed! Now starting level ${level+1}!");

            // increment to the next level
            level++;

            return NEW_ROOM;
        }

        return WAITING;
    }

    /**
     * MonsterControllerThread will attempt to call this method multiple times
     */
    fun moveMonsters() {
        gameBoard.claimNextTurn();
        gameBoard.completedCurrentTurn();
    }
}