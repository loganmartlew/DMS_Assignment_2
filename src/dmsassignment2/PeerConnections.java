/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package dmsassignment2;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

/**
 *
 * @author Logan
 */
public interface PeerConnections extends Remote {
    public void addPeer(User user) throws RemoteException;
    public void removePeer(String id) throws RemoteException;
    public List<User> getPeers() throws RemoteException;
    public List<String> getNames() throws RemoteException;
}
