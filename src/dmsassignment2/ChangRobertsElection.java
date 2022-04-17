/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dmsassignment2;

/**
 *
 * @author Logan
 */
public class ChangRobertsElection {
    private static final long ID = ProcessHandle.current().pid();
    
    private ChangRobertsElection nextProcess;
    private long leaderProcessID;
    
    private boolean participant = false;
    
    public ChangRobertsElection(ChangRobertsElection nextProcess) {
        this.nextProcess = nextProcess;
    }
    
    public void startElection() {
        this.participant = true;
        this.nextProcess.recieveCandidate(ID);
    }
    
    public void recieveCandidate(long candidate) {
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
    
    public void recieveLeader(long leader) {
        this.leaderProcessID = leader;
        this.participant = false;
        
        if (leader != ID) {
            this.nextProcess.recieveLeader(leader);
        }
    }
}
