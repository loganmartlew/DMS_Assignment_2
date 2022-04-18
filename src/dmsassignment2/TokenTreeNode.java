/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dmsassignment2;

import java.util.List;

/**
 *
 * @author Logan
 */
public class TokenTreeNode {
    enum TokenLocation {
        ABOVE,
        LEFT,
        RIGHT,
        HERE
    }

    public final String pid;
    
    private TokenTreeNode parent = null;
    private TokenTreeNode left = null;
    private TokenTreeNode right = null;

    private boolean usingToken = false;
    
    private TokenLocation tokenLocation = TokenLocation.ABOVE;
    
    public TokenTreeNode(String pid) {
        this.pid = pid;
    }
    
    public void constructFullTree(TokenTreeNode parent, List<TokenTreeNode> nodes) {
        if (parent == null) {
            this.tokenLocation = TokenLocation.HERE;    // Root node holds the token to start
        }   // else location remains ABOVE

        if (nodes.size() == 0) {    // this is a leaf node
            return;
        }

        if (nodes.size() == 1) {    // there is a single child
            this.left = nodes.get(0);
            this.left.constructFullTree(parent, nodes.subList(1, 1));
            return;
        }

        // if there is a left and a right node, then set left node as left child and right node as right child and pass each one half of the remaining nodes
        this.left = nodes.get(0);
        this.left.constructFullTree(this, nodes.subList(1, nodes.size() / 2 + 1));
        this.right = nodes.get(nodes.size() / 2 + 1);
        this.right.constructFullTree(this, nodes.subList(nodes.size() / 2 + 2, nodes.size()));
    }

    public synchronized boolean getToken(TokenTreeNode requester) {    
        if (this.tokenLocation == TokenLocation.HERE) {
            if (usingToken) {
                // Block the requesting node until the token is released
                try {
                    wait();
                } catch (InterruptedException ex) {
                    System.out.println("InterruptedException in getToken()");
                }
            }
        }      
        else {
            boolean success = false;

            // if the token is not here, then request the token from the token location
            if (this.tokenLocation == TokenLocation.ABOVE) {
                // if the token is above, then request the token from the parent
                success = this.parent.getToken(this);
            } 
            else if (this.tokenLocation == TokenLocation.LEFT) {
                // if the token is left, then request the token from the left child
                success = this.left.getToken(this);
            } 
            else if (this.tokenLocation == TokenLocation.RIGHT) {
                // if the token is right, then request the token from the right child
                success = this.right.getToken(this);
            }

            if (success) {
                tokenLocation = TokenLocation.HERE;
            }
            else {
                // failed to fetch the token, so return false
                return false;
            }
        }

        // TOKEN is now HERE
        if(requester == left) {
            tokenLocation = TokenLocation.LEFT;
        }
        else if(requester == right) {
            tokenLocation = TokenLocation.RIGHT;
        }
        else if (requester == parent) {
            tokenLocation = TokenLocation.ABOVE;
        }
        else {
            usingToken = true;
        }

        return true;
    }

    public void releaseToken() {
        usingToken = false;
        // notify all waiting threads
        notifyAll();
    }
}