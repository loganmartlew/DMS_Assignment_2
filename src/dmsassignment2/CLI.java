/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dmsassignment2;

import java.util.Scanner;

/**
 *
 * @author Logan
 */
public class CLI {
    private static final Scanner scan = new Scanner(System.in);
    private static final String[] COMMANDS = {"leave", "snapshot", "get bio", "rate bio"};
    
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
                case "leave" -> {
                    if(DMSAssignment2.leaveNetwork()) {
                        System.out.println("Successfully left the P2P network.");
                        return;
                    }
                    else {
                        System.out.println("Failed to leave P2P network.");
                    }
                }
                
                case "snapshot" -> {
                    System.out.println(DMSAssignment2.takeSnapshot());
                }
                
                case "get bio" -> {
                    System.out.println(DMSAssignment2.getBio(getUsername()));
                }
                
                case "rate bio" -> {
                    
                    String username = getUsername();
                    System.out.println("Like[l], Dislike[d], cancel[ENTER]");
                    String action = scan.nextLine().strip().toLowerCase();
                    
                    // Switch on chosen action
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
                    
                default -> {
                    System.out.println("Command \"" + cmd + "\" not recognised.");
                }
                    
            }
        }
        
    }
}
