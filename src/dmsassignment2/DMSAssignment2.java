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
import java.util.Arrays;
import static java.util.Arrays.asList;
import java.util.List;
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
        
        try {
            initializeChangRoberts(registry);
        } catch(RemoteException e) {
            System.out.println("Could not initialize ChangRobertsElection object");
            e.printStackTrace();
        }
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
       
        System.out.println("");
       
        String objectName = getCreName(processID);
       
        PeerConnections connections = null;
        
        try {
            connections = (PeerConnections) registry.lookup(PeerConnectionsImpl.NAME);
        } catch (NotBoundException | AccessException ex) {
            System.out.println("Could not find PeerConnections object in registry");
        }
        
        if (connections == null) return;
        
        List<String> names = connections.getPeers();
        System.out.println(names);

        names.remove(Long.toString(processID));
        System.out.println(names);
        System.out.println("");

        try {
            if (names.size() < 1) {
                remoteObject.setNextProcess(remoteObject);
            }

            if (names.size() == 1) {
                System.out.println("NAMES SIZE 1");
                System.out.println("Names:");
                System.out.println(names);
                System.out.println("Name: " + names.get(0));
                System.out.println("Registry Names:");
                System.out.println(Arrays.toString(registry.list()));
                System.out.println("CRE Name: " + getCreName(names.get(0)));
                
                ChangRobertsElection otherObject = 
                    (ChangRobertsElection) registry.lookup(getCreName(names.get(0)));
                
                otherObject.setNextProcess(remoteObject);
                remoteObject.setNextProcess(otherObject);
            }

            if (names.size() > 1) {
                ChangRobertsElection object0 = 
                    (ChangRobertsElection) registry.lookup(getCreName(names.get(0)));
                ChangRobertsElection object1 = 
                    (ChangRobertsElection) registry.lookup(getCreName(names.get(1)));

                object0.setNextProcess(remoteObject);
                remoteObject.setNextProcess(object1);
            }
        } catch(NotBoundException | AccessException ex) {
            System.out.println("Error looking up remote ChangRoberts objects");
            System.out.println(ex.getMessage());
            ex.printStackTrace();
        }

        ChangRobertsElection stub = (ChangRobertsElection) 
            UnicastRemoteObject.exportObject(remoteObject, 0);

        registry.rebind(getCreName(processID), stub);

        System.out.println("Added to election ring");
    }
    
    private static String getCreName(long processName) {
        return "cre" + processName;
    }
    
    private static String getCreName(String processName) {
        return "cre" + processName;
    }
}
