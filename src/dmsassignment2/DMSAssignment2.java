/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package dmsassignment2;


import java.net.MalformedURLException;
import java.rmi.AccessException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import static java.util.Arrays.asList;
import java.util.logging.Level;
import java.util.logging.Logger;

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
        Registry registry = connectToRegistry();
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
    
    private static void initializeChangRoberts(Registry registry) {
        
    }
}
