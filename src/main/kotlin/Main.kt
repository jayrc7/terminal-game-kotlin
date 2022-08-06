import model.Gameboard
import java.util.concurrent.locks.ReentrantLock

fun main(args: Array<String>) {
    // initialize gameboard
    var gameboard = Gameboard();

    // initialize game state machine
    var machine = GameStateMachine(gameboard);
}