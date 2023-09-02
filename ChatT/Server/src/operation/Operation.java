package operation;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface Operation extends Remote {
    
    public void insertToDB(String username, String date, String type, String filename, String text) throws RemoteException;
    
    public void getFromDB(String username) throws RemoteException;
}
