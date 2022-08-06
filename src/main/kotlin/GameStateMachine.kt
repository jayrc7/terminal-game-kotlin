import model.Obstacle
import model.Gameboard
import model.Monster
import thread.MonsterControllerThread
import java.util.*
import java.util.concurrent.locks.ReentrantLock

class GameStateMachine constructor (private var gameBoard : Gameboard) {
    // spawns obstacles on gameboard
    //var obstacleSpawner = ObstacleSpawner(gameBoard);

    // contains player's current level
    var level = 1;

    // spawns and controls monsters on gameboard
    var monsterSpawner = MonsterControllerThread(gameBoard, level);

    var monsters : List<Monster> = listOf();

    var obstacles : List<Obstacle> = listOf();

    // defining states
    val NEW_GAME = "NEW_GAME";
    var NEW_ROOM = "NEW_ROOM";
    val WAITING = "WAITING";
    val MOVE_USER = "MOVE_USER";
    // var MOVE_MONSTERS = "MOVE_MONSTERS; not supported yet
    val GAME_OVER = "GAME_OVER";

    // for player movement directions
    val UP = 'w';
    val DOWN = 's';
    val LEFT = 'a';
    val RIGHT = 'd';

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
            else -> { newState = GAME_OVER }
        }

        // only release lock if it is locked
        // TODO issue here is that this may release lock from monster thread control, need to denote whether
        // TODO this thread specifically is holding the lock
        /*
        if (.isLocked)
            sharedMovementLock.unlock();
         */

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
        sharedMovementLock.lock();
        // reset player location
        playerX = 0;
        playerY = 0;

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

        // print status of current room
        gameBoard.printRoom();
        sharedMovementLock.lock();
        return WAITING;
    }

    /**
     * Waits for user input
     * Transitions to: WAITING, MOVE_USER
     */
    fun waiting() : String {

        val scanner = Scanner(System.`in`);
        val input = scanner.next()[0];
        if (input != 'a' && input != 'w' && input != 's' && input != 'd') {
            return WAITING;
        }

        return executeCurrentState(MOVE_USER, input);
    }

    /**
     * Moves user in gameboard
     * Transitions to: WAITING, MOVE_MONSTER (not supported), NEW_ROOM, GAME_OVER
     */
    private fun moveUser(direction: Char) : String {
        // move user based on direction
        when (direction) {
            UP -> {
                if (playerY != 0) {
                    gameBoard[playerY][playerX] = '_';
                    gameBoard[--playerY][playerX] = '$';
                }

            }

            DOWN -> {
                if (playerY != ROWS-1) {
                    gameBoard[playerY][playerX] = '_';
                    gameBoard[++playerY][playerX] = '$';
                }
            }

            LEFT -> {
                if (playerX != 0) {
                    gameBoard[playerY][playerX] = '_';
                    gameBoard[playerY][--playerX] = '$';
                };
            }

            RIGHT -> {
                if (playerX != COLS-1) {
                    gameBoard[playerY][playerX] = '_';
                    gameBoard[playerY][++playerX] = '$';
                };
            }

            else -> {
                // do nothing
            }
        }

        // print status of current room
        printRoom();

        // check if user has reached objective
        if (playerX == EXIT_ROOM_X && playerY == EXIT_ROOM_Y) {
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
}