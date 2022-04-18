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
public interface User extends Remote {
    public String getBiography() throws RemoteException;
    public void setBiography(String biography) throws RemoteException;
    public void rate(int rating) throws RemoteException;
    public int getBiographyRating() throws RemoteException;
    public String getId() throws RemoteException;
    public String getUsername() throws RemoteException;
}