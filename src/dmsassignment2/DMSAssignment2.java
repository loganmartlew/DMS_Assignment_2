/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package dmsassignment2;


import java.rmi.AccessException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import  java.util.Arrays;
import java.util.List;

/**
 *
 * @author logan
 */
public class DMSAssignment2 {
    
    private static final long PROCESS_ID = ProcessHandle.current().pid();
    
    private static Registry registry = connectToRegistry();

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        if (registry == null) {
            System.out.println("Failed to connect to registry");
            return;
        }
        
        joinNetwork();
        CLI.commandLoop();  // Main thread execution will remain in this function until participant has left the network.
        
        System.out.println("P2P participant main method terminating.");
    }
    
    private static Registry connectToRegistry() {
        Registry localRegistry = null;
        
        try {
            LocateRegistry.createRegistry(1099);
        } catch (RemoteException e) {
            System.out.println("Can't create a new registry");
        }
        
        try {
            localRegistry = LocateRegistry.getRegistry();
            
            // Ensures a PeerConnections object is in registry
            if (!Arrays.asList(localRegistry.list()).contains(PeerConnectionsImpl.NAME)) {
                PeerConnectionsImpl remoteObject = new PeerConnectionsImpl();
                
                PeerConnections stub = 
                    (PeerConnections) UnicastRemoteObject.exportObject(remoteObject, 0);
                localRegistry.rebind(PeerConnectionsImpl.NAME, stub);
            }
        } catch (RemoteException e) {
            System.err.println("Unable to bind to registry: " + e);
        }
        
        return localRegistry;
    }
    
    public static boolean joinNetwork() {
        try {
            PeerConnections connections = getPeerConnections();
            connections.addPeer(Long.toString(PROCESS_ID));
        } catch (RemoteException ex) {
            System.out.println("Failed to add self to connection list");
            return false;
        }
        
        try {
            initializeLeaderElection();
            System.out.println("Added to election ring");
        } catch(RemoteException e) {
            System.out.println("Could not initialize LeaderElection object");
            return false;
        }
        
        return true;
    }
    
    public static boolean leaveNetwork() throws RemoteException {
        PeerConnections connections = getPeerConnections();
        if (connections == null) return false;
        
        connections.removePeer(Long.toString(PROCESS_ID));
        
        try {
            registry.unbind(LeaderElectionImpl.getLeaderObjectName(PROCESS_ID));
        } catch (NotBoundException | AccessException ex) {
            return false;
        }
        
        List<String> names = connections.getPeers();
        rebuildLeaderElectionNodes(names);
        
        return true;
    }
    
    private static void startElection() {
        try {
            LeaderElection election =
                    (LeaderElection) registry.lookup(LeaderElectionImpl.getLeaderObjectName(PROCESS_ID));
            
            election.startElection();
        } catch (NotBoundException | RemoteException ex) {
            System.out.println("Couldnt find own election object");
        }
    }
    
    private static void initializeLeaderElection() throws RemoteException {
        LeaderElectionImpl remoteObject = new LeaderElectionImpl();
       
        String objectName = LeaderElectionImpl.getLeaderObjectName(PROCESS_ID);
       
        PeerConnections connections = getPeerConnections();
        if (connections == null) return;
        
        List<String> names = connections.getPeers();

        try {
            LeaderElection stub = (LeaderElection) 
                UnicastRemoteObject.exportObject(remoteObject, 0);

            registry.rebind(objectName, stub);
        } catch (RemoteException ex) {
            System.out.println("Failed to bind own LeaderElection object to registry");
            return;
        }
        
        try {
            rebuildLeaderElectionNodes(names);
        } catch (RemoteException ex) {
            System.out.println("Failed to arrange LeaderElection nodes");
        }
    }
    
    private static void rebuildLeaderElectionNodes(List<String> names) 
            throws RemoteException {
        List<String> creNames = new ArrayList();
        List<LeaderElection> creObjects = new ArrayList();
        names.forEach(name -> creNames.add(LeaderElectionImpl.getLeaderObjectName(name)));
        creNames.forEach(name -> creObjects.add(getElectionObject(name)));
        
        for (int i = 0; i < creObjects.size(); i++) {
            if (i == creObjects.size() - 1) {
                creObjects.get(i).setNextProcess(creObjects.get(0));
                System.out.println(creNames.get(i) + " > " + creNames.get(0));
                continue;
            }
            
            creObjects.get(i).setNextProcess(creObjects.get(i + 1));
            System.out.println(creNames.get(i) + " > " + creNames.get(i + 1));
        }
    }
    
    private static PeerConnections getPeerConnections() {
        PeerConnections connections = null;
        
        try {
            connections = (PeerConnections) registry.lookup(PeerConnectionsImpl.NAME);
        } catch (NotBoundException | RemoteException ex) {
            System.out.println("Could not find PeerConnections object in registry");
        }
        
        return connections;
    }
    
    private static LeaderElection getElectionObject(String name) {
        try {
            LeaderElection object = (LeaderElection)
                    registry.lookup(name);
            
            return object;
        } catch (RemoteException | NotBoundException ex) {
            System.out.println("Couldn't look up object with name " + name);
        }
        
        return null;
    }
    
    public static String getBio(String fetchUsername){
        // TODO: Implement RMI get user biography
        return "";
    }
    
    public static void rateBio(String username, int rating){
        // TODO: Implement token ring syncronised rate biography with +1 or -1
    }
    
    public static String takeSnapshot(){
        // TODO: Implement snapshot algorithm
        return "";
    }
}
