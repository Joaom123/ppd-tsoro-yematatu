package com.ifce.ppd.tsoroyematatu;

import java.io.Serializable;
import java.util.UUID;

public class Client implements Serializable {
    private final String id;
    private final String name;

    public Client(String name) {
        this.name = name;
        this.id = UUID.randomUUID().toString();
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}