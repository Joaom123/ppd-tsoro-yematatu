package com.ifce.ppd.tsoroyematatu.server;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;

/**
 * Handles the connection for each connected client, so the server
 * can handle multiple clients at the same time
 */
public class PlayerThread implements Runnable {
    private Server server;
    private Socket socket;

    public PlayerThread(Server server, Socket socket) {
        this.socket = socket;
        this.server = server;
    }

    public void run() {
        while (true) {

        }
    }
}
