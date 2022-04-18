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
    private List<String> peers;
    public static final String NAME = "connections";
    
    public PeerConnectionsImpl() {
        this.peers = new ArrayList();
    }
    
    public void addPeer(String name) {
        this.peers.add(name);
    }
    
    public List<String> getPeers() {
        return this.peers;
    }
}
