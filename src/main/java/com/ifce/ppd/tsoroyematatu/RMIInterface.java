package com.ifce.ppd.tsoroyematatu;
import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 *
 */
public interface RMIInterface extends Remote {
    /**
     * @param client The client's model.
     * @param roomId The room's id.
     * @return ROOM_IS_FULL, WAIT_RIVAL_CONNECT or PLAYABLE
     * @throws RemoteException When there is a failure to connect to the server.
     */
    MESSAGE_TYPES createClient(Client client, String roomId, ClientCallback clientCallback) throws RemoteException;

    void receiveMessageFromClient(String message, Client client) throws RemoteException;

    void move(String pieceId, String pointId) throws RemoteException;

    void exit() throws RemoteException;

    void withdrawal() throws RemoteException;

    void draw() throws RemoteException;

    void drawDenied() throws RemoteException;

    void drawAccepted() throws RemoteException;
}
