package com.ifce.ppd.tsoroyematatu.exceptions;

public class NoRivalException extends Exception {
    /**
     * Thrown when there is no rival player
     */
    public NoRivalException() {
        super("Não há rival!");
    }
}
