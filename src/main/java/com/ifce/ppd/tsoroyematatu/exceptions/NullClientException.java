package com.ifce.ppd.tsoroyematatu.exceptions;

public class NullClientException extends Exception {
    /**
     * Thrown when there is no client
     */
    public NullClientException() {
        super("Não há cliente!");
    }
}
