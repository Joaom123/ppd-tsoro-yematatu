package com.ifce.ppd.tsoroyematatu.exceptions;

public class NullClientException extends Exception {
    /**
     * Exceção lançada quando não há cliente (Client)
     */
    public NullClientException() {
        super("Não há cliente!");
    }
}
