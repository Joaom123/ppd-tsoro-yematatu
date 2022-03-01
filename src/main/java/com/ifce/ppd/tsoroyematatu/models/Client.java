package com.ifce.ppd.tsoroyematatu.models;

import java.io.Serializable;

public class Client implements Serializable {
    /**
     * The name of the client.
     */
    private final String name;
    /**
     * The id of the client.
     */
    private int id;

    public Client(String name) {
        this.name = name;
    }

    /**
     * ID's getter.
     *
     * @return The id of the client.
     */
    public int getId() {
        return id;
    }

    /**
     * Id's setter.
     *
     * @param id ID to be set.
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * Name's getter.
     *
     * @return The name of the client.
     */
    public String getName() {
        return name;
    }
}