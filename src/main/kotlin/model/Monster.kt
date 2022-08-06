package model

class Monster {
    // monster's location
    var monsterX : Int;
    var monsterY : Int;

    var health : Int;

    init {
        // determine coordinates of monster respawn
        monsterX = 0;
        monsterY = 0;
        health = 100;
    }

    fun getXCoordinate() : Int {
        return monsterX;
    }

    fun getYCoordinate() : Int {
        return monsterY;
    }

    fun setXCoordinate(x : Int) {
        monsterX = x;
    }

    fun setYCoordinate(y : Int) {
        monsterY = y;
    }
}
