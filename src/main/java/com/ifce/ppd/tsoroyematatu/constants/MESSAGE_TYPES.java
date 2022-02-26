package com.ifce.ppd.tsoroyematatu.constants;

public enum MESSAGE_TYPES {
    INIT(0),
    MESSAGE(1),
    MOVE(2),
    WAIT_RIVAL_CONNECT(3),
    WAIT_RIVAL_MAKE_MOVE(4),
    CAN_PLAY(5),
    PLAYABLE(6),
    EXIT(7);

    private final int flag;

    MESSAGE_TYPES(int i) {
        flag = i;
    }

    public int getFlag() {
        return flag;
    }
}
