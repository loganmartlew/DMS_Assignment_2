/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package dmsassignment2;

import java.io.Serializable;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

/**
 *
 * @author Logan
 */
public interface TokenTreeNode extends Remote, Serializable {
    public void constructFullTree(TokenTreeNode parent, List<TokenTreeNode> nodes) throws RemoteException;
    public boolean getToken() throws RemoteException;
    public boolean getToken(TokenTreeNode requester) throws RemoteException;
    public void releaseToken() throws RemoteException;
}
