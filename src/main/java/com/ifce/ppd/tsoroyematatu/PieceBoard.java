package com.ifce.ppd.tsoroyematatu;

/**
 * The representation of the board's piece. The piece can be owned by a player.
 */
public class PieceBoard {
    private final String id;
    private final Player ownerPlayer;

    public PieceBoard(String id, Player ownerPlayer) {
        this.id = id;
        this.ownerPlayer = ownerPlayer;
    }

    /**
     * Id's getter.
     *
     * @return The id.
     */
    public String getId() {
        return id;
    }

    /**
     * OwnerPlayer's getter.
     *
     * @return The player.
     */
    public Player getOwnerPlayer() {
        return ownerPlayer;
    }
}
