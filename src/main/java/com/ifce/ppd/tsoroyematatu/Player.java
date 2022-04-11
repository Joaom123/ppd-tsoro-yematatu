package com.ifce.ppd.tsoroyematatu;

/**
 * The representation of the Player. It has a Client, useful to differentiate one player from another.
 * The clientCallback is useful for the server execute functions on the client-side.
 */
public class Player {
    private final Server server;
    private final Client client;
    private final ClientCallback clientCallback;
    private Room room;
    private boolean isFirstPlayer = false;

    public Player(Server server, Client client, Room room, ClientCallback clientCallback) {
        this.server = server;
        this.client = client;
        this.room = room;
        this.clientCallback = clientCallback;
    }

    public Room getRoom() {
        return room;
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

    public Client getClient() {
        return client;
    }

    public ClientCallback getClientCallback() {
        return clientCallback;
    }

    public ClientCallback getRivalClientCallback() {
        return room.getRivalPlayer(this).getClientCallback();
    }
}
