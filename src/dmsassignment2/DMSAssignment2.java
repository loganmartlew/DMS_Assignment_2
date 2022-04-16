/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package dmsassignment2;

import java.util.ArrayList;

/**
 *
 * @author logan
 */
public class DMSAssignment2 {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        System.out.println("Hello");
        
        VectorTimestamp timestamp = new VectorTimestamp(4);
        
        ArrayList<Integer> oldVector = new ArrayList();
        oldVector.add(5);
        oldVector.add(7);
        oldVector.add(54);
        oldVector.add(8);
        oldVector.add(4);
        timestamp.setVector(oldVector);
        
        System.out.println(timestamp.getVector());
        
        ArrayList<Integer> newVector = new ArrayList();
        newVector.add(3);
        newVector.add(9);
        newVector.add(55);
        newVector.add(1);
        newVector.add(1);
        
        try {
            timestamp.update(newVector);
        
            System.out.println(timestamp.getVector());
        } catch (Exception e) {}
        
    }
    
}
