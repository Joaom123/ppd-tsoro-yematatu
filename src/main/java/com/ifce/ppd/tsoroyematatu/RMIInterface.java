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
     * @throws RemoteException When there is a failure to connect to the server.
     */
    void createClient(Client client, String roomId, ClientCallback clientCallback) throws RemoteException;

    void messageToRival(String message, Client client) throws RemoteException;

    void move(String pieceId, String pointId, Client client) throws RemoteException;

    void exit(Client client) throws RemoteException;

    void withdrawal(Client client) throws RemoteException;

    void draw(Client client) throws RemoteException;

    void drawDenied(Client client) throws RemoteException;

    void drawAccepted(Client client) throws RemoteException;
}
