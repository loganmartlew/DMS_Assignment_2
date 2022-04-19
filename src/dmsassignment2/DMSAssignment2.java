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
    
    private static boolean connected = false;

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        if (registry == null) {
            System.out.println("Failed to connect to registry");
            return;
        }
        
        CLI.commandLoop();  // Main thread execution will remain in this function until participant has left the network.
        
        System.out.println("P2P participant main method terminating.");
        // System.exit(0);
    }
    
    private static Registry connectToRegistry() {
        Registry localRegistry = null;
        
        try {
            LocateRegistry.createRegistry(1099);
        } catch (RemoteException e) {
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
    
    private static void initializeLeaderElection() throws RemoteException {
        LeaderElectionImpl remoteObject = new LeaderElectionImpl();
       
        String objectName = LeaderElectionImpl.getLeaderObjectName(PROCESS_ID);

        try {
            LeaderElection stub = (LeaderElection) 
                UnicastRemoteObject.exportObject(remoteObject, 0);

            registry.rebind(objectName, stub);
        } catch (RemoteException ex) {
            System.out.println("Failed to bind own LeaderElection object to registry");
            throw new RemoteException(ex.getMessage());
        }
        
        try {
            PeerConnections connections = getPeerConnections();
            List<String> names = connections.getIds();
            rebuildLeaderElectionNodes(names);
        } catch (RemoteException ex) {
            System.out.println("Failed to arrange LeaderElection nodes");
            throw new RemoteException(ex.getMessage());
        }
    }
    
    private static void uninitializeLeaderElection() throws RemoteException {
        try {
            registry.unbind(LeaderElectionImpl.getLeaderObjectName(PROCESS_ID));
        } catch (NotBoundException | AccessException ex) {
            System.out.println("Failed to unbind own LeaderElection object from registry");
            throw new RemoteException(ex.getMessage());
        }
        
        try {
            PeerConnections connections = getPeerConnections();
            List<String> names = connections.getIds();
            rebuildLeaderElectionNodes(names);
        } catch (RemoteException ex) {
            System.out.println("Failed to arrange LeaderElection nodes");
            throw new RemoteException(ex.getMessage());
        }
    }
    
    private static void rebuildLeaderElectionNodes(List<String> names) 
            throws RemoteException {
        List<String> creNames = new ArrayList<String>();
        List<LeaderElection> creObjects = new ArrayList<LeaderElection>();
        names.forEach(name -> creNames.add(LeaderElectionImpl.getLeaderObjectName(name)));
        creNames.forEach(name -> creObjects.add(getElectionObject(name)));
        
        for (int i = 0; i < creObjects.size(); i++) {
            if (i == creObjects.size() - 1) {
                creObjects.get(i).setNextProcess(creObjects.get(0));
                continue;
            }
            
            creObjects.get(i).setNextProcess(creObjects.get(i + 1));
        }
    }

    private static void initializeTokenTree() throws RemoteException {
        TokenTreeNodeImpl remoteObject = new TokenTreeNodeImpl(Long.toString(PROCESS_ID));

        String objectName = TokenTreeNodeImpl.getTreeObjectName(PROCESS_ID);

        try {
            TokenTreeNode stub = (TokenTreeNode) 
                UnicastRemoteObject.exportObject(remoteObject, 0);

            registry.rebind(objectName, stub);
        } catch (RemoteException ex) {
            System.out.println("Failed to bind own TokenTree object to registry");
            throw new RemoteException(ex.getMessage());
        }

        String leaderName = TokenTreeNodeImpl.getTreeObjectName(startElection(true));

        rebuildTokenTreeNodes(getElectionObject(LeaderElectionImpl.getLeaderObjectName(PROCESS_ID)));
    }

    private static void uninitializeTokenTree(LeaderElection election) throws RemoteException {
        try {
            String objectName = TokenTreeNodeImpl.getTreeObjectName(PROCESS_ID);

            registry.unbind(objectName);
        } catch (RemoteException | NotBoundException ex) {
            System.out.println("Failed to unbind own TokenTree object to registry");
            throw new RemoteException(ex.getMessage());
        }

        rebuildTokenTreeNodes(election);
    }

    private static void rebuildTokenTreeNodes(LeaderElection election) throws RemoteException {
        String leaderName = TokenTreeNodeImpl.getTreeObjectName(election.startElection());
        TokenTreeNode leader = getTreeObject(leaderName);

        if (leader == null) return;

        PeerConnections connections = getPeerConnections();
        List<String> names = connections.getIds();
        names.remove(leaderName);

        List<TokenTreeNode> nodes = new ArrayList();
        names.forEach(name -> nodes.add(getTreeObject(TokenTreeNodeImpl.getTreeObjectName(name))));
        
        leader.constructFullTree(null, nodes);
    }
    
    private static long startElection(boolean ownElectionObject) {
        try {
            LeaderElection election =
                    (LeaderElection) registry.lookup(LeaderElectionImpl.getLeaderObjectName(PROCESS_ID));
            
            return election.startElection();
        } catch (NotBoundException | RemoteException ex) {
            System.out.println("Couldnt find own election object");
        }
        
        return 0;
    }
    
    public static boolean isConnected() {
        return connected;
    }
    
    // ----- Fetch RMI Object Methods ----- \\
    
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

    private static TokenTreeNode getTreeObject(String name) {
        try {
            TokenTreeNode object = (TokenTreeNode)
                    registry.lookup(name);
            
            return object;
        } catch (RemoteException | NotBoundException ex) {
            System.out.println("Couldn't look up object with name " + name);
        }
        
        return null;
    }
    
    private static User getCurrentUser() {
       PeerConnections connections = getPeerConnections();

        try {
            User user = connections.getUserById(Long.toString(PROCESS_ID));
            return user;
        } catch (RemoteException ex) {
            System.out.println("Unable to get user from connections");
        }

        return null;
    }

    private static User getUser(String username) {
        PeerConnections connections = getPeerConnections();

        try {
            User user = connections.getUserByName(username);
            return user;
        } catch (RemoteException ex) {
            System.out.println("Unable to get user from connections");
        }

        return null;
    }
    
    // ----- User command methods ----- \\
    
    public static boolean joinNetwork(String username) {
        if(connected){
            System.out.println("User already in network.");
            return false;
        }
        
        UserImpl newUser = null;
        
        try {
            newUser = new UserImpl(Long.toString(PROCESS_ID), username);
            User stub = (User) UnicastRemoteObject.exportObject(newUser, 0);
            registry.rebind(UserImpl.getUserObjectName(PROCESS_ID), stub);
        } catch (RemoteException ex) {
            System.out.println("Failed to add user to registry");
            return false;
        }
        
        try {
            if (newUser == null) throw new RemoteException();
            
            PeerConnections connections = getPeerConnections();
            connections.addPeer((User)newUser);
        } catch (RemoteException ex) {
            System.out.println("Failed to add self to connection list");
            return false;
        }
        
        try {
            initializeLeaderElection();
            System.out.println("Added to election ring");
        } catch (RemoteException e) {
            System.out.println("LeaderElection initialization failed");
            return false;
        }

        try {
            initializeTokenTree();
            System.out.println("Added to token tree");
        } catch (RemoteException e) {
            System.out.println("TokenTree initialization failed");
            e.printStackTrace();
            return false;
        }
        
        connected = true;
        return true;
    }
    
    public static boolean leaveNetwork() {
        try {
            registry.unbind(UserImpl.getUserObjectName(PROCESS_ID));
        } catch (RemoteException | NotBoundException ex) {
            System.out.println("Failed to remove user from registry");
            return false;
        }

        try {
            PeerConnections connections = getPeerConnections();
            connections.removePeer(Long.toString(PROCESS_ID));
        } catch (RemoteException ex) {
            System.out.println("Failed to remove self from connection list");
            return false;
        }

        LeaderElection election = getElectionObject(LeaderElectionImpl.getLeaderObjectName(PROCESS_ID));

        try {
            uninitializeLeaderElection();
            System.out.println("Removed from election ring");
        } catch (RemoteException e) {
            System.out.println("LeaderElection uninitialization failed");
            return false;
        }

        try {
            uninitializeTokenTree(election.getNextProcess());
            System.out.println("Removed from token tree");
        } catch (RemoteException e) {
            System.out.println("TokenTree uninitialization failed");
            e.printStackTrace();
            return false;
        }
        
        connected = false;
        return true;
    }

    public static List<String> getUsernames() throws RemoteException {
        PeerConnections connections = getPeerConnections();
        if (connections == null) {
            throw new NullPointerException("Could not get connections");
        }

        return connections.getUsernames();
    }
    
    public static void setBio(String newBio) {
        User user = getCurrentUser();

        try {
            user.setBiography(newBio);
        } catch (RemoteException ex) {
            return;
        }
    }
    
    public static String getBio(String username) throws RemoteException, NullPointerException {
        User user = getUser(username);
        if (user == null) {
            throw new NullPointerException("User does not exist");
        }
            
        return user.getBiography();
    }
    
    public static void rateBio(String username, int rating) throws RemoteException, NullPointerException {
        TokenTreeNode node = getTreeObject(TokenTreeNodeImpl.getTreeObjectName(PROCESS_ID));
        if (node == null) {
            throw new NullPointerException("Could not find tree node");
        };

        User user = getUser(username);
        if (user == null) {
            throw new NullPointerException("Could not find user");
        };

        if(node.getToken()) {
            try {
                user.rate(rating);
            } catch (RemoteException ex) {
                return;
            }
            finally {
                node.releaseToken();
            }
        }
        else {
            System.out.println("getToken failed");
        }
    }
    
    public static int getBioRating(String username) throws RemoteException, NullPointerException {
        User user = getUser(username);
        if (user == null) {
            throw new NullPointerException("User does not exist");
        }

        return user.getBiographyRating();
    }
    
    public static String takeSnapshot(){
        // TODO: Implement snapshot algorithm
        return "";
    }
}
