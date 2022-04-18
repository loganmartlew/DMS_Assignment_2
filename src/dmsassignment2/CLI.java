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
    private static Scanner scan = new Scanner(System.in);
    private static final String[] COMMANDS = {"leave", "snapshot"};
    
    public static String getIp() {
        System.out.println("Enter IP Adress of P2P Network");
        System.out.print(">");
        return scan.nextLine();
    }
    
    public static void commandLoop() {
        System.out.println("Available commands:"); // TODO: Add other options for utilising the P2P network
        for(String command: COMMANDS){
            System.out.println("\t" + command);
        }
        
        String cmd;
        while(true){
            System.out.print(">");
            cmd = scan.nextLine().toLowerCase().strip(); // Fetch input ignorning case and white space
            
            switch(cmd){
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
                    
                default -> {
                    System.out.println("Command \"" + cmd + "\" not recognised.");
                }
                    
            }
        }
        
    }
}
