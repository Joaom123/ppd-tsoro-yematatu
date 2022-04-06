package com.ifce.ppd.tsoroyematatu;

/**
 * Handles the connection for each connected player(client).
 */
public class Player {
    private final Server server;
    private final Client client;
    private Room room;
    private boolean isFirstPlayer = false;

    public Player(Server server, Client client, Room room) {
        this.server = server;
        this.client = client;
        this.room = room;
    }

    public void setRoom(Room room) {
        this.room = room;
    }

    public boolean isFirstPlayer() {
        return isFirstPlayer;
    }

    public void setFirstPlayer(boolean firstPlayer) {
        isFirstPlayer = firstPlayer;
    }
}
