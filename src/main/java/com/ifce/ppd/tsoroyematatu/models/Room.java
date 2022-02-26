package com.ifce.ppd.tsoroyematatu.models;

import com.ifce.ppd.tsoroyematatu.exceptions.MaximumNumberPlayersInTheRoomException;
import com.ifce.ppd.tsoroyematatu.exceptions.NoRivalException;
import com.ifce.ppd.tsoroyematatu.server.Game;
import com.ifce.ppd.tsoroyematatu.server.PlayerThread;

import java.io.IOException;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

public class Room {
    private final String id;
    private final Set<PlayerThread> playersThreads = new HashSet<>();
    private final Game game;

    public Room(String id) {
        this.id = id;
        game = new Game(this);
    }

    public void addPlayer(PlayerThread playerThread) throws MaximumNumberPlayersInTheRoomException {
        if (isFull())
            throw new MaximumNumberPlayersInTheRoomException();
        playersThreads.add(playerThread);
    }

    public String getId() {
        return id;
    }

    public Game getGame() {
        return game;
    }

    public PlayerThread getRivalPlayerThread(PlayerThread playerThread) throws NoRivalException {
        for (PlayerThread playerThread1 : playersThreads) {
            if (playerThread != playerThread1)
                return playerThread1;
        }
        throw new NoRivalException();
    }

    public void removePlayerThread(PlayerThread playerThread) {
        playersThreads.remove(playerThread);
    }

    public boolean isFull() {
        return playersThreads.size() == 2;
    }

    public void sendPlayable() throws IOException {
        for (PlayerThread playerThread : playersThreads)
            playerThread.sendPlayableFlag();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Room)) return false;
        Room room = (Room) o;
        return id.equals(room.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    public void sendMoveToPlayers(String pieceId, String pointId) throws IOException {
        for (PlayerThread playerThread : playersThreads)
            playerThread.sendMoveToPlayer(pieceId, pointId);
    }
}
