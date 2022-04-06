package com.ifce.ppd.tsoroyematatu;
import java.rmi.Remote;
import java.rmi.RemoteException;
public interface RMIInterface extends Remote {
    /**
     *
     * @param client
     * @param roomId
     * @return ROOM_IS_FULL, WAIT_RIVAL_CONNECT or PLAYABLE
     * @throws RemoteException
     */
    MESSAGE_TYPES createClient(Client client, String roomId) throws RemoteException;

    void receiveMessageFromClient(String message) throws RemoteException;

    void register(ClientCallback client) throws RemoteException;
}
