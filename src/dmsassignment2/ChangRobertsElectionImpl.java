/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dmsassignment2;

import java.rmi.RemoteException;

/**
 *
 * @author Logan
 */
public class ChangRobertsElectionImpl implements ChangRobertsElection {
    private static final long ID = ProcessHandle.current().pid();
    
    private ChangRobertsElection nextProcess;
    private long leaderProcessID;
    
    private boolean participant = false;
    
    public ChangRobertsElectionImpl() {}
    
    public ChangRobertsElectionImpl(ChangRobertsElectionImpl nextProcess) {
        this.nextProcess = nextProcess;
    }
    
    public void setNextProcess(ChangRobertsElection nextProcess) throws RemoteException {
        this.nextProcess = nextProcess;
    }
    
    public void startElection() throws RemoteException {
        this.participant = true;
        this.nextProcess.recieveCandidate(ID);
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
        System.out.println("Leader: " + leader);
        
        if (leader != ID) {
            this.nextProcess.recieveLeader(leader);
        }
    }
}
