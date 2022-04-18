/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dmsassignment2;

import java.util.ArrayList;
import java.util.List;
import java.rmi.RemoteException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Logan
 */
public class PeerConnectionsImpl implements PeerConnections {
    private List<User> peers;
    public static final String NAME = "connections";
    
    public PeerConnectionsImpl() {
        this.peers = new ArrayList<User>();
    }
    
    public void addPeer(User user) {
        this.peers.add(user);
    }
    
    public void removePeer(String id) throws RemoteException {
        this.peers.remove(this.getUserById(id));
    }
    
    public List<User> getPeers() {
        return this.peers;
    }
    
    public List<String> getNames() throws RemoteException {
        List<String> names = new ArrayList<String>();
        
        this.peers.forEach(user -> {
            try {
                names.add(user.getId());
            } catch (RemoteException ex) {
                System.out.println("Error accessing names");
            }
        });
        
        return names;
    }
    
    public User getUserByName(String name) throws RemoteException {
        for (User user : peers) {
            if (user.getUsername().equals(name)) {
                return user;
            }
        }
        return null;
    }
    
    public User getUserById(String id) throws RemoteException {
        for (User user : peers) {
            if (user.getId().equals(id)) {
                return user;
            }
        }
        return null;
    }
}
