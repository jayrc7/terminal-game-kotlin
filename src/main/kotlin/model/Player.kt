package model

class Player {
    // fields used for player's location
    private var playerXCoordinate = 0;
    private var playerYCoordinate = 0;

    fun getXCoordinate() : Int {
        return playerXCoordinate;
    }

    fun getYCoordinate() : Int {
        return playerYCoordinate;
    }

    fun moveUp() {
        playerYCoordinate--;
    }

    fun moveDown() {
        playerYCoordinate++;
    }

    fun moveLeft() {
        playerXCoordinate--;
    }

    fun moveRight() {
        playerXCoordinate++;
    }


    fun resetLocation() {
        playerXCoordinate = 0;
        playerYCoordinate = 0;
    }
}