package com.ifce.ppd.tsoroyematatu.server;

public class PointBoard {
    private int id;
    private boolean isEmpty = true;

    public PointBoard(int id) {
        this.id = id;
    }

    public PointBoard(int id, boolean isEmpty) {
        this.id = id;
        this.isEmpty = isEmpty;
    }
}
