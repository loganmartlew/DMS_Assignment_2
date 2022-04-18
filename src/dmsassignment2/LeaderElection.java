/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package dmsassignment2;

import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 *
 * @author Logan
 */
public interface LeaderElection extends Remote {
    public void setNextProcess(LeaderElection nextProcess) throws RemoteException;
    public LeaderElection getNextProcess() throws RemoteException;
    public long startElection() throws RemoteException;
    public void recieveCandidate(long candidate) throws RemoteException;
    public void recieveLeader(long leader) throws RemoteException;
}
