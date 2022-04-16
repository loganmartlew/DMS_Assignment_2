/**
   A remote interface that represents a greeting
   Note this interface should be on both the server and the client
   @see RMIGreetingImpl.java
*/
import java.rmi.Remote;
import java.rmi.RemoteException;

public interface RMIGreeting extends Remote
{
   public String getGreeting() throws RemoteException;   
}
