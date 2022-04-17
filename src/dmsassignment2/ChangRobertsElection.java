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
public interface ChangRobertsElection extends Remote {
    public void setNextProcess(String nextProcessName) throws RemoteException;
    public void startElection() throws RemoteException;
    public void recieveCandidate(long candidate) throws RemoteException;
    public void recieveLeader(long leader) throws RemoteException;
}
