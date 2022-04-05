package com.ifce.ppd.tsoroyematatu;

import com.ifce.ppd.tsoroyematatu.PlayerThread;

public class PieceBoard {
    private final String id;
    private final PlayerThread ownerPlayer;

    public PieceBoard(String id, PlayerThread ownerPlayer) {
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
    public PlayerThread getOwnerPlayer() {
        return ownerPlayer;
    }
}
