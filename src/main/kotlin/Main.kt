import model.Player

fun main(args: Array<String>) {
    // initialize player
    val player =  Player()

    // initialize gameboard
    val gameboard = Gameboard(player);

    // initialize game state machine
    val machine = GameStateMachine(gameboard, player);
}