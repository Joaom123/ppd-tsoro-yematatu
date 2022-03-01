package com.ifce.ppd.tsoroyematatu.models;

public class PointBoard {
    private final String id;
    private PieceBoard pieceBoard = null;

    public PointBoard(String id) {
        this.id = id;
    }

    /**
     * Id's getter.
     *
     * @return Id value
     */
    public String getId() {
        return id;
    }

    /**
     * PieceBoard's getter.
     *
     * @return The pieceBoard.
     */
    public PieceBoard getPieceBoard() {
        return pieceBoard;
    }

    /**
     * PieceBoard's setter.
     *
     * @param pieceBoard Piece board to be set.
     */
    public void setPieceBoard(PieceBoard pieceBoard) {
        this.pieceBoard = pieceBoard;
    }

    /**
     * @return True if is occupied by a pieceBoard. False if isn't occupied.
     */
    public boolean isOccupied() {
        return pieceBoard != null;
    }
}
