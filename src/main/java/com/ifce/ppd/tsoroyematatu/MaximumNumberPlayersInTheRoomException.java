package com.ifce.ppd.tsoroyematatu;

public class MaximumNumberPlayersInTheRoomException extends Exception {
    /**
     * Thrown when there is 2 players in the room
     */
    public MaximumNumberPlayersInTheRoomException() {
        super("Número máximo de jogadores na sala foi atingido!");
    }
}
