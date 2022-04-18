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
    
    private TokenLocation tokenLocation = TokenLocation.ABOVE;
    private boolean accessRequested = false;
    
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

    public synchronized boolean requestToken(TokenTreeNode requester) {
        if (accessRequested) {
            // TODO: This node is currently utilising the token, so we need to wait for it to be released...
        }

        // Node is not currently requesting
        if (this.tokenLocation != TokenLocation.HERE) {
            accessRequested = true;

            // if the token is not here, then request the token from the token location
            if (this.tokenLocation == TokenLocation.ABOVE) {
                // if the token is above, then request the token from the parent
                this.parent.requestToken(this);
            } 
            else if (this.tokenLocation == TokenLocation.LEFT) {
                // if the token is left, then request the token from the left child
                this.left.requestToken(this);
            } 
            else if (this.tokenLocation == TokenLocation.RIGHT) {
                // if the token is right, then request the token from the right child
                this.right.requestToken(this);
            }
        }

        // TOKEN is now HERE
        accessRequested = false;
        if(requester == left) {
            tokenLocation = TokenLocation.LEFT;
        }
        else if(requester == right) {
            tokenLocation = TokenLocation.RIGHT;
        }
        else {
            tokenLocation = TokenLocation.ABOVE;
        }

        return true;
    }

    public void releaseToken() {
        accessRequested = false;

        // TODO: Somehow notify the next node waiting that the token is now available...
    }
}