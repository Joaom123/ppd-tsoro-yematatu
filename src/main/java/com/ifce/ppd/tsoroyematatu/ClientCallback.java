package com.ifce.ppd.tsoroyematatu;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface ClientCallback extends Remote {
    void ping() throws RemoteException;
}
