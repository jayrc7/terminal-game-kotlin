import model.Player
import java.util.concurrent.locks.ReentrantLock

class Gameboard constructor(private var player : Player, private val ROWS: Int = 3, private val COLS: Int = 4) {

    private var claimTurnLock : ReentrantLock = ReentrantLock();

    private var gameBoard : Array<CharArray> = Array<CharArray> (ROWS) { i -> CharArray(COLS) }

    // boolean to determine whether monsters have current turn
    var isItMonstersTurn = false;

    // for player movement directions
    val UP = 'w';
    val DOWN = 's';
    val LEFT = 'a';
    val RIGHT = 'd';

    fun getTotalRows() : Int {
        return ROWS;
    }

    fun getTotalCols() : Int {
        return COLS;
    }

    fun getBoard() : Array<CharArray> {
        return gameBoard;
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

    fun setupRoom(player : Player) {
        for (i in 0..ROWS-1) {
            // iterate over board cols
            for (j in 0..gameBoard[0].size - 1) {
                // TODO add logic for monsters
                // render player if current position in board is player position, otherwise render free space
                if (i == player.getXCoordinate() && j == player.getYCoordinate())
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

    fun isCurrentTurnClaimed() : Boolean {
        return claimTurnLock.isLocked;
    }

    fun moveUser(direction : Char) {
        // get player coordinates
        var playerX = player.getXCoordinate();
        var playerY = player.getYCoordinate();

        // move user based on direction
        when (direction) {
            UP -> {
                if (playerY != 0) {
                    player.moveUp();
                    gameBoard[playerY][playerX] = '_';
                    gameBoard[playerY][playerX] = '$';
                }

            }

            DOWN -> {
                if (playerY != ROWS - 1) {
                    player.moveDown();
                    gameBoard[playerY][playerX] = '_';
                    gameBoard[++playerY][playerX] = '$';
                }
            }

            LEFT -> {
                if (playerX != 0) {
                    player.moveLeft();
                    gameBoard[playerY][playerX] = '_';
                    gameBoard[playerY][--playerX] = '$';
                };
            }

            RIGHT -> {
                if (playerX != COLS - 1) {
                    player.moveRight();
                    gameBoard[playerY][playerX] = '_';
                    gameBoard[playerY][++playerX] = '$';
                };
            }

            else -> {
                // do nothing
            }
        }
    }

    fun getPlayerXCoordinate() : Int {
        return player.getXCoordinate();
    }

    fun getPlayerYCoordinate() : Int {
        return player.getYCoordinate();
    }

    fun resetPlayerLocation() {
        player.resetLocation();
    }

    // TODO make sure updating is monster turn flag is added
    fun doesPlayerHaveCurrentTurn() : Boolean {
        return claimTurnLock.isLocked && !isItMonstersTurn;
    }

    /**
     * MonsterControllerThread will attempt to call this method every 1.5 seconds
     */
    fun moveMonstersInBoard() {
        claimTurnLock.lock();

    }

}
