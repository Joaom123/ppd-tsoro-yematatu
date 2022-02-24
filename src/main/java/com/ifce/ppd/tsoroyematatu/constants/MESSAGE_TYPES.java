package com.ifce.ppd.tsoroyematatu.constants;

public enum MESSAGE_TYPES {
    INIT(0),
    MESSAGE(1),
    MOVE(2),
    WAITING(3),
    PLAYABLE(4),
    EXIT(5);

    private final int flag;

    MESSAGE_TYPES(int i) {
        flag = i;
    }

    public int getFlag() {
        return flag;
    }
}
