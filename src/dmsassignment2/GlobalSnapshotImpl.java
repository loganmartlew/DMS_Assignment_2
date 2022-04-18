/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dmsassignment2;

/**
 *
 * @author Logan
 */
public class GlobalSnapshotImpl implements GlobalSnapshot {
    private static final long ID = ProcessHandle.current().pid();
    
    private VectorTimestamp timestamp = new VectorTimestamp();
    
    private boolean ownTaken = false;
    private VectorTimestamp snapshot;
    
    public GlobalSnapshotImpl() {}
    
    public void recieveMessage() {
        if (!this.ownTaken) {
            this.snapshot = 
        }
    }
}
