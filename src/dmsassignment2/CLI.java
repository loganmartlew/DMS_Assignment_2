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
    
    public static String getIp() {
        System.out.println("Enter IP Adress of P2P Network");
        System.out.print(">");
        return scan.nextLine();
    }
    
    public static void commandLoop() {
        System.out.println("Available commands:\n\tleave\n\t"); // TODO: Add other options for utilising the P2P network
        
        while(true){
            System.out.print(">");
            String cmd = scan.nextLine().toLowerCase().strip(); // Fetch input ignorning case and white space
            
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
                    
                default -> {
                    System.out.println("Command \"" + cmd + "\" not recognised.");
                }
                    
            }
        }
        
    }
}
