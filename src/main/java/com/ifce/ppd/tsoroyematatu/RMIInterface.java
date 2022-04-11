package com.ifce.ppd.tsoroyematatu;
import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * The client can execute a function on the server-side.
 * Each function must send the client's info, used by the server to identify the player.
 */
public interface RMIInterface extends Remote {
    /**
     * @param client The client's model.
     * @param roomId The room's id.
     * @throws RemoteException When there is a failure to connect to the server.
     */
    void createClient(Client client, String roomId, ClientCallback clientCallback) throws RemoteException;

    /**
     * @param message The message to be sent.
     * @param client The message's owner.
     * @throws RemoteException When there is a failure to connect to the server.
     */
    void messageToRival(String message, Client client) throws RemoteException;

    /**
     * @param pieceId Piece to be moved.
     * @param pointId The destiny of the piece.
     * @param client The move's owner.
     * @throws RemoteException When there is a failure to connect to the server.
     */
    void move(String pieceId, String pointId, Client client) throws RemoteException;

    /**
     * @param client Client who wants to quit.
     * @throws RemoteException When there is a failure to connect to the server.
     */
    void exit(Client client) throws RemoteException;

    /**
     * @param client Client who wants to withdrawal.
     * @throws RemoteException When there is a failure to connect to the server.
     */
    void withdrawal(Client client) throws RemoteException;

    /**
     * @param client Client who wants to draw.
     * @throws RemoteException When there is a failure to connect to the server.
     */
    void draw(Client client) throws RemoteException;

    /**
     * @param client The client who denied the draw.
     * @throws RemoteException When there is a failure to connect to the server.
     */
    void drawDenied(Client client) throws RemoteException;

    /**
     *
     * @param client The client who accepted the draw.
     * @throws RemoteException When there is a failure to connect to the server.
     */
    void drawAccepted(Client client) throws RemoteException;
}
