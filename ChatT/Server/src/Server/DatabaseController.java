
package Server;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import operation.Operation;

public class DatabaseController extends UnicastRemoteObject implements Operation {

    public DatabaseController() throws RemoteException{
        
    }

    @Override
    public void insertToDB(String username, String date, String type, String filename, String text) throws RemoteException {
        System.out.println("DataBaseController Accessesd");
        DatabaseManager.insertToDB(username, date, type, filename, text);
    }

    @Override
    public void getFromDB(String username) throws RemoteException {
        System.out.println("DataBaseController Accessed");
        DatabaseManager.getFromDB(username);
    } 
}
