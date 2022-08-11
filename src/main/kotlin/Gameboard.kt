import java.lang.System.exit
import java.util.concurrent.locks.ReentrantLock

class Gameboard constructor(private val ROWS: Int = 3, private val COLS: Int = 4) {

    private var claimTurnLock : ReentrantLock = ReentrantLock();

    private var gameBoard : Array<CharArray> = Array<CharArray> (ROWS) { i -> CharArray(COLS) }

    // for player movement directions
    val UP = 'w';
    val DOWN = 's';
    val LEFT = 'a';
    val RIGHT = 'd';

    // contains player's current level
    var level = 1;

    // exit room variables
    val EXIT_ROOM_X = COLS - 1;
    val EXIT_ROOM_Y = ROWS - 1;

    var playerX = 0;
    var playerY = 0;

    fun checkGameOver() {
        // check if user has reached objective
        if (playerX == EXIT_ROOM_X && playerY == EXIT_ROOM_Y) {
            // check if user won the game
            if (level == 3) {
                println("\nPlayer wins! Thanks for playing!");
                exit(0);
            }

            println("\nLevel completed! Now starting level ${level+1}!");

            // increment to the next level
            level++;

            newRoom();
        }
    }

    fun newRoom() {
        claimNextTurn();

        // reset player location
        resetPlayerLocation();

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
        setupRoom();

        // print status of current room
        println("Current level: ${level}\n");
        printRoom();
        completedCurrentTurn();
    }

    fun printRoom() {
        // double for loop for generating game board, no monsters are rendered yet
        // player is represented by $, free space by -, and objective marker as 0
        for (i in 0..ROWS-1) {
            // iterate over board cols
            for (j in 0..COLS-1) {
                print(gameBoard[i][j]);
            }

            // print an empty line for padding
            println()
        }
    }

    fun setupRoom() {
        for (i in 0..ROWS-1) {
            // iterate over board cols
            for (j in 0..gameBoard[0].size - 1) {
                // TODO add logic for monsters
                // render player if current position in board is player position, otherwise render free space
                if (i == playerX && j == playerY)
                    gameBoard[i][j] = '$';
                else if (i == ROWS-1 && j == COLS-1)
                    gameBoard[i][j] = '0';
                else
                    gameBoard[i][j] = '_';
            }
        }
    }

    fun claimNextTurn() {
        claimTurnLock.lock();
    }

    fun completedCurrentTurn() {
        claimTurnLock.unlock();
    }

    fun moveUser(direction : Char) {
        // move user based on direction
        when (direction) {
            UP -> {
                if (playerY != 0) {
                    gameBoard[playerY][playerX] = '_';
                    gameBoard[--playerY][playerX] = '$';
                }

            }

            DOWN -> {
                if (playerY != ROWS - 1) {
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
                if (playerX != COLS - 1) {
                    gameBoard[playerY][playerX] = '_';
                    gameBoard[playerY][++playerX] = '$';
                };
            }

            else -> {
                // do nothing
            }
        }
    }

    fun resetPlayerLocation() {
        playerX = 0;
        playerY = 0;
    }

    /**
     * MonsterControllerThread will attempt to call this method every 1.5 seconds
     */
    fun moveMonstersInBoard() {
        claimTurnLock.lock();
    }

}
