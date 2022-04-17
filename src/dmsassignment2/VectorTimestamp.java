/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dmsassignment2;

import java.util.ArrayList;

/**
 *
 * @author Logan
 */
public class VectorTimestamp {
    private int index;
    private ArrayList<Integer> vector;
    
    public VectorTimestamp(int index) {
        this.index = index;
        this.vector = new ArrayList();
        
        for (int i = 0; i <= index; i++) {
            this.vector.add(0);
        }
    }
    
    public void addMember() {
        this.vector.add(0);
    }
    
    public void eventOccurred() {
        this.vector.set(this.index, this.vector.get(this.index) + 1);
    }
    
    public ArrayList<Integer> getVector() {
        return this.vector;
    }
    
    // For testing purposes
    public void setVector(ArrayList<Integer> vector) {
        this.vector = vector;
    }
    
    public void update(ArrayList<Integer> recievedVector) throws Exception {
        if (recievedVector.size() != this.vector.size()) {
            throw new Exception("Vector timestamps were not of the same size");
        }
        
        for (int i = 0; i < recievedVector.size(); i++) {
            if (recievedVector.get(i) > this.vector.get(i)) {
                this.vector.set(i, recievedVector.get(i));
            }
        }
    }
}
