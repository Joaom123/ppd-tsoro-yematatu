package com.ifce.ppd.tsoroyematatu.models;

import java.io.Serializable;

public class Client implements Serializable {
    private final String name;
    private int id;
    private String roomId;

    public Client(String name) {
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }
}