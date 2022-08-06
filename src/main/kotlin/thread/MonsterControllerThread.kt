package thread

import model.Gameboard
import model.Monster
import java.util.concurrent.locks.ReentrantLock
import kotlin.random.Random

class MonsterControllerThread constructor(var gameboard : Gameboard, level : Int, val sharedMovementLock : ReentrantLock) : Thread() {
    var startedWork = false;
    val totalMonsters = level * 2;
    val monsters = List<Monster>(totalMonsters){ i -> Monster() };

    override  fun run() {
        startedWork = true;

        // generate monsters on board
        generateMonsterPositions();


        // move monsters until thread is interrupted
        // thead will be interrupted when the player reaches a new room or the game ends
        while(!isInterrupted) {
            sharedMovementLock.lock();

            sharedMovementLock.unlock();
        }
    }
    fun generateMonsterPositions() {
        // update location of each monster
        var currentMonster : Monster;
        for (i in 0..totalMonsters-1) {
            currentMonster = monsters[i];
            determineCoordinates(currentMonster);
        }
    }

    /**
     * Determines the x,y coordinates for the current monster
     * Rule for generating monster location:
     *      - must be at least three columns away from the player
     */
    private fun determineCoordinates(currentMonster: Monster) {
        currentMonster.setXCoordinate(Random.nextInt(4, ));
    }
}
