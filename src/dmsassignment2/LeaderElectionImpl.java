/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dmsassignment2;

import java.io.Serializable;
import java.rmi.RemoteException;

/**
 *
 * @author Logan
 */
public class LeaderElectionImpl implements LeaderElection, Serializable {
    private static final long ID = ProcessHandle.current().pid();
    
    private LeaderElection nextProcess;
    private long leaderProcessID;
    
    private boolean participant = false;
    
    public LeaderElectionImpl() {}
    
    public LeaderElectionImpl(LeaderElectionImpl nextProcess) {
        this.nextProcess = nextProcess;
    }
    
    public void setNextProcess(LeaderElection nextProcess) throws RemoteException {
        this.nextProcess = nextProcess;
    }
    
    public long startElection() throws RemoteException {
        this.participant = true;
        this.nextProcess.recieveCandidate(ID);
        return leaderProcessID;
    }
    
    public void recieveCandidate(long candidate) throws RemoteException {
        if (candidate > ID) {
            
            this.participant = true;
            this.nextProcess.recieveCandidate(candidate);
            
        } else if (candidate == ID) {
            
            this.nextProcess.recieveLeader(ID);
            
        } else if (candidate < ID) {
            if (!this.participant) {
                this.startElection();
            }
        }
    }
    
    public void recieveLeader(long leader) throws RemoteException {
        this.leaderProcessID = leader;
        this.participant = false;
        
        if (leader != ID) {
            this.nextProcess.recieveLeader(leader);
        }
    }
    
    public static String getLeaderObjectName(long processName) {
        return "election-node-" + processName;
    }
    
    public static String getLeaderObjectName(String processName) {
        return "election-node-" + processName;
    }
}
