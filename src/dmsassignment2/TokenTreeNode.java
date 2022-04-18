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
    
    private TokenTreeNode parent = null;
    private TokenTreeNode left = null;
    private TokenTreeNode right = null;
    
    private TokenLocation tokenLocation = TokenLocation.ABOVE;
    private boolean accessRequested = false;
    
    public TokenTreeNode() {}
    
    public void constructFullTree(List<TokenTreeNode> nodes, TokenTreeNode parent) {
        if (parent == null) {
            this.tokenLocation = TokenLocation.HERE;
        }
        
        
    }
}
