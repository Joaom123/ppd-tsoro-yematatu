package com.ifce.ppd.tsoroyematatu.services;

public enum MESSAGE_TYPES {
    INIT(0),
    MESSAGE(1),
    MOVE(2),
    EXIT(3),
    ;

    private final int flag;

    MESSAGE_TYPES(int i) {
        flag = i;
    }

    public int getFlag() {
        return flag;
    }
}
