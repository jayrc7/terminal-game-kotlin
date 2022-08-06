package model

import java.util.concurrent.locks.ReentrantLock

class Gameboard constructor(private val ROWS: Int = 7, private val COLS: Int = 18,
                            private var level : Int = 0, private var player : Player) {

    private var claimTurnLock : ReentrantLock = ReentrantLock();

    private var gameBoard : Array<CharArray> = Array<CharArray> (ROWS) { i -> CharArray(COLS) }

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
        // print stats
        println("Current level: ${level}");
        println();

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
}