import thread.PlayerControllerThread

fun main(args: Array<String>) {
    // print welcome text
    println("Welcome to the game. Objective is to reach the end of the third room, while defeating the monsters \n" +
            "that stand in your way!");

    // wait until user presses enter key to start the game
    do {
        println("Press enter to play!");
    } while (readLine() != "");

    // initialize gameboard
    val gameboard = Gameboard();
    gameboard.newRoom();

    // initialize new thread for taking user inputs
    var playerController = PlayerControllerThread(gameboard);
    playerController.start();
}