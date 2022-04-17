/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dmsassignment2;

import java.io.Serializable;
import java.rmi.AccessException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Logan
 */
public class ChangRobertsElectionImpl implements ChangRobertsElection, Serializable {
    private static final long ID = ProcessHandle.current().pid();
    
    private String nextProcessName;
    private long leaderProcessID;
    
    private boolean participant = false;
    
    private Registry registry = null;
    
    public ChangRobertsElectionImpl() {}
    
    public void setNextProcess(String nextProcess) throws RemoteException {
        this.nextProcessName = nextProcess;
    }
    
    private void setRegistry() throws RemoteException {
        try {
            LocateRegistry.createRegistry(1099);
        } catch (RemoteException e) {
        }
        
        Registry newRegistry = LocateRegistry.getRegistry();
        
        this.registry = newRegistry;
    }
    
    private ChangRobertsElection getNextProcess() throws RemoteException {
        this.setRegistry();
        
        try {
            ChangRobertsElection nextProcess =
                    (ChangRobertsElection) this.registry.lookup(this.nextProcessName);
            
            return nextProcess;
        } catch (NotBoundException | AccessException ex) {
            System.out.println("Error looking up next process");
        }
        
        return null;
    }
    
    public void startElection() throws RemoteException {
        this.participant = true;
        this.getNextProcess().recieveCandidate(ID);
    }
    
    public void recieveCandidate(long candidate) throws RemoteException {
        if (candidate > ID) {
            System.out.println("Candidate is better");
            this.participant = true;
            this.getNextProcess().recieveCandidate(candidate);
            
        } else if (candidate == ID) {
            System.out.println("I won");
            this.getNextProcess().recieveLeader(ID);
            
        } else if (candidate < ID) {
            if (!this.participant) {
                this.startElection();
            }
        }
    }
    
    public void recieveLeader(long leader) throws RemoteException {
        this.leaderProcessID = leader;
        this.participant = false;
        System.out.println("Leader: " + leader);
        
        if (leader != ID) {
            this.getNextProcess().recieveLeader(leader);
        }
    }
}
