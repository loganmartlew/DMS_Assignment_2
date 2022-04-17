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
        System.out.println("> ");
        return scan.nextLine();
    }
}
