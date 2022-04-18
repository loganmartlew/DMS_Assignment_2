/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dmsassignment2;

/**
 *
 * @author Brock
 */
public class User {
    public final String username;
    private String biography = "";
    private int biographyRating = 0;
    
    public User(String username) {
        this.username = username;
    }
    
    public String getBiography() {
        return biography;
    }
    
    public void setBiography(String biography) {
        this.biography = biography;
    }

    public void rate(int rating) {
        biographyRating += rating;
    }
    
}