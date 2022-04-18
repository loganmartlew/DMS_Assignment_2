/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dmsassignment2;

import java.util.Scanner;

import javax.sound.sampled.SourceDataLine;

/**
 *
 * @author Logan
 */
public class CLI {
    private static final Scanner scan = new Scanner(System.in);
    private static final String[] COMMANDS = {"join", "leave", "update bio", "get bio", "rate bio", "get bio rating", "snapshot"};
    
    public static String getIp() {
        System.out.println("Enter IP Adress of P2P Network");
        System.out.print(">");
        return scan.nextLine().strip();
    }
    
    private static String getUsername(){
        String name;
        System.out.print("Username: ");
        name = scan.nextLine().strip();
        
        return name;
    }
    
    public static void commandLoop() {
        System.out.println("Available commands:"); // TODO: Add other options for utilising the P2P network
        for(String command: COMMANDS){
            System.out.println("\t" + command);
        }
        
        String cmd;
        while(true){
            System.out.print(">");
            cmd = scan.nextLine().strip().toLowerCase(); // Fetch input ignorning case and white space
            
            switch(cmd) {
                case "join" -> {
                    String username = getUsername();
                    if(DMSAssignment2.joinNetwork(username)) {
                        System.out.println("Successfuly joined the P2P network.");
                    } else {
                        System.out.println("Failed to join P2P network.");
                    }
                }
                
                case "leave" -> {
                    if (!DMSAssignment2.isConnected()) {
                        System.out.println("Cannot perform this action when not connected to P2P network.");
                        break;
                    }
                    
                    if(DMSAssignment2.leaveNetwork()) {
                        System.out.println("Successfully left the P2P network.");
                        return; // exit commandLoop
                    }
                    else {
                        System.out.println("Failed to leave P2P network.");
                    }
                }
                
                case "update bio" -> {
                    if (!DMSAssignment2.isConnected()) {
                        System.out.println("Cannot perform this action when not connected to P2P network.");
                        break;
                    }
                    
                    System.out.print("Enter new bio: ");
                    String newBio = scan.nextLine().strip();
                    DMSAssignment2.setBio(newBio);
                }
                
                case "snapshot" -> {
                    if (!DMSAssignment2.isConnected()) {
                        System.out.println("Cannot perform this action when not connected to P2P network.");
                        break;
                    }
                    
                    System.out.println(DMSAssignment2.takeSnapshot());
                    
                }
                
                case "get bio" -> {
                    if (!DMSAssignment2.isConnected()) {
                        System.out.println("Cannot perform this action when not connected to P2P network.");
                        break;
                    }
                    
                    try {
                        System.out.println(DMSAssignment2.getBio(getUsername()));
                    }
                    catch(Exception e) {
                        System.out.println("User not found");
                    }
                }
                
                case "get bio rating" -> {
                    if (!DMSAssignment2.isConnected()) {
                        System.out.println("Cannot perform this action when not connected to P2P network.");
                        break;
                    }

                    String fetchUsername = getUsername();

                    try{
                        System.out.println(DMSAssignment2.getBioRating(fetchUsername));
                    }
                    catch(Exception e) {
                        System.out.println("User not found");
                    }
                }
                
                case "rate bio" -> {
                    if (!DMSAssignment2.isConnected()) {
                        System.out.println("Cannot perform this action when not connected to P2P network.");
                        break;
                    }
                    
                    String username = getUsername();
                    System.out.print("Like[L], Dislike[D], cancel[ENTER]: ");
                    String action = scan.nextLine().strip().toLowerCase();
                    
                    // Switch on chosen action
                    try {
                        switch(action){
                            case "l" -> {
                                DMSAssignment2.rateBio(username, 1);
                                System.out.println("Liked " + username + "'s biography.");
                            }
                            case "d" -> {
                                DMSAssignment2.rateBio(username, -1);
                                System.out.println("Disliked " + username + "'s biography.");
                            }
                            default -> {
                                System.out.println("Cancelled.");
                            }
                        }
                    }
                    catch(Exception e) {
                        System.out.println("User not found" + e);
                    }

                }
                    
                default -> {
                    System.out.println("Unrecognised command: \"" + cmd + "\".");
                }
                    
            }
        }
        
    }
}
