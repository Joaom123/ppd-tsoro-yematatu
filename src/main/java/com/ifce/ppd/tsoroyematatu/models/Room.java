package com.ifce.ppd.tsoroyematatu.models;

import com.ifce.ppd.tsoroyematatu.exceptions.MaximumNumberPlayersInTheRoomException;
import com.ifce.ppd.tsoroyematatu.server.PlayerThread;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

public class Room {
    private final String id;
    private final Set<PlayerThread> playersThreads = new HashSet<>();

    public Room(String id) {
        this.id = id;
    }

    public void addPlayer(PlayerThread playerThread) throws MaximumNumberPlayersInTheRoomException {
        if (maximumNumberPlayersReached())
            throw new MaximumNumberPlayersInTheRoomException();
        playersThreads.add(playerThread);
    }

    public String getId() {
        return id;
    }

    public PlayerThread getRivalPlayerThread(PlayerThread playerThread) {
        for (PlayerThread playerThread1 : playersThreads) {
            if (playerThread != playerThread1)
                return playerThread1;
        }
        return playerThread;
    }

    public boolean maximumNumberPlayersReached() {
        return playersThreads.size() == 2;
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
}
