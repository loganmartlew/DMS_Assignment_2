/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dmsassignment2;

/**
 *
 * @author logan
 */
public class TokenRingNode {
    private boolean hasToken = false;
    private boolean accessRequested = false;
    
    // RMI Object
    private TokenRingNode next;
    
    public TokenRingNode() {}
    
    public void setNextNode(TokenRingNode node) {
        this.next = node;
    }
    
    public void recieveToken() {
        this.hasToken = true;
        this.accessResource();
    }
    
    public void accessResource() {
        if (!accessRequested) this.sendToken();
        
        // Do something
        
        this.sendToken();
    }
    
    public void sendToken() {
        this.hasToken = false;
        this.next.recieveToken();
    }
}
