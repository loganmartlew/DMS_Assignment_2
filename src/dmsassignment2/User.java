/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dmsassignment2;

import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 *
 * @author Brock
 */
public class User implements Remote {
    public final String id;
    public final String username;
    private String biography = "";
    private int biographyRating = 0;
    
    public User(String id, String username) throws RemoteException {
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
    
    public static String getUserObjectName(long processName) {
        return "user-" + processName;
    }
    
    public static String getUserObjectName(String processName) {
        return "user-" + processName;
    }
}