/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dmsassignment2;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Logan
 */
public class PeerConnectionsImpl implements PeerConnections {
    private List<User> peers;
    public static final String NAME = "connections";
    
    public PeerConnectionsImpl() {
        this.peers = new ArrayList();
    }
    
    public void addPeer(User user) {
        this.peers.add(user);
    }
    
    public void removePeer(String id) {
        this.peers.remove(this.getUserById(id));
    }
    
    public List<User> getPeers() {
        return this.peers;
    }
    
    public List<String> getNames() {
        List<String> names = new ArrayList();
        
        this.peers.forEach(user -> names.add(user.id));
        
        return names;
    }
    
    public User getUserByName(String name) {
        for (User user : peers) {
            if (user.username.equals(name)) {
                return user;
            }
        }
        return null;
    }
    
    public User getUserById(String id) {
        for (User user : peers) {
            if (user.id.equals(id)) {
                return user;
            }
        }
        return null;
    }
}
