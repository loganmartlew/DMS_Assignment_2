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
            this.tokenLocation = TokenLocation.HERE;
            // sort nodes by pid in descending order
            nodes.sort((TokenTreeNode n1, TokenTreeNode n2) -> {
                return n2.pid.compareTo(n1.pid);
            });
        }   // else location remains ABOVE

        if (nodes.size() == 0) {    // leaf node
            return;
        }

        if (nodes.size() == 1) {    // single node
            this.left = nodes.get(0);
            this.left.constructFullTree(parent, nodes.subList(1, nodes.size()));
            return;
        }

        // if there is a left and a right node, then set left node as left child and right node as right child and pass each half of the remaining nodes
        this.left = nodes.get(0);
        this.left.constructFullTree(this, nodes.subList(1, nodes.size() / 2 + 1));
        this.right = nodes.get(nodes.size() / 2 + 1);
        this.right.constructFullTree(this, nodes.subList(nodes.size() / 2 + 2, nodes.size()));
        
        
    }
}
