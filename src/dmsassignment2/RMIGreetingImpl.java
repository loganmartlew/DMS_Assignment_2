/**
   A class that implements a remote object that holds a string
   greeting and whose main method registers an RMIGreetingImpl
   with the identifying name "greeting"
   Note this class should appear on the server
   @author Andrew Ensor
*/
import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

public class RMIGreetingImpl implements RMIGreeting
{
   private String message;
   
   public RMIGreetingImpl(String message)
   {  this.message = message;
   }
   
   public String getGreeting()
   {  System.out.println("getGreeting method called");
      return message;
   }
   
   // main method that creates an RMIGreetingImp and registers stub
   public static void main(String[] args)
   {  RMIGreetingImpl remoteObject
         = new RMIGreetingImpl("Hello Remote World");
      try
      {  // create stub (note prior to Java 5.0 must use rmic utility)
         RMIGreeting stub = (RMIGreeting)
            UnicastRemoteObject.exportObject(remoteObject, 0);
         // get the registry which is running on the default port 1099
         Registry registry = LocateRegistry.getRegistry();
         registry.rebind("greeting", stub);//binds if not already
         // display the names currently bound in the registry
         System.out.println("Names bound in RMI registry");
         try
         {  String[] bindings = Naming.list("localhost"); // no URL
            for (String name : bindings)
               System.out.println(name); 
         }
         catch (MalformedURLException e)
         {  System.err.println("Unable to see names: " + e);
         }
      }
      catch (RemoteException e)
      {  System.err.println("Unable to bind to registry: " + e);
      }
      // note that separate thread created to keep remoteObject alive
      System.out.println("Main method of RMIGreetingImpl done");
   }
}
