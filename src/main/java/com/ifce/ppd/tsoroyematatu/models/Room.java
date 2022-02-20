package com.ifce.ppd.tsoroyematatu.models;

import com.ifce.ppd.tsoroyematatu.server.PlayerThread;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

public class Room {
    private String id;
    private Set<PlayerThread> playersThreads = new HashSet<>();

    public Room(String id) {
        this.id = id;
    }

    public void addPlayer(PlayerThread playerThread) {

    }

    public String getId() {
        return id;
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
