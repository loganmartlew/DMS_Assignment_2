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
import static java.util.Arrays.asList;
import java.util.List;

/**
 *
 * @author logan
 */
public class DMSAssignment2 {
    
    private static final long processID = ProcessHandle.current().pid();

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        
        joinNetwork();
        CLI.commandLoop();  // Main thread execution will remain in this function until participant has left the network.
        
        System.out.println("P2P participant main method terminating.");
    }
    
    public static boolean joinNetwork() {
        // String ip = CLI.getIp()
        Registry registry = connectToRegistry();
        
        try {
            initializeChangRoberts(registry);
        } catch(RemoteException e) {
            System.out.println("Could not initialize ChangRobertsElection object");
            e.printStackTrace();
            return false;
        }
        
        try {
            startElection(registry);
        } catch (RemoteException ex) {
            System.out.println("Couldn't run election");
        }
        
        return true;
    }
    
    public static boolean leaveNetwork() {
        // TODO: Implement leaving of network
        return true;
    }
    
    public static String takeSnapshot(){
        // TODO: Implement snapshot algorithm
    }
    
    public static String getBio(String fetchUsername){
        // TODO: Implement RMI get user biography
    }
    
    public static void rateBio(String username, int rating){
        // TODO: Implement token ring syncronised rate biography with +1 or -1
    }

    private static Registry connectToRegistry() {
        Registry registry;
        
        try {
            LocateRegistry.createRegistry(1099);
        } catch (RemoteException e) {
            System.out.println("Can't create a new registry");
        }
        
        try {
            registry = LocateRegistry.getRegistry();
            
            // Ensures a PeerConnections object is in registry
            if (!asList(registry.list()).contains(PeerConnectionsImpl.NAME)) {
                PeerConnectionsImpl remoteObject = new PeerConnectionsImpl();
                
                PeerConnections stub = 
                    (PeerConnections) UnicastRemoteObject.exportObject(remoteObject, 0);
                registry.rebind(PeerConnectionsImpl.NAME, stub);
            }
            
            PeerConnections connections;
            try {
                connections = (PeerConnections) registry.lookup(PeerConnectionsImpl.NAME);
                connections.addPeer(Long.toString(processID));
                
                System.out.println("Connected with process id: " + processID);
                System.out.println(connections.getPeers());
            } catch (NotBoundException ex) {
                System.out.println("PeerConnections object not found");
            } catch (AccessException ex) {
                System.out.println(ex.getMessage());
            }
            
            return registry;
        } catch (RemoteException e) {
            System.err.println("Unable to bind to registry: " + e);
        }
        
        return null;
    }
    
    private static void startElection(Registry registry) throws RemoteException {
        try {
            ChangRobertsElection election =
                    (ChangRobertsElection) registry.lookup(getCreName(processID));
            
            election.startElection();
        } catch (NotBoundException | AccessException ex) {
            System.out.println("Couldnt find object");
        }
    }
    
    private static void initializeChangRoberts(Registry registry) throws RemoteException {
        ChangRobertsElectionImpl remoteObject = new ChangRobertsElectionImpl();
       
        String objectName = getCreName(processID);
       
        PeerConnections connections = null;
        
        try {
            connections = (PeerConnections) registry.lookup(PeerConnectionsImpl.NAME);
        } catch (NotBoundException | AccessException ex) {
            System.out.println("Could not find PeerConnections object in registry");
        }
        
        if (connections == null) return;
        
        List<String> names = connections.getPeers();

        ChangRobertsElection stub = (ChangRobertsElection) 
            UnicastRemoteObject.exportObject(remoteObject, 0);

        registry.rebind(objectName, stub);
        
        arrangeChangRobertsNodes(names, registry);

        System.out.println("Added to election ring");
    }
    
    private static void arrangeChangRobertsNodes(List<String> names, Registry registry) 
            throws RemoteException {
        List<String> creNames = new ArrayList();
        List<ChangRobertsElection> creObjects = new ArrayList();
        names.forEach(name -> creNames.add(getCreName(name)));
        creNames.forEach(name -> creObjects.add(getElectionObject(name, registry)));
        
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
    
    private static ChangRobertsElection getElectionObject(String name, Registry registry) {
        try {
            ChangRobertsElection object = (ChangRobertsElection)
                    registry.lookup(name);
            
            return object;
        } catch (RemoteException | NotBoundException ex) {
            System.out.println("Couldn't look up object with name " + name);
        }
        
        return null;
    }
    
    private static String getCreName(long processName) {
        return "cre" + processName;
    }
    
    private static String getCreName(String processName) {
        return "cre" + processName;
    }
}
