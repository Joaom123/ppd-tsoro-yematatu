package com.ifce.ppd.tsoroyematatu.models;

import javafx.scene.shape.Circle;

public class PieceFront {
    private final Circle piece;
    private Circle point = null;

    public PieceFront(Circle piece) {
        this.piece = piece;
    }

    /**
     * Piece's getter.
     *
     * @return The piece.
     */
    public Circle getPiece() {
        return piece;
    }

    /**
     * Point's getter.
     *
     * @return The point.
     */
    public Circle getPoint() {
        return point;
    }

    /**
     * Point's setter.
     *
     * @param point Point to be set.
     */
    public void setPoint(Circle point) {
        this.point = point;
    }
}
