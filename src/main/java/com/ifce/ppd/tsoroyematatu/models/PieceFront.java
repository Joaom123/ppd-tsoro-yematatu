package com.ifce.ppd.tsoroyematatu.models;

import javafx.scene.shape.Circle;

public class PieceFront {
    private final Circle piece;
    private Circle point = null;

    public PieceFront(Circle piece) {
        this.piece = piece;
    }

    public void setPoint(Circle point) {
        this.point = point;
    }

    public Circle getPiece() {
        return piece;
    }

    public Circle getPoint() {
        return point;
    }
}
