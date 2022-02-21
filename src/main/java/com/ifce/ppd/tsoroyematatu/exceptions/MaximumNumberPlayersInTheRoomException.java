package com.ifce.ppd.tsoroyematatu.exceptions;

public class MaximumNumberPlayersInTheRoomException extends Exception {
    public MaximumNumberPlayersInTheRoomException() {
        super("Número máximo de jogadores na sala foi atingido!");
    }
}
