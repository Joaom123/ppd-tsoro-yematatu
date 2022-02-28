package com.ifce.ppd.tsoroyematatu.models;

import com.ifce.ppd.tsoroyematatu.models.PieceBoard;

public class PointBoard {
    private final String id;
    private PieceBoard pieceBoard = null;

    public PointBoard(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public PieceBoard getPieceBoard() {
        return pieceBoard;
    }

    public void setPieceBoard(PieceBoard pieceBoard) {
        this.pieceBoard = pieceBoard;
    }

    public boolean isOccupied() {
        return pieceBoard != null;
    }
}
