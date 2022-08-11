package thread

import Gameboard
import java.util.*

class PlayerControllerThread constructor (val gameboard: Gameboard): Thread() {
    // for player movement directions
    val UP = 'w';
    val DOWN = 's';
    val LEFT = 'a';
    val RIGHT = 'd';

    override fun run() {
        while (true) {
            println("enter next move: ");
            val scanner = Scanner(System.`in`);
            val input = scanner.next()[0];
            if (input == LEFT || input == UP || input == DOWN || input == RIGHT) {
                gameboard.claimNextTurn(); // obtain turn lock
                gameboard.moveUser(input); // move user
                gameboard.printRoom(); // print room after moving user
                gameboard.completedCurrentTurn(); // release turn lock

                // check if user completed game or if new room needs to be made
                gameboard.checkGameOver();

                // delay user's next move by a second
                sleep(1000);
            }
        }

    }

}