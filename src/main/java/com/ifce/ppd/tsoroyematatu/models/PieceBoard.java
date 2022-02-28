package com.ifce.ppd.tsoroyematatu.models;

import com.ifce.ppd.tsoroyematatu.server.PlayerThread;

public class PieceBoard {
    private final String id;
    private final PlayerThread ownerPlayer;

    public PieceBoard(String id, PlayerThread ownerPlayer) {
        this.id = id;
        this.ownerPlayer = ownerPlayer;
    }

    public String getId() {
        return id;
    }

    public PlayerThread getOwnerPlayer() {
        return ownerPlayer;
    }
}
