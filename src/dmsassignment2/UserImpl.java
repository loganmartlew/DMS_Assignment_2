/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dmsassignment2;

import java.io.Serializable;
import java.rmi.RemoteException;

/**
 *
 * @author Brock
 */
public class UserImpl implements User, Serializable {
    private final String id;
    private final String username;
    private String biography = "";
    private int biographyRating = 0;
    
    public UserImpl(String id, String username) throws RemoteException {
        this.id = id;
        this.username = username;
    }
    
    public String getBiography() throws RemoteException {
        return biography;
    }
    
    public void setBiography(String biography) throws RemoteException {
        this.biography = biography;
    }

    public void rate(int rating) throws RemoteException {
        biographyRating += rating;
    }
    
    public int getBiographyRating() throws RemoteException {
        return biographyRating;
    }

    public String getId() throws RemoteException {
        return id;
    }

    public String getUsername() throws RemoteException {
        return username;
    }
    
    public static String getUserObjectName(long processName) {
        return "user-" + processName;
    }
    
    public static String getUserObjectName(String processName) {
        return "user-" + processName;
    }
}