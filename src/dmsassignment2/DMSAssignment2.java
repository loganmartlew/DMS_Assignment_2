/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package dmsassignment2;


import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import static java.util.Arrays.asList;

/**
 *
 * @author logan
 */
public class DMSAssignment2 {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        Registry registry;
        try {
            LocateRegistry.createRegistry(1099);
        } catch (RemoteException e) {
            System.out.println("Can't create a new registry");
        }
        
        try {  // create stub (note prior to Java 5.0 must use rmic utility)
            // get the registry which is running on the default port 1099
            registry = LocateRegistry.getRegistry();
            
            if(asList(registry.list()).contains(PeerConnectionsImpl.NAME)){
                
            }
            
        } catch (RemoteException e) {
            System.err.println("Unable to bind to registry: " + e);
            return;
        }
        
        // note that separate thread created to keep remoteObject alive
        System.out.println("Main method of DMSAssignment2 done.");
    }

}
